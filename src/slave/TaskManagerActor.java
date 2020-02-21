package slave;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.jetbrains.annotations.Nullable;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import shared.AkkaMessages.*;
import shared.AkkaMessages.modifyGraph.*;
import shared.AkkaMessages.select.SelectMsg;
import shared.PropertyHandler;
import shared.Utils;
import shared.VertexM;
import shared.antlr4.InputParser;
import shared.computation.Computation;
import shared.computation.ComputationCallback;
import shared.computation.ComputationRuntime;
import shared.computation.Vertex;
import shared.data.BoxMsg;
import shared.data.CompositeKey;
import shared.data.MultiKeyMap;
import shared.data.SynchronizedIterator;
import shared.exceptions.ComputationFinishedException;
import shared.resources.computationImpl.IngoingEdges;
import shared.resources.computationImpl.OutgoingEdges;
import shared.resources.computationImpl.PageRank;
import shared.resources.computationImpl.TriangleCounting;
import shared.selection.Partition;
import shared.selection.Select;
import shared.streamProcessing.PartitionStreamsHandler;
import shared.streamProcessing.StreamProcessingCallback;
import shared.variables.VariableGraph;
import shared.variables.solver.VariableSolver;

import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//LAST USED: private static final long serialVersionUID = 200063L;

public class TaskManagerActor extends AbstractActorWithStash implements ComputationCallback, StreamProcessingCallback {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


	private final String name;
	private final int numWorkers;
	private final String masterAddress;

	private ActorSelection master;
	private Map<Integer, ActorRef> slaves;

	private HashMap<String, VertexM> vertices;
	private HashMap<String, Computation> computations;
	private MultiKeyMap<ComputationRuntime> partitionComputations;
	private VariableSolver variables;

	private ThreadPoolExecutor executors;
	private final AtomicInteger waitingResponses = new AtomicInteger(0);

	private TaskManagerActor(String name, int numWorkers, String masterAddress) {
		this.name = name;
		this.numWorkers = numWorkers;
		this.masterAddress = masterAddress;
		this.computations = new HashMap<>();
		this.partitionComputations = null;
		this.variables = new VariableSolver();
	}

	@Override
	public void preStart() throws Exception {
		super.preStart();
		master = getContext().actorSelection(masterAddress);
		master.tell(new SlaveAnnounceMsg(name, 1), self());

		if (!Boolean.parseBoolean(PropertyHandler.getProperty("debugLog"))) {
			getContext().getSystem().eventStream().setLogLevel(0);
		}
	}

	@Override
	public Receive createReceive() {
		return preInitState();
	}

	/**
	 * States
	 */

	private final Receive preInitState() {
		return receiveBuilder(). //
		    match(DistributeHashMapMsg.class, this::onDistributeHashMapMsg). //
		    build();
	}

	private final Receive initializedState() {
		return receiveBuilder().
		    match(AddEdgeMsg.class, this::onAddEdgeMsg). //
		    match(DeleteEdgeMsg.class, this::onDeleteEdgeMsg). //
			match(DeleteVertexMsg.class, this::onDeleteVertexMsg). //
			match(UpdateVertexMsg.class, this::onUpdateVertexMsg). //
			match(UpdateEdgeMsg.class, this::onUpdateEdgeMsg). //
		    match(InstallComputationMsg.class, this::onInstallComputationMsg). //
		    match(StartComputationStepMsg.class, this::onStartComputationStepMsg). //
			match(ComputeResultsMsg.class, this::onComputeResultMsg). //
			match(RegisterVariableMsg.class, this::onRegisterVariableMsg). //
			match(NewPartitionMsg.class, this::onNewPartitionMsg). //
			match(ExtractMsg.class, this::onExtractMsg).
			match(SelectMsg.class, this::onSelectMsg).
			match(NewTimestampMsg.class, this::onNewTimestampMsg).
			match(SaveVariableGraphMsg.class, this::onSaveVariableGraphMsg). //
			match(RestoreVariableGraphMsg.class, this::onRestoreVariableGraphMsg). //
			match(BoxMsg.class, x->stash()). //
			match(ValidateNodesMsg.class, x->stash()). //
			match(Serializable.class, x-> System.err.println("received wrong message: " + x)).
		    build();
	}

	private final Receive waitingResponseState() {
		return receiveBuilder().
				match(BoxMsg.class, this::onInboxMsg). //
				match(AckMsg.class, this::onAckMsg). //
				match(Serializable.class, x->stash()).
				build();
	}

	private final Receive edgeValidation() {
		return receiveBuilder(). //
				match(ValidateNodesMsg.class, this::onValidateNodesMsg).
				match(InvalidNodesMsg.class, this::onInvalidNodesMsg).
				build();
	}

	/**
	 * Message processing
	 */

	private final void onDistributeHashMapMsg(DistributeHashMapMsg initMsg) {
		log.info(initMsg.toString());
		slaves = initMsg.getHashMapping();


		executors = new ThreadPoolExecutor(numWorkers, numWorkers, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

		//region: Initialize Graph
		vertices = new HashMap<>();

		BufferedReader reader;
		try {
			int numberOfLines = Utils.countLines(PropertyHandler.getProperty("datasetPath"));
			int ignoreLastRecords = Integer.parseInt(PropertyHandler.getProperty("numRecordsAsInput"));
			reader = new BufferedReader(new FileReader(PropertyHandler.getProperty("datasetPath")));
			String line = reader.readLine();
			for (int i = 0; i < numberOfLines-ignoreLastRecords; i++) {
				if (!line.isBlank()) {
					java.io.Serializable parsed = InputParser.parse(line);

					if ( parsed instanceof AddEdgeMsg && getActor(((AddEdgeMsg)parsed).getSourceName()).compareTo(self()) == 0 ) {
						AddEdgeMsg msg = (AddEdgeMsg) parsed;
						VertexM vertex = vertices.get(msg.getSourceName());
						vertex.addEdge(msg.getDestinationName());
						for (Pair<String, String[]> attribute : msg.getAttributes()) {
							vertex.setLabelEdge(msg.getDestinationName(), attribute.first(), attribute.second());
						}
					}
					else if (parsed instanceof UpdateEdgeMsg && getActor(((UpdateEdgeMsg)parsed).sourceId).compareTo(self()) == 0 ) {
						UpdateEdgeMsg msg = (UpdateEdgeMsg) parsed;
						updateEdgeOperations(msg);
					}
					else if (parsed instanceof DeleteEdgeMsg && getActor(((DeleteEdgeMsg)parsed).getSourceName()).compareTo(self()) == 0 ) {
						DeleteEdgeMsg msg = (DeleteEdgeMsg) parsed;
						VertexM vertex = vertices.get(msg.getSourceName());
						vertex.deleteEdge(msg.getDestinationName());
					}
					else if (parsed instanceof UpdateVertexMsg && getActor(((UpdateVertexMsg)parsed).vertexName).compareTo(self()) == 0 ) {
						UpdateVertexMsg msg = (UpdateVertexMsg) parsed;
						updateVertexOperations(msg);
					}
					else if (parsed instanceof DeleteVertexMsg && getActor(((DeleteVertexMsg)parsed).getVertexName()).compareTo(self()) == 0 ) {
						DeleteVertexMsg msg = (DeleteVertexMsg) parsed;
						vertices.remove(msg.getVertexName());
					}
				}
				line = reader.readLine();
			}
			reader.close();
			debugTaskManagerState();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//endregion

		//get some default computations
		defaultComputations();

		master.tell(new AckMsg(), self());

		getContext().become(initializedState());
	}

	private void updateVertexOperations(UpdateVertexMsg msg) {
		VertexM vertex = vertices.get(msg.getVertexName());
		if (vertex == null){ //Create a new vertex
			vertex = new VertexM(msg.vertexName, new VertexM.State());
		}
		for (Pair<String, String[]> attribute : msg.getAttributes()) {
			vertex.setLabelVartex(attribute.first(), attribute.second());
		}
		vertices.putIfAbsent(vertex.getNodeId(), vertex);
	}

	private void updateEdgeOperations(UpdateEdgeMsg msg) {
		VertexM vertex = vertices.get(msg.sourceId);
		if (vertex == null){
			vertex = new VertexM(msg.sourceId, new VertexM.State());
		}

		VertexM.State edgeState = vertex.getEdgeState(msg.destId);
		if (edgeState == null) {
			vertex.addEdge(msg.destId);
		}
		for (Pair<String, String[]> attribute : msg.getAttributes()) {
			edgeState.put(attribute.first(), attribute.second());
		}
	}


	private final void onAddEdgeMsg(AddEdgeMsg msg) {
		log.info(msg.toString());
		VertexM vertex = vertices.get(msg.getSourceName());
		vertex.addEdge(msg.getDestinationName());
		for (Pair<String, String[]> attribute : msg.getAttributes()) {
			vertex.setLabelEdge(msg.getDestinationName(), attribute.first(), attribute.second());
		}

		master.tell(new AckMsg(), self());
		debugTaskManagerState();
	}

	private final void onDeleteEdgeMsg(DeleteEdgeMsg msg) {
		log.info(msg.toString());
		VertexM vertex = vertices.get(msg.getSourceName());
		vertex.deleteEdge(msg.getDestinationName());

		master.tell(new AckMsg(), self());
		debugTaskManagerState();
	}

	private final void onDeleteVertexMsg(DeleteVertexMsg msg) {
		log.info(msg.toString());
		vertices.remove(msg.getVertexName());

		master.tell(new AckMsg(), self());
		debugTaskManagerState();
	}

	private final void onUpdateVertexMsg(UpdateVertexMsg msg) {
		log.info(msg.toString());
		updateVertexOperations(msg);
		master.tell(new AckMsg(), self());
		debugTaskManagerState();
	}

	private final void onUpdateEdgeMsg(UpdateEdgeMsg msg) {
		log.info(msg.toString());
		updateEdgeOperations(msg);
		master.tell(new AckMsg(), self());
		debugTaskManagerState();
	}

	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info(msg.toString());
		computations.put(msg.getIdentifier(), msg.getComputation());
		master.tell(new AckMsg(), self());
	}

	private final void onNewTimestampMsg(NewTimestampMsg msg) {
		log.info(msg.toString());
		variables.setCurrentTimestamp(msg.timestamp);
		this.partitionComputations = null;
		variables.removeOldVersions();
		master.tell(new AckMsg(), self());
	}

	private final void onValidateNodesMsg(ValidateNodesMsg msg) {
		log.info(msg.toString());
		MultiKeyMap<Set<String>> invalidNodesPartitions = new MultiKeyMap<>(msg.nodesToValidate.getKeys());
		msg.nodesToValidate.getAllElements().entrySet().parallelStream().forEach(group -> {

			Set<String> invalidNodes = Collections.synchronizedSet(new HashSet<>());
			Map<String, Vertex> vertices = this.partitionComputations.getValue(group.getKey()).getVertices();

			group.getValue().parallelStream().forEach(vertexId -> {
				if (!vertices.containsKey(vertexId)){
					invalidNodes.add(vertexId);
				}
			});
			invalidNodesPartitions.putValue(group.getKey().getKeysMapping(), invalidNodes);
		});

		InvalidNodesMsg invalidNodesMsg = new InvalidNodesMsg(invalidNodesPartitions);
		getSender().tell(invalidNodesMsg, self());

		if(this.waitingResponses.decrementAndGet() == 0){
			getContext().become(initializedState());
			master.tell(new AckMsg(), self());
		}
	}

	private final void onInvalidNodesMsg(InvalidNodesMsg msg) {
		log.info(msg.toString());
		msg.invalidNodes.getAllElements().entrySet().parallelStream().forEach(group -> {
			HashSet<String> deleteEdges = new HashSet<>(group.getValue());
			 Stream<Vertex> partitionVertices = this.partitionComputations.getValue(group.getKey()).getVertices().values().parallelStream();
			partitionVertices.forEach(vertex -> {
				VertexM vertexM = (VertexM) vertex;
				ArrayList<String> toRemove = new ArrayList<>();
				for (String edge: vertexM.getEdges()) {
					if (deleteEdges.contains(edge)) toRemove.add(edge);
				}
				vertexM.deleteEdges(deleteEdges);
			});
		});
		if(this.waitingResponses.decrementAndGet() == 0){
			getContext().become(initializedState());
			master.tell(new AckMsg(), self());
		}
	}

	private final void onSaveVariableGraphMsg(SaveVariableGraphMsg msg) {
		log.info(msg.toString());

		instantiatePartitionsIfAbsent();

		MultiKeyMap<Map<String, Vertex>> savedPartitions = new MultiKeyMap<>(this.partitionComputations.getKeys());

		this.partitionComputations.getAllElements().entrySet().parallelStream().forEach(entryPartition -> {
			savedPartitions.putValue(entryPartition.getKey(), entryPartition.getValue().getVertices());
		});

		this.variables.addVariable(new VariableGraph(msg.getVarableName(), msg.getTimewindow(), this.variables.getCurrentTimestamp(), savedPartitions));

		master.tell(new AckMsg(), self());
	}

	private  final void onRestoreVariableGraphMsg(RestoreVariableGraphMsg msg) {
		log.info(msg.toString());

		/*
		For each partition, for each node, keep only vertices and nodes not deleted
		 */

		if	(msg.getVariableName() == null) {

			//Restore general partition
			this.partitionComputations = null;
			this.instantiatePartitionsIfAbsent();

		} else {

			MultiKeyMap<Map<String, Vertex>> newPartitions = this.variables.getGraphs(msg.getVariableName(), msg.getTimeAgo());

			newPartitions.getAllElements().entrySet().forEach(entryP -> {

				entryP.getValue().values().parallelStream()
						.filter(vertex -> this.vertices.get(vertex.getNodeId()) != null)
						.forEach(vertex -> {
							Arrays.asList(vertex.getEdges()).stream().forEach(edge -> {
								VertexM vertexM = ((VertexM) vertex);
								String[] edges = vertexM.getEdges();
								if (!(Arrays.asList(edges).contains(edge))) {
									vertexM.deleteEdge(edge);
								}
							});
						});
			});
			//todo create runtimes
		}
		master.tell(new AckMsg(), self());

	}

	private final void onStartComputationStepMsg(StartComputationStepMsg msg) throws ExecutionException, InterruptedException {

		log.info(msg.toString());
		boolean isCompleted = false; //If true computation has terminated for all runtimes

		//Performe it before first superstep only
		computationSetup(msg);


		//Run RuntimeComputation (in series)
		if (msg.getFreeVars() == null) {

			//Get all Runtimes and send messages
			int numCompleted = 0;
			for (ComputationRuntime computationRuntime: partitionComputations.getAllElements().values()) {
				try {
					computationRuntime.compute(msg.getStepNumber(), this.executors);
				} catch (ComputationFinishedException e) {
					numCompleted = numCompleted + 1;
				}
				if (numCompleted == partitionComputations.getAllElements().size()) {
					isCompleted = true;
				}
				sendOutBox(computationRuntime.getOutgoingMessages());

			}
		} else {

			//get and run the specific runtime and send messages
			ComputationRuntime computationRuntime = partitionComputations.getValue(msg.getFreeVars());
			try {
				computationRuntime.compute(msg.getStepNumber(), this.executors);
			} catch (ComputationFinishedException e) {
				isCompleted = true;
			}
			sendOutBox(computationRuntime.getOutgoingMessages());

		}

		if (isCompleted) {
			master.tell(new AckMsgComputationTerminated(), self());
			log.info("Computation has terminated");
		} else {
			log.info("Computation requires more steps: end of step " + msg.getStepNumber());
			master.tell(new AckMsg(), self());
		}

	}



	private final void onComputeResultMsg(ComputeResultsMsg msg) throws ExecutionException, InterruptedException {
		log.info(msg.toString());

		if (msg.getFreeVars() == null) {
			//Get all Runtimes, set return value variable name and send messages
			for (ComputationRuntime computationRuntime: partitionComputations.getAllElements().values()) {
				Computation currentComputation = computationRuntime.getComputation();
				currentComputation.setReturnVarNames(msg.getReturnVarNames());
				computationRuntime.computeResults(this.executors);
			}
		} else {
			//get and run the specific runtime and send messages
			ComputationRuntime computationRuntime = partitionComputations.getValue(msg.getFreeVars());
			Computation currentComputation = computationRuntime.getComputation();
			currentComputation.setReturnVarNames(msg.getReturnVarNames());
			computationRuntime.computeResults(this.executors);
		}

		master.tell(new AckMsg(), self());

		//debug only
		for (Tuple2<String, Long> name: msg.getReturnVarNames()) {
			variables.printVariable(name.f0);
		}

	}


	/**
	 * Handle incoming inbox registering messages
	 * @param incoming
	 * @implNote Get runtimes and add messages, than decrease waiting, send back ack and if waiting is zero change state
	 */
	private final void onInboxMsg(BoxMsg incoming){
		log.info("BoxMsg received from " + sender().toString() + " for stpnmb " + incoming.getStepNumber());

		//Set message partition
		Map<String, String> partition = incoming.getPartition();
		if (incoming.getPartition() == null) {
			partition = new HashMap<>();
			partition.put("all", "all");
		}
		ComputationRuntime computationRuntime = this.partitionComputations.getValue(partition);
		computationRuntime.updateIncomingMsgs(incoming);
		getSender().tell(new AckMsg(), self());
		this.onAckMsg(new AckMsg());

	}

	/**
	 * Other slave has received my Outgoing messages
	 */
	private final void onAckMsg(AckMsg msg){
		log.info(msg.toString());

		if (this.waitingResponses.decrementAndGet() == 0){
			getContext().become(initializedState());
			unstashAll();
		} else if (this.waitingResponses.get() < 0 ) {
			log.info("Warning: waiting responses = " + this.waitingResponses.get());
			this.waitingResponses.set(0);
			getContext().become(initializedState());
			unstashAll();
		}

		log.info("Waiting " + this.waitingResponses + " responses");
	}

	private final void onRegisterVariableMsg(RegisterVariableMsg msg) {
		log.info(msg.toString());

		this.variables.addVariable(msg.getVariable());
		master.tell(new AckMsg(), self());
	}

	private final void onNewPartitionMsg(NewPartitionMsg msg) throws ExecutionException, InterruptedException {
		log.info(msg.toString());

		this.instantiatePartitionsIfAbsent();
		List<String> newPartitioningLabels = new ArrayList<>();
		newPartitioningLabels.addAll(Arrays.asList(this.partitionComputations.getKeys()));
		newPartitioningLabels.addAll(msg.getPartitioningSolver().getNames());

		MultiKeyMap<ComputationRuntime> newRuntimes = new MultiKeyMap<>(newPartitioningLabels.toArray(String[]::new));

		for (ComputationRuntime computationRuntime: this.partitionComputations.getAllElements().values()) { //For each previous computation runtime

			//region: Get before partition
			HashMap<String, String> beforePartition;
			if (computationRuntime.getPartition() == null) {
				beforePartition = new HashMap<>();
				beforePartition.put("all", "all");
			} else{
				beforePartition = new HashMap<>(computationRuntime.getPartition());
			}
			//endregion

			Partition partition = new Partition(computationRuntime, msg.getPartitioningSolver(), this.variables, this.executors);
			partition.computeSubpartitions();


			if (msg.getPartitioningSolver().partitionOnEdge){
				//EdgePartitioning
				List<Tuple3<String, String, HashMap<String, String[]>>> collected = partition.getCollectedResultsEdges();

				//Flat collected partitioning
				ConcurrentMap<HashMap<String, String>, List<Tuple2<String, String>>> collectedPartitions = collected.parallelStream().map(tuple3 -> new Tuple3<>(tuple3.f0, tuple3.f1, elencatePartitions(tuple3.f2, beforePartition)))
						.map(tuple -> {
							ArrayList<Tuple3<String, String, HashMap<String, String>>> returnTuple = new ArrayList<>();
							for (HashMap<String, String> singlePartition : tuple.f2) {
								returnTuple.add(new Tuple3<>(tuple.f0, tuple.f1, singlePartition));
							}
							return returnTuple;
						}).flatMap(Collection::parallelStream)
						.collect(Collectors.groupingByConcurrent(tuple3 -> (HashMap<String, String>)tuple3.f2.clone(), Collectors.mapping(tuple3 -> new Tuple2<>(tuple3.f0, tuple3.f1), Collectors.toList())));

				ConcurrentMap<HashMap<String, String>, HashMap<Tuple2<String, String>, Tuple2<VertexM, String>>> collect = collectedPartitions.entrySet().stream().map(p -> {
					ConcurrentMap<Tuple2<String, String>, Tuple2<VertexM, String>> map = p.getValue().parallelStream()
							.collect(Collectors.toConcurrentMap(entry -> entry, entry -> new Tuple2<>(this.vertices.get(entry.f0), entry.f1)));
					return new Tuple2<>(p.getKey(), new HashMap<>(map));
				}).collect(Collectors.toConcurrentMap(t -> t.f0, t -> t.f1));

				collect.forEach((key, value) -> {
					newRuntimes.putValue(key, new ComputationRuntime(this, new LinkedHashMap<>(key)));

					ConcurrentMap<VertexM, List<Tuple2<VertexM, String>>> vertexAndList = value.values().parallelStream().collect(Collectors.groupingByConcurrent(entry -> entry.f0, Collectors.toList()));

					Map<String, Vertex> newVertices = vertexAndList.entrySet().parallelStream().map(vertex -> {

						List<String> listOfEdges = vertex.getValue().stream().map(e -> e.f1).collect(Collectors.toList());

						return vertex.getKey().getVertexView(listOfEdges, true);
					}).collect(Collectors.toMap(vertex -> vertex.getNodeId(), vertex -> vertex));

					newRuntimes.putValue(key, new ComputationRuntime(this, new LinkedHashMap<>(key)));
					newRuntimes.getValue(key).setVertices(newVertices);

				});


			} else {
				//Vertex partitioning
				List<Pair<String, HashMap<String, String[]>>> collected = partition.getCollectedResultsVertices();

				//Flat collected partitioning -> using parallel streams degrades performances
				Map<HashMap<String, String>, List<String>> collectedPartitions = collected.parallelStream()
						.map(tuple2 -> new Tuple2<>(tuple2.first(), elencatePartitions(tuple2.second(), beforePartition)))
						.map(tuple -> {
							ArrayList<Tuple2<String, HashMap<String, String>>> returnTuple = new ArrayList<>();
							for (HashMap<String, String> singlePartition : tuple.f1) {
								returnTuple.add(new Tuple2<>(tuple.f0, singlePartition));
							}
							return returnTuple;
						})
						.flatMap(Collection::parallelStream)
						.collect(Collectors.groupingByConcurrent(tuple2 -> (HashMap<String, String>)tuple2.f1.clone(), Collectors.mapping(tuple2 -> tuple2.f0, Collectors.toList())));

				ConcurrentMap<HashMap<String, String>, HashMap<String, Vertex>> collect = collectedPartitions.entrySet().parallelStream()
						.map(p -> {
							ConcurrentMap<String, Vertex> map = p.getValue().parallelStream().collect(Collectors.toConcurrentMap(entry -> entry, entry -> computationRuntime.getVertices().get(entry)));
							return new Tuple2<>(p.getKey(), new HashMap<>(map));
						}).collect(Collectors.toConcurrentMap(t -> t.f0, t -> t.f1));

				collect.forEach((key, value) -> {
					newRuntimes.putValue(key, new ComputationRuntime(this, new LinkedHashMap<>(key)));
					newRuntimes.getValue(key).setVertices(value);
				});


			}

		}

		this.partitionComputations = newRuntimes;

		if (!(msg.getPartitioningSolver().partitionOnEdge)){
			this.validateEdges();
		}

	}

	private final void onExtractMsg (ExtractMsg msg) throws Exception {
		log.info(msg.toString());

		new PartitionStreamsHandler(this.partitionComputations, msg.getOperationsList(), variables, this)
				.solveOperationChain();

		master.tell(new AckMsg(), self());

		//variables.printAllVariables();
	}

	private final void onSelectMsg (SelectMsg msg) {
		log.info(msg.toString());

		/*
				For each partition: do select
				if select on vertices: validate edges
				return ack to master
		 */
		this.instantiatePartitionsIfAbsent();

		this.partitionComputations.getAllElements().values().stream().forEach(computationRuntime -> {

			Collection<VertexM> verticesM =
					computationRuntime
							.getVertices()
							.values()
							.parallelStream()
							.map(vertex -> (VertexM)vertex)
							.collect(Collectors.toList());

			HashMap<String,String> newPartition = null;
			if (computationRuntime.getPartition()!= null) {
				newPartition = new HashMap<>(computationRuntime.getPartition());
			}

			Select select = new Select(msg.operations, verticesM.iterator(), this.variables, executors, newPartition);

			try {
				Map<String, Vertex> selected = select.performSelection();
				computationRuntime.setVertices(selected);
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		});

		if (msg.operations.hasEdgeToken()) {
			//Ack to master will be sent after validation operations
			validateEdges();
		} else {
			master.tell(new AckMsg(), self());
		}

	}

	/**
	 * Props for this actor
	 */
	public static final Props props(String name, int numMyWorkers, String jobManagerAddr) {
		return Props.create(TaskManagerActor.class, name, numMyWorkers, jobManagerAddr);
	}

	private Map<String, Vertex> getAllReadOnlyVertices(){
		return new HashMap<>(this.vertices);
	}

	/**
	 * Sent ougoing messages to other actors
	 * @param outgoingBox
	 */
	private void sendOutBox(BoxMsg outgoingBox) throws ExecutionException, InterruptedException {
		//Create an outbox for each ActorRef
		Map<ActorRef, BoxMsg> outboxes = new HashMap();
		for (ActorRef destinations: this.slaves.values()) {
			BoxMsg box = new BoxMsg(outgoingBox.getStepNumber());
			box.setPartition(outgoingBox.getPartition());
			outboxes.put(destinations, box);
		}
		//For each vertex destination retrieve the actor and populate its box
		SynchronizedIterator<Map.Entry<String, ArrayList>> destIterator = outgoingBox.getSyncIterator();
		Utils.parallelizeAndWait(executors, new PopulateOutbox(this, destIterator, outboxes));
			//Increase waiting response and send also if empty
		this.waitingResponses.addAndGet(outboxes.size()*2);
			for (Map.Entry<ActorRef, BoxMsg> destOutbox: outboxes.entrySet()) {

					destOutbox.getKey().tell(destOutbox.getValue(), self());

			}

		//Switch to waiting response state
		getContext().become(waitingResponseState());
		unstashAll();
		//End
	}

	private void computationSetup(StartComputationStepMsg msg) {
		//Set VariableSolver in ComputationParameters and set the Parameters list if first step
		if (msg.getStepNumber() == 0) {

			//Check computation name
			if (!this.computations.containsKey(msg.getComputationId())) {
				throw new NullPointerException("Computation " + msg.getComputationId() + " isn't registered on taskManager");
			}

			msg.getComputationParameters().setVariableSolver(this.variables);
			this.computations.get(msg.getComputationId()).setComputationParameters(msg.getComputationParameters());

			//If no partitions are available, no select or free variables have been allocated
			this.instantiatePartitionsIfAbsent();


			//set computation for each runtime
			for (ComputationRuntime computation: this.partitionComputations.getAllElements().values()) {
				computation.setComputation(this.computations.get(msg.getComputationId()));
			}

		}
	}


	private ActorRef getActor(String vertex) {
		return this.slaves.get(Utils.getPartition(vertex, this.slaves.size()));
	}

	/**
	 *
	 * @param compressed new elements key-values
	 * @param solved HashMap of pre-existing partitions
	 * @return
	 */
	private ArrayList<HashMap<String, String>> elencatePartitions(HashMap<String, String[]> compressed, @Nullable HashMap<String, String> solved){

		if (solved == null)
			return elencatePartitionsRecursion(compressed, new HashMap<>());
		else
			return elencatePartitionsRecursion(compressed, (HashMap<String, String>)solved.clone());

	}

	/**
	 *
	 * @param compressed
	 * @param solved will be modified!!
	 * @return
	 */
	private ArrayList<HashMap<String, String>> elencatePartitionsRecursion(HashMap<String, String[]> compressed, HashMap<String, String> solved){
		ArrayList<HashMap<String, String>> results = new ArrayList<>();
		//Basecase
		if (compressed.isEmpty()) {
			results.add(solved);
			return results;
		}
		//Recursion
		Map.Entry<String, String[]> element = compressed.entrySet().iterator().next();
		HashMap<String, String[]> subMap = (HashMap<String, String[]>) compressed.clone();
		subMap.remove(element.getKey());
		for (int i = 0; i < element.getValue().length; i++) {
			solved.put(element.getKey(), element.getValue()[i]);
			results.addAll(elencatePartitionsRecursion(subMap, solved));
		}
		return results;
	}

	private void instantiatePartitionsIfAbsent() {
		if (partitionComputations == null){
			ComputationRuntime computationRuntime = new ComputationRuntime(this, null, getAllReadOnlyVertices());
			this.partitionComputations = new MultiKeyMap<>(new String[]{"all"});
			HashMap<String, String> key = new HashMap<>();
			key.put("all", "all");
			this.partitionComputations.putValue(key, computationRuntime);

		}
	}

	public void validateEdges () {

		ArrayList<MultiKeyMap<Set<String>>> destinations = new ArrayList<>();

		for (int i = 0; i < this.slaves.keySet().size(); i++) {
			destinations.add(new MultiKeyMap<>(this.partitionComputations.getKeys()));
		}

		this.partitionComputations.getAllElements().entrySet().parallelStream()
				.forEach(computationRuntime -> {

					for (MultiKeyMap<Set<String>> taskManager : destinations) {
						taskManager.putValue(computationRuntime.getKey().getKeysMapping(), Collections.synchronizedSet(new HashSet<>()));
					}

					computationRuntime.getValue().getVertices().values().parallelStream().map(vertex -> {
						VertexM vertexM = (VertexM) vertex;
						return (Arrays.asList(vertexM.getEdges()));
					}).flatMap(list -> list.stream()).forEach(edge -> {
						int destination = Utils.getPartition(edge, this.slaves.size());
						destinations.get(destination).getValue(computationRuntime.getKey()).add(edge);
					});

				});


		this.waitingResponses.set(this.slaves.size()*2); //2 times slaves
		getContext().become(edgeValidation());
		unstashAll();

		for (int i = 0; i < destinations.size(); i++) {
			MultiKeyMap<Set<String>> destination = destinations.get(i);
			this.slaves.get(i).tell(new ValidateNodesMsg(destination, null), self());
		}

	}


	private void defaultComputations() {
		this.computations.putIfAbsent("IngoingEdges", new IngoingEdges());
		this.computations.putIfAbsent("OutgoingEdges", new OutgoingEdges());
		this.computations.putIfAbsent("PageRank", new PageRank());
		this.computations.putIfAbsent("TriangleCounting", new TriangleCounting());
	}

	@Override
	public Aggregate getAggregatedResult(Aggregate aggregate) throws Exception {
		final Aggregate response;
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		scala.concurrent.Future<Object> future = Patterns.ask(master, new AggregateMsg(aggregate), timeout);
		response = (Aggregate) Await.result(future, timeout.duration());
		return response;
	}

	@Override
	public void forwardAndForgetToMaster(Aggregate aggregate) throws Exception {
		this.master.tell(aggregate, self());
	}


	private static class PopulateOutbox implements Utils.DuplicableRunnable {

		private final TaskManagerActor taskManagerActor;
		private final SynchronizedIterator<Map.Entry<String, ArrayList>> destIterator;
		private final Map<ActorRef, BoxMsg> outboxes;

		public PopulateOutbox(TaskManagerActor taskManagerActor, SynchronizedIterator<Map.Entry<String, ArrayList>> destIterator, Map<ActorRef, BoxMsg> outboxes) {
			this.taskManagerActor = taskManagerActor;
			this.destIterator = destIterator;
			this.outboxes = outboxes;
		}

		@Override
		public Utils.DuplicableRunnable getCopy() {
			return new PopulateOutbox(this.taskManagerActor, this.destIterator, this.outboxes);
		}

		@Override
		public void run() {

			try {
				while (true) {
					//From outgoingBox <destination,Messages> -> <Actor, <destination, Messages>>
					Map.Entry<String, ArrayList> vertex = destIterator.next();
					ActorRef destActor = taskManagerActor.getActor(vertex.getKey());
					outboxes.get(destActor).put(vertex.getKey(), vertex.getValue());
				}
			} catch (NoSuchElementException e){ /* END */}
		}
	}

	@Override
	public void registerComputationResult(String vertexName, Tuple2<String, Long> variableName, String[] values, @Nullable Map<String, String> partition) { //Update
		// Add to variable and create variable if not present
		try {
			this.variables.addToVariable(variableName, VariableSolver.VariableType.VERTEX, partition, new Tuple2<>(vertexName, Arrays.asList(values)));
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
			System.err.println("Unable to add to variable");
		}
	}

	@Override
	public VariableSolver getVariableSolver() {
		return this.variables;
	}

	private void debugTaskManagerState() {
		/*
		for (VertexM vertex: this.vertices.values()) {
			log.info(vertex.toString());
		}
		 */
	}

	@Override
	public void postStop() {
		this.executors.shutdown();
		super.postStop();
		PropertyHandler.exit();
	}

}

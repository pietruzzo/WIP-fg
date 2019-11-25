package slave;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.japi.Procedure;
import akka.pattern.Patterns;
import akka.util.Timeout;
import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple2;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import shared.AkkaMessages.*;
import shared.AkkaMessages.modifyGraph.AddEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteVertexMsg;
import shared.AkkaMessages.modifyGraph.UpdateVertexMsg;
import shared.AkkaMessages.select.SelectMsg;
import shared.Utils;
import shared.VertexM;
import shared.computation.*;
import shared.data.BoxMsg;
import shared.data.MultiKeyMap;
import shared.data.SynchronizedIterator;
import shared.exceptions.ComputationFinishedException;
import shared.selection.Partition;
import shared.selection.Select;
import shared.streamProcessing.StreamProcessingCallback;
import shared.variables.VariableGraph;
import shared.variables.solver.VariableSolver;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//LAST USED: private static final long serialVersionUID = 200060L;

public class TaskManagerActor extends AbstractActor implements ComputationCallback, StreamProcessingCallback {
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
		master.tell(new SlaveAnnounceMsg(name, numWorkers), self());
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
		    build();
	}

	private final Receive waitingResponseState() {
		return receiveBuilder().
				match(BoxMsg.class, this::onInboxMsg). //
				match(AckMsg.class, this::onAckMsg). //
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

		master.tell(new AckMsg(), self());

		getContext().become(initializedState());
	}



	private final void onAddEdgeMsg(AddEdgeMsg msg) {
		log.info(msg.toString());
		VertexM vertex = vertices.get(msg.getSourceName());
		vertex.addEdge(msg.getDestinationName());
		for (Pair<String, String[]> attribute : msg.getAttributes()) {
			vertex.setLabelEdge(msg.getDestinationName(), attribute.first(), attribute.second());
		}

		master.tell(new AckMsg(), self());
	}

	private final void onDeleteEdgeMsg(DeleteEdgeMsg msg) {
		log.info(msg.toString());
		VertexM vertex = vertices.get(msg.getSourceName());
		vertex.deleteEdge(msg.getDestinationName());

		master.tell(new AckMsg(), self());
	}

	private final void onDeleteVertexMsg(DeleteVertexMsg msg) {
		log.info(msg.toString());
		vertices.remove(msg.getVertexName());

		master.tell(new AckMsg(), self());
	}

	private final void onUpdateVertexMsg(UpdateVertexMsg msg) {
		log.info(msg.toString());
		VertexM vertex = vertices.get(msg.getVertexName());
		if (vertex == null){ //Create a new vertex
			vertex = new VertexM(msg.vertexName, new VertexM.State());
		}
		for (Pair<String, String[]> attribute : msg.getAttributes()) {
			vertex.setLabelVartex(attribute.first(), attribute.second());
		}
		master.tell(new AckMsg(), self());
	}

	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info(msg.toString());
		computations.put(msg.getIdentifier(), msg.getComputation());
		master.tell(new AckMsg(), self());
	}

	private final void onNewTimestampMsg(NewTimestampMsg msg) {
		log.info(msg.toString());
		variables.setCurrentTimestamp(msg.timestamp);
		variables.removeOldVersions();
		master.tell(new AckMsg(), self());
	}

	private final void onValidateNodesMsg(ValidateNodesMsg msg) {

		MultiKeyMap<Set<String>> invalidNodesPartitions = new MultiKeyMap<>(msg.nodesToValidate.getKeys());
		msg.nodesToValidate.getAllElements().entrySet().parallelStream().forEach(entry -> {

			Set<String> invalidNodes = Collections.synchronizedSet(new HashSet<>());
			Map<String, Vertex> vertices = this.partitionComputations.getValue(entry.getKey()).getVertices();

			entry.getValue().parallelStream().forEach(vertexId -> {
				if (!vertices.containsKey(vertexId)){
					invalidNodes.add(vertexId);
				}
			});
			invalidNodesPartitions.putValue(entry.getKey().getKeysMapping(), invalidNodes);
		});

		if(this.waitingResponses.decrementAndGet() == 0){
			getContext().become(initializedState());
		}
	}

	private final void onInvalidNodesMsg(InvalidNodesMsg msg) {
		msg.invalidNodes.getAllElements().entrySet().parallelStream().forEach(entry -> {
			HashSet<String> deleteEdges = new HashSet<>(entry.getValue());
			 Stream<Vertex> partitionVertices = this.partitionComputations.getValue(entry.getKey()).getVertices().values().parallelStream();
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

		instantiatePartitionsIfAbsent();

		MultiKeyMap<Map<String, Vertex>> savedPartitions = new MultiKeyMap<>(this.partitionComputations.getKeys());

		this.partitionComputations.getAllElements().entrySet().parallelStream().forEach(entryPartition -> {
			savedPartitions.putValue(entryPartition.getKey(), entryPartition.getValue().getVertices());
		});

		this.variables.addVariable(new VariableGraph(msg.getVarableName(), msg.getTimewindow(), this.variables.getCurrentTimestamp(), savedPartitions));

		master.tell(new AckMsg(), self());
	}

	private  final void onRestoreVariableGraphMsg(RestoreVariableGraphMsg msg) {

		/*
		For each partition, for each node, keep only vertices and nodes not deleted
		 */

		MultiKeyMap<Map<String, Vertex>> newPartitions = this.variables.getGraphs(msg.getVariableName(), msg.getTimeAgo());

		newPartitions.getAllElements().entrySet().stream().forEach(entryP ->{

			entryP.getValue().values().parallelStream()
					.filter(vertex -> this.vertices.get(vertex.getNodeId()) != null)
					.forEach(vertex -> {
						Arrays.asList(vertex.getEdges()).stream().forEach(edge -> {
							VertexM vertexM = ((VertexM) vertex);
							String[] edges = vertexM.getEdges();
							if ( !(Arrays.asList(edges).contains(edge)) ){
								vertexM.deleteEdge(edge);
							}
						});
					});
		});

		master.tell(new AckMsg(), self());

	}

	private final void onStartComputationStepMsg(StartComputationStepMsg msg) throws ExecutionException, InterruptedException {

		log.info(msg.toString());
		boolean isCompleted = false; //If true computation has terminated for all runtimes

		//If no partitions are available, no select or free variables have been allocated
		this.instantiatePartitionsIfAbsent();

		//set computation for each runtime
		for (ComputationRuntime computation: this.partitionComputations.getAllElements().values()) {
			computation.setComputation(this.computations.get(msg.getComputationId()));
		}
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
		} else {
			master.tell(new AckMsg(), self());
		}

	}

	private final void onComputeResultMsg(ComputeResultsMsg msg) throws ExecutionException, InterruptedException {

		if (msg.getFreeVars() == null) {
			//Get all Runtimes and send messages
			for (ComputationRuntime computationRuntime: partitionComputations.getAllElements().values()) {
				computationRuntime.computeResults(this.executors);
			}
		} else {
			//get and run the specific runtime and send messages
			ComputationRuntime computationRuntime = partitionComputations.getValue(msg.getFreeVars());
			computationRuntime.computeResults(this.executors);
			sendOutBox(computationRuntime.getOutgoingMessages());
		}

		master.tell(new AckMsg(), self());

	}


	/**
	 * Handle incoming inbox registering messages
	 * @param incoming
	 * @implNote Get runtimes and add messages, than decrease waiting, send back ack and if waiting is zero change state
	 */
	private final void onInboxMsg(BoxMsg incoming){
		ComputationRuntime computationRuntime = this.partitionComputations.getValue(incoming.getPartition());
		computationRuntime.updateIncomingMsgs(incoming);
		getSender().tell(new AckMsg(), self());

		if (this.waitingResponses.decrementAndGet() == 0){
			getContext().become(initializedState());
		}

	}

	/**
	 * Other slave has received my Outgoing messages
	 */
	private final void onAckMsg(AckMsg msg){
		if (this.waitingResponses.decrementAndGet() == 0){
			getContext().become(initializedState());
		}
	}

	private final void onRegisterVariableMsg(RegisterVariableMsg msg) {
		this.variables.addVariable(msg.getVariable());
		master.tell(new AckMsg(), self());
	}

	private final void onNewPartitionMsg(NewPartitionMsg msg) throws ExecutionException, InterruptedException {

		List<String> newPartitioningLabels = new ArrayList<>();
		newPartitioningLabels.addAll(Arrays.asList(this.partitionComputations.getKeys()));
		newPartitioningLabels.addAll(msg.getPartitioningSolver().getNames());

		MultiKeyMap<ComputationRuntime> newRuntimes = new MultiKeyMap<>((String[])newPartitioningLabels.toArray());

		for (ComputationRuntime computationRuntime: this.partitionComputations.getAllElements().values()) { //For each previus computation runtime

			Partition partition = new Partition(computationRuntime, msg.getPartitioningSolver(), this.variables, this.executors);
			partition.computeSubpartitions();


			if (msg.getPartitioningSolver().partitionOnEdge){
				//EdgePartitioning
				List<Tuple3<String, String, HashMap<String, String[]>>> collected = partition.getCollectedResultsEdges();

				//Flat collected partitioning
				collected.parallelStream().map(tuple3 -> new Tuple3<>(tuple3.f0, tuple3.f1, elencatePartitions(tuple3.f2, null)))
						.map(tuple -> {
					ArrayList<Tuple3<String, String, HashMap<String, String>>> returnTuple = new ArrayList<>();
					for (HashMap<String, String> singlePartition: tuple.f2) {
						returnTuple.add(new Tuple3<>(tuple.f0, tuple.f1, singlePartition));
					}
					return returnTuple;
				}).flatMap(list -> list.parallelStream())
				/* From HashMap to a list of tuples
					1-	Get ComputationRuntime inside newRuntimes
							-Create new empty Runtime if not present
							-Create node without edges if node is not present
					2-	Get source node
					3-	Add edge to node
				 */
				.forEach(tuple3 -> {
					ComputationRuntime computation;
					try {
						 computation = newRuntimes.getValue(tuple3.f2);
						 if (computation == null) throw new IllegalArgumentException();
					} catch (IllegalArgumentException e ){
						computation = new ComputationRuntime(this, null, new LinkedHashMap<>(tuple3.f2));
						newRuntimes.putValue(tuple3.f2, computation);
					}

					VertexM globalVertex = this.vertices.get(tuple3.f0);

					VertexM vertex = (VertexM) computation.getVertices().get(tuple3.f0);
					if (vertex == null) {
						vertex = new VertexM(globalVertex.getNodeId(), globalVertex.getState());
						computation.getVertices().put(vertex.getNodeId(), vertex);
					}
					vertex.addEdge(tuple3.f1, globalVertex.getEdgeState(tuple3.f1));
				});


			} else {
				//Vertex partitioning
				List<Pair<String, HashMap<String, String[]>>> collected = partition.getCollectedResultsVertices();

				//Flat collected partitioning
				collected.parallelStream().map(tuple2 -> new Tuple2<>(tuple2.first(), elencatePartitions(tuple2.second(), null)))
						.map(tuple -> {
							ArrayList<Tuple2<String, HashMap<String, String>>> returnTuple = new ArrayList<>();
							for (HashMap<String, String> singlePartition: tuple.f1) {
								returnTuple.add(new Tuple2<>(tuple.f0, singlePartition));
							}
							return returnTuple;
						}).flatMap(list -> list.parallelStream())
						/* From HashMap to a list of tuples
                            1-	Get ComputationRuntime inside newRuntimes
                                    -Create new empty Runtime if not present
                                    -Create node without edges if node is not present
                            2-	Get source node
                            3-	Add edge to node
                         */
						.forEach(tuple2 -> {
							ComputationRuntime computation;
							try {
								computation = newRuntimes.getValue(tuple2.f1);
								if (computation == null) throw new IllegalArgumentException();
							} catch (IllegalArgumentException e ){
								computation = new ComputationRuntime(this, null, new LinkedHashMap<>(tuple2.f1));
								newRuntimes.putValue(tuple2.f1, computation);
							}

							VertexM globalVertex = this.vertices.get(tuple2.f0);

							VertexM vertex = (VertexM) computation.getVertices().get(tuple2.f0);
							if (vertex == null) {
								vertex = new VertexM(globalVertex.getNodeId(), globalVertex.getState());
								computation.getVertices().put(vertex.getNodeId(), vertex);
							}
						});
			}

		}

		this.partitionComputations = newRuntimes;

		if (!(msg.getPartitioningSolver().partitionOnEdge)){
			this.validateEdges();
		}

	}

	private final void onExtractMsg (ExtractMsg msg) {
		//TODO: From Runtime partitions to StreamPartitions
	}

	private final void onSelectMsg (SelectMsg msg) {
		/*
				For each partition: do select
				if select on vertices: validate edges
				return ack to master
		 */
		this.partitionComputations.getAllElements().values().stream().forEach(computationRuntime -> {

			Collection<VertexM> verticesM = computationRuntime.getVertices().values().parallelStream().map(vertex -> (VertexM)vertex).collect(Collectors.toList());
			Select select = new Select(msg.operations, verticesM.iterator(), this.variables, executors, new HashMap<>(computationRuntime.getPartition()));
			try {
				Map<String, Vertex> selected = select.performSelection();
				computationRuntime.setVertices(selected);
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		if (msg.operations.hasEdgeToken()) validateEdges();

		master.tell(new AckMsg(), self());
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
		this.waitingResponses.set(outboxes.size()*2);
			for (Map.Entry<ActorRef, BoxMsg> destOutbox: outboxes.entrySet()) {

					destOutbox.getKey().tell(destOutbox.getValue(), self());

			}

		//Switch to waiting response state
		getContext().become(waitingResponseState());
		//End
	}

	private ActorRef getActor(String vertex) {
		return this.slaves.get(Utils.getPartition(vertex, this.slaves.size()));
	}

	private ArrayList<HashMap<String, String>> elencatePartitions(HashMap<String, String[]> compressed, @Nullable HashMap<String, String> solved){
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
			if (solved == null) solved = new HashMap<>();
			solved.put(element.getKey(), element.getValue()[i]);
				results.addAll(elencatePartitions(subMap, solved));
		}
		return results;
	}

	private void instantiatePartitionsIfAbsent() {
		if (partitionComputations == null){
			ComputationRuntime computationRuntime = new ComputationRuntime(this, null, null, getAllReadOnlyVertices());
			this.partitionComputations = new MultiKeyMap<>(new String[]{"all"});
			HashMap<String, String> key = new HashMap<>();
			key.put("all", "all");
			this.partitionComputations.putValue(key, computationRuntime);

			//Run first iteration on selected free variables, if NULL on all free variables
		}
	}

	public void validateEdges () {

		ArrayList<MultiKeyMap<Set<String>>> destinations = new ArrayList<>();

		for (int i = 0; i < this.slaves.keySet().size(); i++) {
			destinations.add(new MultiKeyMap<>(this.partitionComputations.getKeys()));
		}

		this.partitionComputations.getAllElements().entrySet().parallelStream()
				.forEach(entry -> {

					for (MultiKeyMap<Set<String>> taskManager : destinations) {
						taskManager.putValue(entry.getKey().getKeysMapping(), Collections.synchronizedSet(new HashSet<>()));
					}

					entry.getValue().getVertices().values().parallelStream().map(vertex -> {
						VertexM vertexM = (VertexM) vertex;
						return (Arrays.asList(vertexM.getEdges()));
					}).flatMap(list -> list.stream()).forEach(edge -> {
						int destination = Utils.getPartition(edge, this.slaves.size());
						destinations.get(destination).getValue(entry.getKey().getKeysMapping()).add(edge);
					});

				});

		for (int i = 0; i < destinations.size(); i++) {
			MultiKeyMap<Set<String>> destination = destinations.get(i);
			this.slaves.get(i).tell(new ValidateNodesMsg(destination), self());
		}
		this.waitingResponses.set(this.slaves.size()*2); //2 times slaves
		getContext().become(edgeValidation());
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
	public void updateState(String vertexName, String key, String[] values) {
		vertices.get(vertexName).setLabelVartex(key, values);
	}
}

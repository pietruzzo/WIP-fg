package slave;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import shared.AkkaMessages.*;
import shared.AkkaMessages.modifyGraph.AddEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteVertexMsg;
import shared.AkkaMessages.modifyGraph.UpdateVertexMsg;
import shared.Utils;
import shared.VertexNew;
import shared.computation.*;
import shared.data.BoxMsg;
import shared.data.SynchronizedIterator;
import shared.selection.Variable;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManagerActor extends AbstractActor implements ComputationCallback {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private final String name;
	private final int numWorkers;
	private final String masterAddress;

	private ActorSelection master;
	private Map<Integer, ActorRef> slaves;

	private HashMap<String, VertexNew> vertices;
	private HashMap<String, Computation> computations;
	private PartitionComputation partitionComputation;
	private HashMap<String, Variable> variables;

	private ThreadPoolExecutor executors;
	private final AtomicInteger waitingResponses = new AtomicInteger(0);

	private TaskManagerActor(String name, int numWorkers, String masterAddress) {
		this.name = name;
		this.numWorkers = numWorkers;
		this.masterAddress = masterAddress;
		this.computations = new HashMap<>();
		this.partitionComputation = null;
		this.variables = new HashMap<>();
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
		    build();
	}

	private final Receive waitingResponseState() {
		return receiveBuilder().
				match(BoxMsg.class, this::onInboxMsg). //
				match(AckMsg.class, this::onAckMsg). //
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
		VertexNew vertex = vertices.get(msg.getSourceName());
		vertex.addEdge(msg.getDestinationName());
		for (Pair<String, String[]> attribute : msg.getAttributes()) {
			vertex.setLabelEdge(msg.getDestinationName(), attribute.first(), attribute.second());
		}

		master.tell(new AckMsg(), self());
	}

	private final void onDeleteEdgeMsg(DeleteEdgeMsg msg) {
		log.info(msg.toString());
		VertexNew vertex = vertices.get(msg.getSourceName());
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
		VertexNew vertex = vertices.get(msg.getVertexName());
		if (vertex == null){ //Create a new vertex
			vertex = new VertexNew(msg.vertexName, new VertexNew.State());
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

	private final void onStartComputationStepMsg(StartComputationStepMsg msg) throws ExecutionException, InterruptedException {
		log.info(msg.toString());

		//If no partitions are available, no select or free variables have been allocated
		if (partitionComputation == null){
			this.partitionComputation = new PartitionComputation.Leaf(new ComputationRuntime(this, msg.getTimestamp(), this.computations.get(msg.getComputationId()), null, getAllReadOnlyVertices()));
			//Run first iteration on selected free variables, if NULL on all free variables
		}
		//Run RuntimeComputation (in series)
		if (msg.getFreeVars() == null) {
			//Get all Runtimes and send messages
			for (ComputationRuntime computationRuntime: partitionComputation.getAll()) {
				computationRuntime.compute(msg.getStepNumber(), this.executors);
				sendOutBox(computationRuntime.getOutgoingMessages());
			}
		} else {
			//get and run the specific runtime and send messages
			ComputationRuntime computationRuntime = partitionComputation.get(msg.getFreeVars());
			computationRuntime.compute(msg.getStepNumber(), this.executors);
			sendOutBox(computationRuntime.getOutgoingMessages());
		}
		master.tell(new AckMsg(), self());

	}

	private final void onComputeResultMsg(ComputeResultsMsg msg) throws ExecutionException, InterruptedException {
		if (msg.getFreeVars() == null) {
			//Get all Runtimes and send messages
			for (ComputationRuntime computationRuntime: partitionComputation.getAll()) {
				computationRuntime.computeResults(this.executors);
			}
		} else {
			//get and run the specific runtime and send messages
			ComputationRuntime computationRuntime = partitionComputation.get(msg.getFreeVars());
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
		ComputationRuntime computationRuntime = this.partitionComputation.get(incoming.getPartition());
		computationRuntime.updateIncomingMsgs(incoming);
		getSender().tell(new AckMsg(), self());
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
		this.variables.put(msg.getVariable().name, msg.getVariable());
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
			//Increase waiting response and send if not empty
			for (Map.Entry<ActorRef, BoxMsg> destOutbox: outboxes.entrySet()) {
				if (!destOutbox.getValue().isEmpty()){
					this.waitingResponses.incrementAndGet();
					destOutbox.getKey().tell(destOutbox.getValue(), self());
				}
			}

		//Switch to waiting response state
		getContext().become(waitingResponseState());
		//End
	}

	private ActorRef getActor(String vertex) {
		return this.slaves.get(Utils.getPartition(vertex, this.slaves.size()));
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

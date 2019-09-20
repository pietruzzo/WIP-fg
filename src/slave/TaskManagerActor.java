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
import shared.Vertex;
import shared.computation.Computation;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManagerActor extends AbstractActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private final String name;
	private final int numWorkers;
	private final String masterAddress;

	private ActorSelection master;
	private Map<Integer, ActorRef> slaves;

	private HashMap<String, Vertex> vertices;
	private HashMap<String, Computation> computations;

	private ThreadPoolExecutor executors;

	private TaskManagerActor(String name, int numWorkers, String masterAddress) {
		this.name = name;
		this.numWorkers = numWorkers;
		this.masterAddress = masterAddress;
		this.computations = new HashMap<>();
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
		    //match(ComputationMsg.class, this::onComputationMsg). //
		    //match(ResultRequestMsg.class, this::onResultRequestMsg). //
		    //match(ResultReplyMsg.class, this::onResultReplyMsg). //
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
		Vertex vertex = vertices.get(msg.getSourceName());
		vertex.addEdge(msg.getDestinationName());
		for (Pair<String, String> attribute : msg.getAttributes()) {
			vertex.state.addToState(attribute.first(), attribute.second(), msg.getTimestamp());
		}

		master.tell(new AckMsg(), self());
	}

	private final void onDeleteEdgeMsg(DeleteEdgeMsg msg) {
		log.info(msg.toString());
		Vertex vertex = vertices.get(msg.getSourceName());
		vertex.removeEdge(msg.getDestinationName());

		master.tell(new AckMsg(), self());
	}

	private final void onDeleteVertexMsg(DeleteVertexMsg msg) {
		log.info(msg.toString());
		vertices.remove(msg.getVertexName());

		master.tell(new AckMsg(), self());
	}

	private final void onUpdateVertexMsg(UpdateVertexMsg msg) {
		log.info(msg.toString());
		Vertex vertex = vertices.get(msg.getVertexName());
		for (Pair<String, String> attribute : msg.getAttributes()) {
			vertex.state.addToState(attribute.first(), attribute.second(), msg.getTimestamp());
		}
		master.tell(new AckMsg(), self());
	}

	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info(msg.toString());
		computations.put(msg.getIdentifier(), msg.getComputation());
		master.tell(new AckMsg(), self());
	}

	private final void onStartComputationStepMsg(StartComputationStepMsg msg) {
		log.info(msg.toString());

		//Todo
		//Allocate ComputationRuntime if step = 0
		//Run RuntimeComputation (in parallel)


	}

	private final void onInboxesMsg(){

	}

	/**
	 * Props for this actor
	 */
	public static final Props props(String name, int numMyWorkers, String jobManagerAddr) {
		return Props.create(TaskManagerActor.class, name, numMyWorkers, jobManagerAddr);
	}
}

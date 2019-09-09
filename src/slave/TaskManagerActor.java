package slave;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import shared.AkkaMessages.DistributeHashMapMsg;
import shared.AkkaMessages.LaunchAckMsg;
import shared.AkkaMessages.SlaveAnnounceMsg;
import shared.AkkaMessages.modifyGraph.AddEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteVertexMsg;
import shared.AkkaMessages.modifyGraph.UpdateVertexMsg;
import shared.Vertex;
import shared.data.DataSet;

import java.util.*;
import java.util.Map.Entry;
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

	private final DataSet vertices = new DataSet();

	private ThreadPoolExecutor executors;
	//private  VertexCentricComputation computation=null;

	// State for vertex centric computation
	//private InOutboxImpl superstepBox;

    //variables for result unification
	//private List<ResultReplyMsg> msgBuffer = new ArrayList<>();
	//int msgCount = 0;

	private TaskManagerActor(String name, int numWorkers, String masterAddress) {
		this.name = name;
		this.numWorkers = numWorkers;
		this.masterAddress = masterAddress;
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
		    //match(InstallComputationMsg.class, this::onInstallComputationMsg). //
		    //match(StartComputationMsg.class, this::onStartComputationMsg). //
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

		master.tell(new LaunchAckMsg(), self());

		getContext().become(initializedState());
	}



	private final void onAddEdgeMsg(AddEdgeMsg msg) {
		log.info(msg.toString());
		Vertex vertex = vertices.get(msg.getSourceName());
		vertex.addEdge(msg.getDestinationName());
		for (Pair<String, String> attribute : msg.getAttributes()) {
			vertex.state.addToState(attribute.first(), attribute.second(), msg.getTimestamp());
		}

		master.tell(new LaunchAckMsg(), self());
	}

	private final void onDeleteEdgeMsg(DeleteEdgeMsg msg) {
		log.info(msg.toString());
		Vertex vertex = vertices.get(msg.getSourceName());
		vertex.removeEdge(msg.getDestinationName());

		master.tell(new LaunchAckMsg(), self());
	}

	private final void onDeleteVertexMsg(DeleteVertexMsg msg) {
		log.info(msg.toString());
		vertices.remove(msg.getVertexName());

		master.tell(new LaunchAckMsg(), self());
	}

	private final void onUpdateVertexMsg(UpdateVertexMsg msg) {
		log.info(msg.toString());
		Vertex vertex = vertices.get(msg.getVertexName());
		for (Pair<String, String> attribute : msg.getAttributes()) {
			vertex.state.addToState(attribute.first(), attribute.second(), msg.getTimestamp());
		}

		master.tell(new LaunchAckMsg(), self());
	}
/*

	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info(msg.toString());
		computation=(VertexCentricComputation) msg.getComputationSupplier().get();
		workers.values().forEach(worker -> worker.tell(msg, self()));
	}

	private final void onStartComputationMsg(StartComputationMsg msg) {
		log.info(msg.toString());
		workers.values().forEach(worker -> worker.tell(msg, self()));
		numWaitingFor = workers.size();
		superstepBox = new InOutboxImpl<>();
	}

	private final void onComputationMsg(ComputationMsg msg) {
		log.info(msg.toString());
		if (msg.isFromJobManager()) {
			workers.values().forEach(worker -> worker.tell(msg, self()));
			log.info("AAAA");
			numWaitingFor = workers.size();
			superstepBox = new InOutboxImpl<>();
		} else {
			numWaitingFor--;
			for (final String recipient : (Set<String>) msg.recipients()) {
				for (final MsgSenderPair pair : (List<MsgSenderPair>) msg.messagesFor(recipient)) {
					superstepBox.add(recipient, pair);
				}
			}
			if (numWaitingFor == 0) {
				master.tell(new ComputationMsg<>(superstepBox, msg.getSuperstep(), false), self());
			}
		}
	}

	private final void onResultRequestMsg(ResultRequestMsg msg) {
		log.info(msg.toString());


		log.info(msg.toString());
		//wait for multiple message before aggregation
        workers.values().stream().forEach(worker -> worker.tell(msg, self()));
		//workers.values().stream().findAny().ifPresent(worker -> worker.tell(msg, self()));
	}

	private final void onResultReplyMsg(ResultReplyMsg msg) {
		log.info(msg.toString());
		msgCount++;
		if (msgCount==workers.size()) {
			msgBuffer.add(msg);
			Set<HashSet<HashSet<String>>> results = new HashSet<>();
			for (ResultReplyMsg msgb : msgBuffer) {
				HashSet<HashSet<String>> msgResult = (HashSet<HashSet<String>>) msgb.getResult();

				results.add(msgResult);
			}
			HashSet<HashSet<String>> finalResult = (HashSet<HashSet<String>>) computation.mergeResults(results);
			msgCount = 0;
			msgBuffer.clear();
			master.tell(new ResultReplyMsg<>(finalResult), sender());

		} else {
			msgBuffer.add(msg);
		}

	}
*/
	/**
	 * Props for this actor
	 */
	public static final Props props(String name, int numMyWorkers, String jobManagerAddr) {
		return Props.create(TaskManagerActor.class, name, numMyWorkers, jobManagerAddr);
	}
}

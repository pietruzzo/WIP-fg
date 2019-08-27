package slave;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import it.polimi.dist.graphproc.common.Utils;
import it.polimi.dist.graphproc.common.messages.TaskManagerAnnounceMsg;
import it.polimi.dist.graphproc.common.messages.TaskManagerInitMsg;
import it.polimi.dist.graphproc.common.messages.commands.ChangeEdgeMsg;
import it.polimi.dist.graphproc.common.messages.commands.ChangeVertexMsg;
import it.polimi.dist.graphproc.common.messages.vertexcentric.*;
import it.polimi.dist.graphproc.vertexcentric.InOutboxImpl;
import it.polimi.dist.graphproc.vertexcentric.MsgSenderPair;
import it.polimi.dist.graphproc.vertexcentric.VertexCentricComputation;

import java.util.*;
import java.util.Map.Entry;

public class TaskManagerActor extends AbstractActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private final String name;
	private final int numMyWorkers;
	private final String masterAddress;

	private ActorSelection master;
	private final Map<ActorRef, Integer> slaves = new HashMap<>();
	//private  VertexCentricComputation computation=null;

	// State for vertex centric computation
	int numWaitingFor = 0;
	private InOutboxImpl superstepBox;

    //variables for result unification
	private List<ResultReplyMsg> msgBuffer = new ArrayList<>();
	int msgCount = 0;

	private TaskManagerActor(String name, int numMyWorkers, String masterAddress) {
		this.name = name;
		this.numMyWorkers = numMyWorkers;
		this.masterAddress = masterAddress;
	}

	@Override
	public void preStart() throws Exception {
		super.preStart();
		jobManager = getContext().actorSelection(jobManagerAddr);
		jobManager.tell(new TaskManagerAnnounceMsg(name, numMyWorkers), self());
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
		    match(TaskManagerInitMsg.class, this::onTaskManagerInitMsg). //
		    build();
	}

	private final Receive initializedState() {
		return receiveBuilder(). //
		    match(ChangeVertexMsg.class, this::onChangeVertexMsg). //
		    match(ChangeEdgeMsg.class, this::onChangeEdgeMsg). //
		    match(InstallComputationMsg.class, this::onInstallComputationMsg). //
		    match(StartComputationMsg.class, this::onStartComputationMsg). //
		    match(ComputationMsg.class, this::onComputationMsg). //
		    match(ResultRequestMsg.class, this::onResultRequestMsg). //
		    match(ResultReplyMsg.class, this::onResultReplyMsg). //
		    build();
	}

	/**
	 * Message processing
	 */

	private final void onTaskManagerInitMsg(TaskManagerInitMsg initMsg) {
		log.info(initMsg.toString());
		taskManagers = initMsg.getTaskManagers();
		numAllWorkers = initMsg.getNumWorkers();

		// Compute the identifier of my first worker
		int firstWorkerId = 0;
		for (final Entry<Integer, ActorRef> entry : taskManagers.entrySet()) {
			if (entry.getValue().equals(self())) {
				firstWorkerId = entry.getKey();
				break;
			}
		}

		// Initialize my workers
		for (int i = 0; i < numMyWorkers; i++) {
			final int workerId = firstWorkerId + i;
			final ActorRef worker = getContext().actorOf(WorkerActor.props(workerId), name + "_worker_" + i);
			workers.put(workerId, worker);
		}

		getContext().become(initializedState());
	}



	private final void onChangeVertexMsg(ChangeVertexMsg msg) {
		log.info(msg.toString());
		final int workerId = Utils.computeResponsibleWorkerFor(msg.getName(), numAllWorkers);
		workers.get(workerId).forward(msg, getContext());
	}

	private final void onChangeEdgeMsg(ChangeEdgeMsg msg) {
		log.info(msg.toString());
		final int workerId = Utils.computeResponsibleWorkerFor(msg.getSource(), numAllWorkers);
		workers.get(workerId).forward(msg, getContext());
	}

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
				jobManager.tell(new ComputationMsg<>(superstepBox, msg.getSuperstep(), false), self());
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
			jobManager.tell(new ResultReplyMsg<>(finalResult), sender());

		} else {
			msgBuffer.add(msg);
		}

	}

	/**
	 * Props for this actor
	 */
	public static final Props props(String name, int numMyWorkers, String jobManagerAddr) {
		return Props.create(TaskManagerActor.class, name, numMyWorkers, jobManagerAddr);
	}
}

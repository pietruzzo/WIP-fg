package master;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import it.polimi.dist.graphproc.common.Utils;
import it.polimi.dist.graphproc.common.messages.TaskManagerAnnounceMsg;
import it.polimi.dist.graphproc.common.messages.TaskManagerInitMsg;
import it.polimi.dist.graphproc.common.messages.commands.ChangeEdgeMsg;
import it.polimi.dist.graphproc.common.messages.commands.ChangeGraphMsg;
import it.polimi.dist.graphproc.common.messages.commands.ChangeVertexMsg;
import it.polimi.dist.graphproc.common.messages.commands.StartMsg;
import it.polimi.dist.graphproc.common.messages.vertexcentric.*;
import it.polimi.dist.graphproc.vertexcentric.InOutboxImpl;
import it.polimi.dist.graphproc.vertexcentric.Inbox;
import it.polimi.dist.graphproc.vertexcentric.MsgSenderPair;
import it.polimi.dist.graphproc.vertexcentric.VertexCentricComputation;
import shared.AkkaMessages.LaunchAckMsg;
import shared.AkkaMessages.DistributeHashMapMsg;
import shared.AkkaMessages.LaunchMsg;
import shared.AkkaMessages.SlaveAnnounceMsg;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class JobManagerActor extends AbstractActorWithStash {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	/**
	 * Ref to slave and associated number of threads
	 */
	private final Map<ActorRef, Integer> slaves = new HashMap<>();

	/**
	 * Map hash values to slaves
	 */
	private final Map<Integer, ActorRef> hashMapping = new HashMap<>();

	private final AtomicInteger waitingResponses = new AtomicInteger(0);
	/*

	private VertexCentricComputation computation = null;
	private List<ResultReplyMsg> msgBuffer = new ArrayList<>();
	int msgCount = 0;
	 */

	/**
	 * State for iterative computations
	 */
	/*
	private Map<Integer, InOutboxImpl> superstepMsgs = new HashMap<>();
	private int receivedReplies = 0;
	private int expectedReplies = 0;

	 */

	@Override
	public Receive createReceive() {
		return waitSlaves();
	}


	/**
	 * States
	 */


	private final Receive waitSlaves() { //Startup phase
		return receiveBuilder().
		    match(SlaveAnnounceMsg.class, this::onSlaveAnnounceMsg).
		    match(LaunchMsg.class, this::onLaunchMsg).
			match(LaunchAckMsg.class, this::onLaunchAckMsg).
		    build();
	}


	private final Receive receiveChangeState() { //Compute a new graph change
		return receiveBuilder().
		    match(InstallComputationMsg.class, this::onInstallComputationMsg).
		    match(ChangeEdgeMsg.class, this::onChangeEdgeMsg).
		    match(ChangeVertexMsg.class, this::onChangeVertexMsg).
		    build();
	}

	/*
	private final Receive iterativeComputationState() {
		return receiveBuilder(). //
		    match(ComputationMsg.class, this::onComputationMsg). //
		    match(ChangeGraphMsg.class, msg -> stash()). //
		    build();
	}

	private final Receive waitingForReplyState() {
		return receiveBuilder(). //
		    match(ResultReplyMsg.class, this::onResultReplyMsg). //
		    match(ChangeGraphMsg.class, msg -> stash()). //
		    build();
	}
	 */



	/**
	 * Message processing
	 */

	private final void onSlaveAnnounceMsg(SlaveAnnounceMsg msg) {
		log.info(msg.toString());
		slaves.put(getSender(), msg.numThreads);
	}


	private final void onLaunchMsg(LaunchMsg msg) {
		log.info(msg.toString());

		//region: map hashmap
		hashMapping.clear();
		int index = 0;
		for (Map.Entry<ActorRef, Integer> slave: slaves.entrySet()) {
			for (int i = 0; i < slave.getValue(); i++) {
				hashMapping.put(index, slave.getKey());
				index = index + 1;
			}
		}
		//endregion

		//Send mapping to slaves and wait ack
		waitingResponses.set(slaves.size());
		for (ActorRef slave: slaves.keySet()) {
			slave.tell(new DistributeHashMapMsg(hashMapping), self());
		}
	}

	private final void onLaunchAckMsg(LaunchAckMsg msg){
		if(waitingResponses.decrementAndGet() == 0)
			getContext().become(receiveChangeState());

	}

/*





	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info(msg.toString());
		computation = (VertexCentricComputation) msg.getComputationSupplier().get();
		for (final ActorRef taskManager : taskManagers.values()) {
			taskManager.tell(msg, self());
		}
	}


	private final void onChangeVertexMsg(ChangeVertexMsg msg) {
		log.info(msg.toString());
		final int responsibleWorker = Utils.computeResponsibleWorkerFor(msg.getName(), numWorkers);
		final ActorRef taskManager = taskManagers.floorEntry(responsibleWorker).getValue();
		taskManager.forward(msg, getContext());
		expectedReplies = taskManagers.size();
		receivedReplies = 0;

		//instead on invoking on each computation on each task manager we have to determine the change span
		//which will be different for each algorithm

		taskManagers.values().forEach(tm -> tm.tell(new StartComputationMsg(msg.timestamp()), self()));
		getContext().become(iterativeComputationState());
	}

	private final void onChangeEdgeMsg(ChangeEdgeMsg msg) {
		log.info(msg.toString());
		final int responsibleWorker = Utils.computeResponsibleWorkerFor(msg.getSource(), numWorkers);
		final ActorRef taskManager = taskManagers.floorEntry(responsibleWorker).getValue();
		taskManager.forward(msg, getContext());
		expectedReplies = taskManagers.size();
		receivedReplies = 0;
		taskManagers.values().forEach(tm -> tm.tell(new StartComputationMsg(msg.timestamp()), self()));
		getContext().become(iterativeComputationState());
	}

	private final void onComputationMsg(ComputationMsg msg) {
		log.info(msg.toString());
		for (final String recipientName : (Set<String>) msg.recipients()) {
			for (final MsgSenderPair p : (List<MsgSenderPair>) msg.messagesFor(recipientName)) {
				final int responsibleWorker = Utils.computeResponsibleWorkerFor(recipientName, numWorkers);
				final int taskManager = taskManagers.floorEntry(responsibleWorker).getKey();
				InOutboxImpl box = superstepMsgs.get(taskManager);
				if (box == null) {
					box = new InOutboxImpl<>();
					superstepMsgs.put(taskManager, box);
				}
				box.add(recipientName, p);
			}
		}
		receivedReplies++;

		// If all the replies §have been received, start a new superstep (if
		// necessary)
		if (receivedReplies == expectedReplies) {
			expectedReplies = superstepMsgs.size();
			// No more supersteps are necessary, send request for result

			if (expectedReplies == 0) {
				taskManagers. //
				    values(). //
						 stream(). //
					forEach(tm -> tm.tell(new ResultRequestMsg(), self()));
				getContext().become(waitingForReplyState());
			}
			// Start a new superstep
			else {
				final Set<ActorRef> involvedTaskManagers = new HashSet<>();
				for (final int taskMangerId : superstepMsgs.keySet()) {
					final ActorRef taskManager = taskManagers.get(taskMangerId);
					involvedTaskManagers.add(taskManager);
					final Inbox box = superstepMsgs.get(taskMangerId);
					taskManager.tell(new ComputationMsg<>(box, msg.getSuperstep() + 1, true), self());
				}
				expectedReplies = involvedTaskManagers.size();
				receivedReplies = 0;
				superstepMsgs = new HashMap<>();
			}
		}
	}

	private final void onResultReplyMsg(ResultReplyMsg msg) {
		log.info(msg.toString());
		//wait for multiple message before aggregation

		msgCount++;
		if (msgCount==taskManagers.size()) {
			Set<HashSet<HashSet<String>>> results = new HashSet<>();
			for (ResultReplyMsg msgb : msgBuffer) {
				HashSet<HashSet<String>> msgResult = (HashSet<HashSet<String>>) msgb.getResult();
				results.add(msgResult);
			}
			HashSet<HashSet<String>> finalResult = (HashSet<HashSet<String>>) computation.mergeResults(results);
			msgCount = 0;
			msgBuffer.clear();
			getContext().become(receiveChangeState());
			unstashAll();
		} else {
			msgBuffer.add(msg);
		}
	}
	 */

	/**
	 * Props for this actor
	 */
	public static final Props props() {
		return Props.create(JobManagerActor.class);
	}

	/*
		Utils functions
	 */

}

package master;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import shared.AkkaMessages.*;
import shared.AkkaMessages.modifyGraph.AddEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteEdgeMsg;
import shared.AkkaMessages.modifyGraph.DeleteVertexMsg;
import shared.AkkaMessages.modifyGraph.UpdateVertexMsg;
import shared.PartitionAssignment;
import shared.computation.Computation;

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
	private final HashMap<Integer, ActorRef> hashMapping = new HashMap<>();

	private final HashMap<String, Computation> computations = new HashMap<>();

	private final AtomicInteger waitingResponses = new AtomicInteger(0);

	private State nextState = this::waitSlaves;


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
			match(AckMsg.class, this::onLaunchAckMsg).
		    build();
	}

	private final Receive waitAck() { //Startup phase
		return receiveBuilder().
				match(AckMsg.class, this::onLaunchAckMsg).
				build();
	}


	private final Receive receiveChangeState() { //Compute a new graph change
		return receiveBuilder().
		    match(AddEdgeMsg.class, this::onAddEdgeMsg).
		    match(DeleteEdgeMsg.class, this::onDeleteEdgeMsg).
		    match(DeleteVertexMsg.class, this::onDeleteVertexMsg).
			match(UpdateVertexMsg.class, this::onUpdateVertexMsg).
			match(InstallComputationMsg.class, this::onInstallComputationMsg).
		    build();
	}

	private final Receive iterativeComputationState(){
		return null; //todo
	}


	/**
	 * Message processing
	 */

	private final void onAddEdgeMsg(AddEdgeMsg msg){
		ActorRef slave = getActor(msg.getSourceName());
		slave.tell(new AddEdgeMsg(msg.getSourceName(), msg.getDestinationName(), msg.getAttributes(), System.currentTimeMillis()), self());
		waitingResponses.set(1);
		nextState = this::iterativeComputationState;
		getContext().become(waitAck(), true);
	}
	private final void onDeleteEdgeMsg(DeleteEdgeMsg msg){
		ActorRef slave = getActor(msg.getSourceName());
		slave.tell(new DeleteEdgeMsg(msg.getSourceName(), msg.getDestinationName(), System.currentTimeMillis()), self());
		waitingResponses.set(1);
		nextState = this::iterativeComputationState;
		getContext().become(waitAck(), true);
	}
	private final void onDeleteVertexMsg(DeleteVertexMsg msg){
		ActorRef slave = getActor(msg.getVertexName());
		slave.tell(new DeleteVertexMsg(msg.getVertexName(),  System.currentTimeMillis()), self());
		waitingResponses.set(1);
		nextState = this::iterativeComputationState;
		getContext().become(waitAck(), true);
	}
	private final void onUpdateVertexMsg(UpdateVertexMsg msg){
		ActorRef slave = getActor(msg.getVertexName());
		slave.tell(new UpdateVertexMsg(msg.getVertexName(), msg.getAttributes(), System.currentTimeMillis()), self());
		waitingResponses.set(1);
		nextState = this::iterativeComputationState;
		getContext().become(waitAck(), true);
	}

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
		nextState = this::receiveChangeState;
		getContext().become(waitAck());
	}

	private final void onLaunchAckMsg(AckMsg msg){
		if(waitingResponses.decrementAndGet() == 0)
			getContext().become(nextState.invoke());

	}





	private final void onInstallComputationMsg(InstallComputationMsg msg) {
		log.info(msg.toString());
		computations.put(msg.getIdentifier(), msg.getComputation());
		for (final ActorRef slave : slaves.keySet()) {
			slave.tell(msg, self());
		}
		nextState = this::receiveChangeState;
		waitingResponses.set(slaves.size());
		getContext().become(waitAck());
	}

/*
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
	private ActorRef getActor(String name) {
		return hashMapping.get(PartitionAssignment.getPartition(name, hashMapping.size()));
	}


	@FunctionalInterface
	public interface State{
		Receive invoke();
	}
}

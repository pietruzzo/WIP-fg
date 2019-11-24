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
import shared.Utils;
import shared.computation.Computation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

	/**
	 * Clients Refs
	 */
	private final Set<ActorRef> clients = new HashSet<>();

	private final HashMap<String, Computation> computations = new HashMap<>();

	private final AtomicInteger waitingResponses = new AtomicInteger(0);

	private final ConcurrentHashMap<String, OngoingAggregate> aggregates = new ConcurrentHashMap<>();

	private long currentTimestamp;

	private State nextState = this::waitSlaves;

	private PatternLogic patternLogic;


	//region: getter/setter


	public LoggingAdapter getLog() {
		return log;
	}

	public Map<ActorRef, Integer> getSlaves() {
		return slaves;
	}

	public HashMap<Integer, ActorRef> getHashMapping() {
		return hashMapping;
	}

	public HashMap<String, Computation> getComputations() {
		return computations;
	}

	public AtomicInteger getWaitingResponses() {
		return waitingResponses;
	}

	public ConcurrentHashMap<String, OngoingAggregate> getAggregates() {
		return aggregates;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
	}

	public long getCurrentTimestamp() {
		return currentTimestamp;
	}

	public void setCurrentTimestamp(long currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	//endregion


	@Override
	public Receive createReceive() {
		return waitSlaves();
	}


	/**
	 * States
	 */


	private final Receive waitSlaves() { //Startup phase
		return receiveBuilder().
			match(HelloClientMsg.class, this::onHelloClientMsg).
		    match(SlaveAnnounceMsg.class, this::onSlaveAnnounceMsg).
		    match(LaunchMsg.class, this::onLaunchMsg).
			match(AckMsg.class, this::onLaunchAckMsg).
			match(AggregateMsg.class, this::onAggregateMsg).
		    build();
	}

	private final Receive waitAck() {
		return receiveBuilder().
				match(AckMsg.class, this::onLaunchAckMsg).
				match(AckMsgComputationTerminated.class, this::onLaunchAckMsg).
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

	private final void onHelloClientMsg(HelloClientMsg msg){
		this.clients.add(sender());

	}
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

	private final void onLaunchAckMsg(AckMsgComputationTerminated msg) {

		this.patternLogic.sendToCurrentPattern(msg);

		if(waitingResponses.decrementAndGet() == 0)
			getContext().become(nextState.invoke());

	}

	private final void onAggregateMsg(AggregateMsg msg){

		OngoingAggregate ongoing = this.aggregates.get(msg.aggregate.getTransactionId());
		ongoing.aggregates.add(msg.aggregate);
		ongoing.collectedActors.add(sender());

		//If finished -> remove aggregate and fire event
		if (ongoing.performOperatorIfCollectedAll()){

			String event = ongoing.getEvaluation();
			if ( event != null ){
				this.clients.forEach(client -> client.tell(new FireMsg(event), self()));
			}

			this.aggregates.remove(msg.aggregate.getTransactionId());

		}

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
		return hashMapping.get(Utils.getPartition(name, hashMapping.size()));
	}


	@FunctionalInterface
	public interface State{
		Receive invoke();
	}
}

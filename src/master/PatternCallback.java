package master;

import akka.actor.ActorRef;

import java.io.Serializable;

public interface PatternCallback {


    <Msg extends Serializable> void sendToAllSlaves(Msg message);

    long getCurrentTimestamp();

    void setNextStateIterativeComputationState ();

    void becomeReceiveChangeState ();

    void becomeAwaitAckFromAll();

    int getNumSlaves();

    void putInOngoingAggregateList (int identifier, OngoingAggregate ongoingAggregate);

    ActorRef getSelf();
}

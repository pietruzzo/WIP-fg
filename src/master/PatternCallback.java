package master;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;

import java.io.Serializable;

/**
 * Used by master business logic to communicate with workers
 */
public interface PatternCallback {


    <Msg extends Serializable> void sendToAllSlaves(Msg message);

    long getCurrentTimestamp();

    void becomeReceiveChangeState ();

    void becomeAwaitAckFromAll();

    int getNumSlaves();

    void putInOngoingAggregateList (int identifier, OngoingAggregate ongoingAggregate);

    ActorRef getSelf();

    LoggingAdapter getLogger ();
}

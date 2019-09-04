package shared.AkkaMessages;

import akka.actor.ActorRef;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DistributeHashMapMsg implements Serializable {
    private static final long serialVersionUID = 200007L;

    private final HashMap<Integer, ActorRef> hashMapping;

    public DistributeHashMapMsg(HashMap<Integer, ActorRef> hashMapping) {
        this.hashMapping = hashMapping;
    }

    public HashMap<Integer, ActorRef> getHashMapping() {
        return hashMapping;
    }
}

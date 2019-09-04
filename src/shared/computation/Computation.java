package shared.computation;

import akka.japi.Pair;
import shared.AkkaMessages.StepMessage;
import shared.Vertex;

import java.util.List;
import java.util.Set;

public interface Computation {

    /**
     * @param vertex a copy of the vertex
     * @param incoming incoming messages
     * @return outgoing messages
     */
    Set<StepMessage> first_Iterate (Vertex vertex, Set<StepMessage> incoming);

    /**
     * @param vertex copy of the vertex
     * @param incoming incoming messages
     * @return outgoing messages
     */
    Set<StepMessage> iterate (Vertex vertex, Set<StepMessage> incoming);

    /**
     *
     * @param vertex copy of the vertex
     * @return <Key, Value> pairs that will be saved in vertex state
     */
    List<Pair<String, String>> compute_result (Vertex vertex);



}
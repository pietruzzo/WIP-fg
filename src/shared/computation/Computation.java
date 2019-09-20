package shared.computation;

import akka.japi.Pair;

import java.io.Serializable;
import java.util.List;

public interface Computation<TMsg> extends Serializable {

    /**
     * @param vertex copy of the vertex
     * @param incoming incoming messages
     * @param iterationStep first iteration is number zero
     * @return outgoing messages
     */
    List<TMsg> iterate (VertexProxy vertex, List<TMsg> incoming, int iterationStep);

    /**
     * @param vertex copy of the vertex
     * @return outgoing messages
     */
    List<TMsg> firstIterate (VertexProxy vertex);

    /**
     *
     * @param vertex copy of the vertex
     * @return <Key, Value> pairs that will be saved in vertex state
     */
    List<Pair<String, String>> compute_result (VertexProxy vertex);



}
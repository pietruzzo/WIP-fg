package shared.computation;

import akka.japi.Pair;
import shared.Vertex;

import java.util.List;
import java.util.Set;

public interface Computation<TMsg> {

    /**
     * @param vertex copy of the vertex
     * @param incoming incoming messages
     * @param iterationStep first iteration is number zero
     * @return outgoing messages
     */
    List<TMsg> iterate (VertexProxy vertex, List<TMsg> incoming, int iterationStep);

    /**
     *
     * @param vertex copy of the vertex
     * @return <Key, Value> pairs that will be saved in vertex state
     */
    List<Pair<String, String>> compute_result (VertexProxy vertex);



}
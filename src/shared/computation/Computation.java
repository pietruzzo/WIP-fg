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
    List<TMsg> iterate (Vertex vertex, List<TMsg> incoming, int iterationStep);

    /**
     * @param vertex copy of the vertex
     * @return outgoing messages
     */
    List<TMsg> firstIterate (Vertex vertex);

    /**
     *
     * @param vertex copy of the vertex
     * @return THe list of <Key, Values[]> pairs that will be saved in vertex state
     */
    List<Pair<String, String[]>> compute_result (Vertex vertex);

    /**
     * Set computation parameters and resultLabelNames
     * @param parameters
     * @param resultLabelsNames
     */
    void setParametersAndReturnNames ( List<String> parameters, List<String> resultLabelsNames);


}
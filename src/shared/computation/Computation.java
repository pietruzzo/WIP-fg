package shared.computation;

import akka.japi.Pair;
import shared.AkkaMessages.StepMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Datastructures shared between nodes must be threadsafe
 */
public interface Computation extends Serializable {

    /**
     * @param vertex copy of the vertex
     * @param incoming incoming messages
     * @param iterationStep first iteration is number zero
     * @return outgoing messages
     */
    List<StepMsg> iterate (Vertex vertex, List<StepMsg> incoming, int iterationStep);

    /**
     * @param vertex copy of the vertex
     * @return outgoing messages
     */
    List<StepMsg> firstIterate (Vertex vertex);

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
    void preInitialize(String[] parameters, String[] resultLabelsNames);


}
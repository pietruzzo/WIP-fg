package shared.computation;

import akka.japi.Pair;
import shared.AkkaMessages.StepMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Datastructures shared between nodes must be threadsafe
 */
public abstract class Computation implements Serializable {

    protected ComputationParameters computationParameters;
    protected List<String> returnVarNames;


    public ComputationParameters getComputationParameters() {
        return computationParameters;
    }

    public void setComputationParameters(ComputationParameters computationParameters) {
        this.computationParameters = computationParameters;
    }

    public List<String> getReturnVarNames() {
        return returnVarNames;
    }

    public void setReturnVarNames(List<String> returnVarNames) {
        this.returnVarNames = returnVarNames;
    }


    /**
     * @param vertex copy of the vertex
     * @param incoming incoming messages
     * @param iterationStep first iteration is number zero
     * @return outgoing messages
     */
    public abstract List<StepMsg> iterate (Vertex vertex, List<StepMsg> incoming, int iterationStep);

    /**
     * @param vertex copy of the vertex
     * @return outgoing messages
     */
    public abstract List<StepMsg> firstIterate (Vertex vertex);

    /**
     *
     * @param vertex copy of the vertex
     * @return List of Pairs of (VariableName, values)
     */
    public abstract List<Pair<String, String[]>> computeResults(Vertex vertex);


    /**
     * Run this method one time just before first iteration
     */
    public abstract void preStart();


}
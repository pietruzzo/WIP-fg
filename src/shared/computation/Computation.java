package shared.computation;

import akka.japi.Pair;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.AkkaMessages.StepMsg;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Datastructures shared between nodes must be threadsafe
 */
public abstract class Computation implements Serializable {

    protected ComputationParameters computationParameters;
    private List<Tuple2<String, Long>> returnVarNames;


    public ComputationParameters getComputationParameters() {
        return computationParameters;
    }

    public void setComputationParameters(ComputationParameters computationParameters) {
        this.computationParameters = computationParameters;
    }

    public void setReturnVarNames(List<Tuple2<String, Long>> returnVarNames) {
        this.returnVarNames = returnVarNames;
    }

    public List<String> returnVarNames (){
        return returnVarNames.stream().map(var -> var.f0).collect(Collectors.toList());
    }

    public List<Tuple2<String, Long>> getVarsTemporalWindow (){
        return returnVarNames;
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
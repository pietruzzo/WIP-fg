package shared.computation;

import akka.japi.Pair;
import shared.data.StepMsg;

import java.io.Serializable;
import java.util.List;

/**
 * Class variables has the partition+group scope, hence you cannot retrieve values from different workers or groups
 * Class variables must be initialized in PRESTART method
 */
public abstract class Computation implements Serializable, Cloneable {


    protected ComputationParameters computationParameters;
    private boolean haltVote;

    protected final void voteToHalt () {
        haltVote = true;
    }

    /**
     * set haltVote to false
     * @return previous haltVote
     */
    public final boolean resetHalted () {
        if (haltVote) {
            haltVote = false;
            return true;
        } else {
            return false;
        }
    }

    // region: Methods handled by framework, do not call it

    public final void setComputationParameters(ComputationParameters computationParameters) {
        this.computationParameters = computationParameters;
    }

    // endregion

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
     * It is executed 1 time per worker before superstep zero
     */
    public abstract void preStart();

    @Override
    public Computation clone() throws CloneNotSupportedException {
        return (Computation) super.clone();
    }
}
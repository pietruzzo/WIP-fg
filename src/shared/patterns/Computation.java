package shared.patterns;

import master.PatternCallback;
import shared.AkkaMessages.AckMsgComputationTerminated;
import shared.AkkaMessages.ComputeResultsMsg;
import shared.AkkaMessages.StartComputationStepMsg;

import java.io.Serializable;
import java.util.List;

public class Computation extends Pattern {

    private String computationName;
    private List<String> outputLabels;
    private List<String> parameters;
    private int stepNumber;
    private int completedSlaves;

    public Computation(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer) {
        super(trigger, variablesToBeGenerated, transportLayer);
    }

    public void setComputation (String computationName, List<String> outputLabels, List<String> parameters) {
        this.computationName = computationName;
        this.outputLabels = outputLabels;
        this.parameters = parameters;
    }


    @Override
    boolean startPatternLogic() {
        //Entry point of the computation

        stepNumber = 0;
        completedSlaves = 0;

        //Prepare computation message
        StartComputationStepMsg message = new StartComputationStepMsg(computationName, null, stepNumber, transportLayer.getCurrentTimestamp());

        //send to all slaves
        transportLayer.sendToAllSlaves(message);

        //set next state and Wait Acks
        transportLayer.setNextStateIterativeComputationState();
        transportLayer.becomeAwaitAckFromAll();

        return false;
    }

    @Override
    boolean processMessage(Serializable message) {

        if (message instanceof AckMsgComputationTerminated) {
            completedSlaves = completedSlaves + 1;
        }

        //Terminate computation and notify completion
        if (completedSlaves == this.transportLayer.getNumSlaves()) {
            computeResult();
            return true;
        }

        return false;
    }

    private void computeResult () {
        transportLayer.sendToAllSlaves(new ComputeResultsMsg(null));
    }

}

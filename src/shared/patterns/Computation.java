package shared.patterns;

import master.PatternCallback;
import shared.AkkaMessages.AckMsgComputationTerminated;
import shared.AkkaMessages.ComputeResultsMsg;
import shared.AkkaMessages.StartComputationStepMsg;

import java.io.Serializable;
import java.util.List;

public class Computation extends Pattern {

    private String computationId;
    private List<String> outputLabels;
    private List<String> parameters;
    private int stepNumber;
    private int completedSlaves;
    private boolean resultComputed;

    public Computation(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer) {
        super(trigger, variablesToBeGenerated, transportLayer);
    }

    public void setComputation (String computationName, List<String> outputLabels, List<String> parameters) {
        this.computationId = computationName;
        this.outputLabels = outputLabels;
        this.parameters = parameters;
    }


    @Override
    boolean startPatternLogic() {
        //Entry point of the computation

        stepNumber = 0;
        completedSlaves = 0;
        resultComputed = false;

        //Prepare computation message
        StartComputationStepMsg message = new StartComputationStepMsg(computationId, null, stepNumber, transportLayer.getCurrentTimestamp());

        //send to all slaves
        transportLayer.sendToAllSlaves(message);

        //set next state and Wait Acks
        transportLayer.becomeAwaitAckFromAll();

        return false;
    }

    @Override
    public boolean processMessage(Serializable message) {

        if (resultComputed) return true;

        if (message instanceof AckMsgComputationTerminated) {
            completedSlaves = completedSlaves + 1;
        }

        //Terminate computation and notify completion
        if (completedSlaves == this.transportLayer.getNumSlaves()) {
            computeResult();
            resultComputed = true;
        }

        completedSlaves = 0;
        return false;
    }

    private void computeResult () {
        transportLayer.sendToAllSlaves(new ComputeResultsMsg(null));
    }

}

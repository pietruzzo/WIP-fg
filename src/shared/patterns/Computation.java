package shared.patterns;

import master.PatternCallback;
import shared.AkkaMessages.AckMsgComputationTerminated;
import shared.AkkaMessages.ComputeResultsMsg;
import shared.AkkaMessages.StartComputationStepMsg;
import shared.PropertyHandler;
import shared.computation.ComputationParametersImpl;

import java.io.Serializable;

public class Computation extends Pattern {

    private String computationId;
    private ComputationParametersImpl parameters;
    private int stepNumber;
    private int completedSlaves;
    private boolean resultComputed;

    public Computation(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer) {
        super(trigger, variablesToBeGenerated, transportLayer);
    }

    public void setComputation (String computationName, ComputationParametersImpl parameters) {
        this.computationId = computationName;
        this.parameters = parameters;
    }


    @Override
    boolean startPatternLogic() {
        //Entry point of the computation

        stepNumber = 0;
        completedSlaves = 0;
        resultComputed = false;

        PropertyHandler.writeOnPerformanceLog("ENTER_COMPUTATION_"+ System.currentTimeMillis());

        return performStepMessage();

    }


    @Override
    public boolean processMessage(Serializable message) {

        if (resultComputed) {
            PropertyHandler.writeOnPerformanceLog("EXITING_COMPUTATION_"+ System.currentTimeMillis());
            return true;
        }

        if (message instanceof AckMsgComputationTerminated) {
            completedSlaves = completedSlaves + 1;


            //Terminate computation and notify completion
            if (completedSlaves == this.transportLayer.getNumSlaves()) {
                computeResult();
            }


        } else {
            stepNumber = stepNumber + 1;
            performStepMessage();
        }

        return false;

    }

    private void computeResult () {

        transportLayer.sendToAllSlaves(new ComputeResultsMsg(null));
        transportLayer.becomeAwaitAckFromAll();
        resultComputed = true;

    }

    private boolean performStepMessage() {

        ComputationParametersImpl params = null;
        completedSlaves = 0;

        if (stepNumber == 0) params = parameters;

        //Prepare computation message
        transportLayer.getLogger().info("Launch superstep " + stepNumber);
        StartComputationStepMsg message = new StartComputationStepMsg(computationId, null, stepNumber, transportLayer.getCurrentTimestamp(), params);

        //send to all slaves
        transportLayer.sendToAllSlaves(message);

        //set next state and Wait Acks
        transportLayer.becomeAwaitAckFromAll();

        return false;
    }

}

package shared.patterns;

import master.PatternCallback;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.AkkaMessages.AckMsgComputationTerminated;
import shared.AkkaMessages.ComputeResultsMsg;
import shared.AkkaMessages.StartComputationStepMsg;
import shared.computation.ComputationParameters;

import java.io.Serializable;
import java.util.List;

public class Computation extends Pattern {

    private String computationId;
    private List<Tuple2<String, Long>> outputLabels;
    private ComputationParameters parameters;
    private int stepNumber;
    private int completedSlaves;
    private boolean resultComputed;

    public Computation(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer) {
        super(trigger, variablesToBeGenerated, transportLayer);
    }

    public void setComputation (String computationName, List<Tuple2<String, Long>> outputLabels, ComputationParameters parameters) {
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

        return performStepMessage();

    }


    @Override
    public boolean processMessage(Serializable message) {

        if (resultComputed) return true;

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


        //Detect end of superstep and begins new one
        /*
        if (receivedSlaves == this.transportLayer.getNumSlaves()) {
            stepNumber = stepNumber + 1;
            performStepMessage();
        }
         */

    }

    private void computeResult () {

        transportLayer.sendToAllSlaves(new ComputeResultsMsg(null, outputLabels));
        transportLayer.becomeAwaitAckFromAll();
        resultComputed = true;

    }

    private boolean performStepMessage() {

        ComputationParameters params = null;
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

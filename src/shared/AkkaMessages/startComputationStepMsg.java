package shared.AkkaMessages;

import shared.computation.ComputationId;

import java.io.Serializable;

public class startComputationStepMsg implements Serializable {

    private static final long serialVersionUID = 200014L;

    private final ComputationId computationId;
    private final int stepNumber;

    public startComputationStepMsg(ComputationId computationId, int stepNumber) {
        this.computationId = computationId;
        this.stepNumber = stepNumber;
    }

    public ComputationId getComputationId() {
        return computationId;
    }
}
package shared.AkkaMessages;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class StartComputationStepMsg implements Serializable {

    private static final long serialVersionUID = 200014L;

    private final long computationId;
    private final LinkedHashMap<String, String> freeVars;
    private final int stepNumber;
    private final long timestamp;

    public StartComputationStepMsg(long computationId, LinkedHashMap<String, String> freeVars, int stepNumber, long timestamp) {
        this.computationId = computationId;
        this.freeVars = freeVars;
        this.stepNumber = stepNumber;
        this.timestamp = timestamp;
    }

    public long getComputationId() {
        return computationId;
    }

    public LinkedHashMap<String, String> getFreeVars() {
        return freeVars;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
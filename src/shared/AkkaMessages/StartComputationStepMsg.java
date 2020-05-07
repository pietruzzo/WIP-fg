package shared.AkkaMessages;

import org.jetbrains.annotations.Nullable;
import shared.computation.ComputationParametersImpl;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class StartComputationStepMsg implements Serializable {

    private static final long serialVersionUID = 200014L;

    private final String computationId;
    private final LinkedHashMap<String, String> freeVars;
    private final int stepNumber;
    private final long timestamp;
    private final ComputationParametersImpl computationParameters;


    public StartComputationStepMsg(String computationId, LinkedHashMap<String, String> freeVars, int stepNumber, long timestamp, @Nullable ComputationParametersImpl computationParameters) {
        this.computationId = computationId;
        this.freeVars = freeVars;
        this.stepNumber = stepNumber;
        this.timestamp = timestamp;
        if (computationParameters != null) {
            this.computationParameters = computationParameters;
        } else {
            this.computationParameters = new ComputationParametersImpl();
        }
    }

    public String getComputationId() {
        return computationId;
    }

    /**
     * @return null if runs over all Free Variables
     */
    public LinkedHashMap<String, String> getFreeVars() {
        return freeVars;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ComputationParametersImpl getComputationParameters() {
        return computationParameters;
    }

    @Override
    public String toString() {
        return "StartComputationStepMsg{" +
                "computationId='" + computationId + '\'' +
                ", freeVars=" + freeVars +
                ", stepNumber=" + stepNumber +
                ", timestamp=" + timestamp +
                ", computationParameters=" + computationParameters +
                '}';
    }
}
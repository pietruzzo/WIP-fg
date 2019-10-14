package shared.AkkaMessages;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ComputeResultsMsg implements Serializable {

    private static final long serialVersionUID = 200020L;

    private final long computationId;
    private final LinkedHashMap<String, String> freeVars;
    private final long timestamp;

    public ComputeResultsMsg(long computationId, LinkedHashMap<String, String> freeVars, int stepNumber, long timestamp) {
        this.computationId = computationId;
        this.freeVars = freeVars;
        this.timestamp = timestamp;
    }

    public long getComputationId() {
        return computationId;
    }

    /**
     * @return null if runs over all Free Variables
     */
    public LinkedHashMap<String, String> getFreeVars() {
        return freeVars;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
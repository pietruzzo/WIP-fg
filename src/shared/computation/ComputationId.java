package shared.computation;

import java.io.Serializable;

public class ComputationId implements Serializable {
    private static final long serialVersionUID = 200013L;

    private final long timestamp;
    private final long identifier;


    public ComputationId(long timestamp, long identifier) {
        this.timestamp = timestamp;
        this.identifier = identifier;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getIdentifier() {
        return identifier;
    }
}
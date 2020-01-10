package shared.variables;

import java.io.Serializable;

public abstract class Variable implements Serializable {

    private final String name;
    private final long persistence;
    private final long timestamp;

    public Variable(String name, long persistence, long timestamp) {
        this.name = name;
        this.persistence = persistence;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public long getPersistence() {
        return persistence;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", persistence=" + persistence +
                ", timestamp=" + timestamp +
                '}';
    }
}

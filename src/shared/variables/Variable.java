package shared.variables;

public abstract class Variable {

    private final String name;
    private final long persistence;
    private final long timestamp;

    public Variable(String name, long persistence, long timestamp) {
        this.name = name;
        this.persistence = persistence;
        this.timestamp = timestamp;
    }
}

package shared.variables;


public class VariableSimple extends Variable {

    /**
     * Valore Aggregato, solitamente sul master
     */
    private final String[] value;

    public VariableSimple(String name, long persistence, long timestamp, String[] values) {
        super(name, persistence, timestamp);
        this.value = values;
    }

    public String[] getValue() {
        return value;
    }
}

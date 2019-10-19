package shared.variables;


public class VariableSimple extends Variable {

    /**
     * Valore Aggregato, solitamente sul master
     */
    private final String valore;

    public VariableSimple(String name, long persistence, long timestamp, String valore) {
        super(name, persistence, timestamp);
        this.valore = valore;
    }

    public String getValore() {
        return valore;
    }
}

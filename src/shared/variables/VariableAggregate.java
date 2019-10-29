package shared.variables;


import akka.japi.Pair;

import java.util.ArrayList;

public class VariableAggregate extends Variable {

    private static final long serialVersionUID = 200040L;

    /**
     * Valore Aggregato, solitamente sul master
     */
    private final String[] value;

    /**
     * Set associated nodeIds in case of free variables
     */
    private ArrayList<Pair<String, String>> associatedEdges;

    public VariableAggregate(String name, long persistence, long timestamp, String[] values) {
        super(name, persistence, timestamp);
        this.value = values;
    }

    public String[] getValue() {
        return value;
    }


    public ArrayList<Pair<String, String>> getAssociatedEdges() {
        return associatedEdges;
    }

    public void setAssociatedEdges(ArrayList<Pair<String, String>> associatedEdges) {
        this.associatedEdges = associatedEdges;
    }

}

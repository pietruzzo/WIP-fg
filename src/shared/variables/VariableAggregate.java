package shared.variables;


import akka.japi.Pair;

import java.util.ArrayList;

public class VariableAggregate extends Variable {

    /**
     * Valore Aggregato, solitamente sul master
     */
    private final String[] value;

    /**
     * Set associated nodeIds in case of free variables
     */
    private ArrayList<String> associatedNodes;
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

    public void setAssociatedNodes (ArrayList<String> associatedNodes) {
        this.associatedNodes = associatedNodes;
    }

    public ArrayList<Pair<String, String>> getAssociatedEdges() {
        return associatedEdges;
    }

    public void setAssociatedEdges(ArrayList<Pair<String, String>> associatedEdges) {
        this.associatedEdges = associatedEdges;
    }

    public ArrayList<String> getAssociatedNodes () {
        return this.associatedNodes;
    }
}

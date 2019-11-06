package shared.variables;

import shared.data.MultiKeyMap;
import shared.computation.Vertex;

import java.util.ArrayList;
import java.util.Map;

public class VariablePartition extends Variable {

    private static final long serialVersionUID = 200042L;

    private final Map<String, String[]> values;
    private final MultiKeyMap<Variable> insideVariable; //Edges, aggregate or vertex
    private final MultiKeyMap<ArrayList<Vertex>> partitionNodes;

    public VariablePartition(String name, long persistence, long timestamp, Map<String, String[]> values) {
        super(name, persistence, timestamp);
        this.values = values;
        this.insideVariable = new MultiKeyMap<>((String[]) values.keySet().toArray());
        this.partitionNodes = new MultiKeyMap<>((String[]) values.keySet().toArray());
    }

    /**
     * @param compositeKey
     * @return aggregate value or null if there isn't that partition associated to that key
     * @throws ClassCastException if variable isn't an aggregate
     * @throws IllegalArgumentException if if there isn't a composite key for the specified key mapping
     *
     */
    @Deprecated
    public String[] getAggregate(Map<String, String> compositeKey) throws ClassCastException, IllegalArgumentException{
        Variable variable = insideVariable.getValue(compositeKey);
        if (variable instanceof VariableAggregate) {
            return ((VariableAggregate) variable).getSingleValue();
        } else if (variable == null){
            return null;
        } else throw new ClassCastException("Variable should be of type: " + VariableAggregate.class.toGenericString());
    }

    /**
     * @param compositeKey
     * @return aggregate value or null if there isn't that partition associated to that key
     * @throws IllegalArgumentException if if there isn't a composite key for the specified key mapping
     */
    public Variable getInsideVariable(Map<String, String> compositeKey) throws IllegalArgumentException{
        return insideVariable.getValue(compositeKey);
    }

    public MultiKeyMap<Variable> getAllInsideVariables () {
        return this.insideVariable;
    }

    public Map<String, String[]> getPartitions () {
        return values;
    }
}

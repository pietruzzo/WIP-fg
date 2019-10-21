package shared.variables;

import java.util.Map;

public class VariableVertex extends Variable {

    /**
     * Map<VertexId, Values>
     */
    private Map<String, String[]> verticesValues;

    public VariableVertex(String name, long persistence, long timestamp, Map<String, String[]> verticesValues) {
        super(name, persistence, timestamp);
        this.verticesValues = verticesValues;
    }

    public Map<String, String[]> getVerticesValues() {
        return verticesValues;
    }

    public String[] getVertexValues(String node) {
        return verticesValues.get(node);
    }
}

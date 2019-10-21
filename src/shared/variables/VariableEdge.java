package shared.variables;

import java.util.Map;

public class VariableEdge extends Variable {

    /**
     * Map<VertexName, Map<EdgeName, Values>>
     */
    private Map<String, Map<String, String[]>> edgesValues;

    public VariableEdge(String name, long persistence, long timestamp, Map<String, Map<String, String[]>> edgesValues) {
        super(name, persistence, timestamp);
        this.edgesValues = edgesValues;
    }

    public String[] getEdgeValues(String vertexName, String edgeName) {
        if (edgesValues.get(vertexName) == null) return null;
        return edgesValues.get(vertexName).get(edgeName);
    }
}

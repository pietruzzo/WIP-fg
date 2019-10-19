package shared.variables;

import java.util.Map;

public class VariableNode extends Variable {

    /**
     * Map<VertexId, Values>
     */
    private Map<String, String[]> verticesValues;

    public VariableNode(String name, long persistence, long timestamp, Map<String, String[]> verticesValues) {
        super(name, persistence, timestamp);
        this.verticesValues = verticesValues;
    }
}

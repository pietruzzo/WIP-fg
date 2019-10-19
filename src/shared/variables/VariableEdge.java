package shared.variables;

import java.util.Map;

public class VariableEdge extends Variable {

    private Map<String, Map<String, String[]>> edgesValues;

    public VariableEdge(String name, long persistence, long timestamp, Map<String, Map<String, String[]>> edgesValues) {
        super(name, persistence, timestamp);
        this.edgesValues = edgesValues;
    }
}

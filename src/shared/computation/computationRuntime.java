package shared.computation;

import shared.Vertex;
import shared.data.BoxMsg;

import java.util.Map;

public class computationRuntime {

    private final ComputationId computationId;
    private final Computation computation;
    private Map<String, Vertex> vertices; //con gli edges modificati
    private int stepNumber;
    private BoxMsg ingoingMessages;
    private BoxMsg outgoingMessages;


    public computationRuntime(ComputationId computationId, Computation computation, Map<String, Vertex> vertices) {
        this.computationId = computationId;
        this.computation = computation;
        this.vertices = vertices;
    }
}

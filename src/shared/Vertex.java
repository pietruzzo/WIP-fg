package shared;

import jdk.internal.jline.internal.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Vertex implements Serializable {
    private static final long serialVersionUID = 200000L;

    final private String vertexName;
    private List<Edge> outgoingEdges = null;

    private VertexState state;

    public Vertex(String vertexName, @Nullable VertexState vertexState) {
        this.vertexName = vertexName;
        this.state = vertexState;
        outgoingEdges = new ArrayList<>();
    }

    public String getVertexName() {
        return vertexName;
    }

    public void addEdge(String destinationVertex){
        outgoingEdges.add(new Edge(destinationVertex));
    }

    public VertexState getStateClone () {
        return state.clone();
    }

    /**
     * @param updates
     */
    public void updateState (Vertex updates){
        if (!updates.getVertexName().equals(this.vertexName)) return; //Todo: exception

        VertexState toBeAdded = updates.getStateClone();

        toBeAdded.

    }
}

class Edge implements Serializable {
    private static final long serialVersionUID = 200001L;

    private String destinationVertexName;

    public Edge(String destinationVertexName) {
        this.destinationVertexName = destinationVertexName;
    }
}
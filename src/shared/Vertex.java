package shared;

import jdk.internal.jline.internal.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Vertex implements Serializable {
    private static final long serialVersionUID = 200000L;

    final private String vertexName;
    final private List<Edge> outgoingEdges;

    final public VertexState state;

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

    }
}

class Edge implements Serializable {
    private static final long serialVersionUID = 200001L;

    private String destinationVertexName;

    public Edge(String destinationVertexName) {
        this.destinationVertexName = destinationVertexName;
    }
}
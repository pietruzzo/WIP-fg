package shared.computation;

import shared.Vertex;

import java.util.ArrayList;

/**
 * ReadOnly copy of the vertex
 */
public class VertexProxy {

    private final shared.Vertex.State state;
    private final String vertexName;
    private final ArrayList<String> edges;

    public VertexProxy(Vertex vertex, ArrayList<String> edges) {
        this.edges = edges;
        this.state = vertex.state;
        this.vertexName = vertex.getVertexName();
    }

    public String getVertexName() {
        return vertexName;
    }

    public ArrayList<String> getEdges() {
        return (ArrayList<String>) edges.clone();
    }

    public String getValue(String key){
        return state.getValue(key);
    }

    public Vertex.Versions getValues(String key, Long t1, Long t2) {
        return (Vertex.Versions) state.getValues(key, t1, t2).clone();
    }
}

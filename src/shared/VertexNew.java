package shared;

import shared.computation.Vertex;

import java.io.Serializable;
import java.util.*;


public class VertexNew implements Serializable, Vertex {

    private static final long serialVersionUID = 200000L;

    private final String nodeId;
    private final State state;
    private final Map<String, State> edges;

    public VertexNew(String nodeId, State state) {
        this.nodeId = nodeId;
        this.state = state;
        this.edges = new HashMap<>();
    }

    public String getNodeId(){
        return this.nodeId;
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized State getEdgeState(String edgeName){
        return edges.get(edgeName);
    }

    public synchronized void setLabelVartex(String labelName, String[] values) {
        this.state.remove(labelName);
        this.state.put(labelName, values);
    }
    public synchronized String[] getLabelVertex(String labelName){
        return state.get(labelName);
    }

    public synchronized String[] getEdges(){
        return (String[]) this.edges.keySet().toArray();
    }

    public synchronized void addEdge(String edgeName){
        this.edges.put(edgeName, new State());
    }

    public synchronized void addEdge(String edgeName, State edgeState){
        if (edgeName == null) addEdge(edgeName);
        else this.edges.put(edgeName, edgeState);
    }

    public synchronized void deleteEdge(String edge){
        this.edges.remove(edge);
    }

    public synchronized void deleteEdges(Collection<String> edges){
        for (String edge: edges) {
            this.edges.remove(edge);
        }
    }

    public synchronized String[] getLabelEdge (String edge, String labelName){
        return this.edges.get(edge).get(labelName);
    }

    public synchronized void setLabelEdge (String edge, String labelName, String[] values){
        this.edges.get(edge).remove(labelName);
        this.edges.get(edge).put(labelName, values);
    }




    public static class State extends HashMap<String, String[]> implements Serializable{

        private static final long serialVersionUID = 200001L;

        public State(Map<? extends String, ? extends String[]> m) {
            super(m);
        }

        public State() {
            super();
        }
    }
}




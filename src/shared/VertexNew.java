package shared;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import shared.computation.Vertex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public void setLabelVartex(String labelName, String[] values) {
        this.state.remove(labelName);
        this.state.put(labelName, values);
    }
    public String[] getLabelVertex(String labelName){
        return state.get(labelName);
    }

    public String[] getEdges(){
        return (String[]) this.edges.keySet().toArray();
    }

    public void addEdge(String edgeName){
        this.edges.put(edgeName, new State());
    }

    public void addEdge(String edgeName, State edgeState){
        if (edgeName == null) addEdge(edgeName);
        else this.edges.put(edgeName, edgeState);
    }

    public void deleteEdge(String edge){
        this.edges.remove(edge);
    }

    public String[] getLabelEdge (String edge, String labelName){
        return this.edges.get(edge).get(labelName);
    }

    public void setLabelEdge (String edge, String labelName, String[] values){
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




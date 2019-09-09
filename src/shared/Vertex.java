package shared;

import akka.japi.Pair;
import jdk.internal.jline.internal.Nullable;

import java.io.Serializable;
import java.util.*;

public class Vertex implements Serializable {
    private static final long serialVersionUID = 200000L;

    final private String vertexName;
    final private List<String> outgoingEdges;

    final public State state;

    public Vertex(String vertexName, @Nullable State state) {
        this.vertexName = vertexName;
        this.state = state;
        outgoingEdges = new ArrayList<>();
    }

    public String getVertexName() {
        return vertexName;
    }

    public void addEdge(String destinationVertex){
        outgoingEdges.add(destinationVertex);
    }

    public boolean removeEdge(String destinationVertex) {
       return outgoingEdges.remove(destinationVertex);
       //TODO: Remove associated state
    }




    public static class State implements Serializable {
        /*
            Assumption: State versions are in timestamp order
         */
        private static final long serialVersionUID = 200002L;

        static public final String DELETE_VALUE = "DELETE";

        final private HashMap<String, Versions> versionedAttributes;  //AttributeName - List of Pairs < Timestamp, Value >

        public State(){
            versionedAttributes = new HashMap<>();
        }

        public void addToState(String name, String value, Long timestamp) {
            Versions versions;
            if (versionedAttributes.containsKey(name)) {
                versions = versionedAttributes.get(name);
            } else {
                versions = new Vertex.Versions();
                versionedAttributes.put(name, versions);
            }
            versions.add(new Pair(timestamp, value));
        }

        public void removeOldStates(String name, Long timestamp) { //But keeping latest one

            Versions values = versionedAttributes.get(name);

            if (values == null) { //No entry for name
                return;
            }

            Versions toBeRemoved = new Vertex.Versions();

            for (Pair p: values) {
                if ((Long)p.second()< timestamp && p != values.get(values.size()-1)) toBeRemoved.add(p);
            }
            for (Pair rv: toBeRemoved) {
                values.remove(rv);
            }
            toBeRemoved.clear();

            //Remove entry if deleted
            if (values.isEmpty() || values.size() == 1 && values.get(0).second().equals(DELETE_VALUE)){
                versionedAttributes.remove(name);
            }
        }

        public void removeOldStates(Long timestamp){
            for (String name: versionedAttributes.keySet()) {
                removeOldStates(name, timestamp);
            }
        }

        /**
         * Get latest version
         */
        public String getValue (String key){
            Versions values = versionedAttributes.get(key);
            return values.get(values.size()-1).second();
        }

        public Versions getValues (String key, Long t1, Long t2){
            Versions values = versionedAttributes.get(key);
            if (values == null || values.isEmpty()) return new Vertex.Versions();
            return values.subVersions(t1, t2);
        }
    }

    public static class Versions extends ArrayList<Pair<Long, String>> implements Serializable {
        private static final long serialVersionUID = 200003L;

        public Versions subVersions (long t1, long t2){
            Versions result = new Vertex.Versions();
            for (Pair<Long, String> entry: this) {
                if (entry.first() > t2) return result;
                else if (entry.first() >= t1) result.add(new Pair<>(entry.first().longValue(), entry.second()));
            }
            return result;
        }
    }

}


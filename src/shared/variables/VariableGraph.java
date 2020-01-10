package shared.variables;


import shared.computation.Vertex;
import shared.data.MultiKeyMap;

import java.util.Map;

public class VariableGraph extends Variable {

    final private MultiKeyMap<Map<String, Vertex>> savedPartitions;

    public VariableGraph(String name, long persistence, long timestamp, MultiKeyMap<Map<String, Vertex>> partitions) {
        super(name, persistence, timestamp);
        this.savedPartitions = partitions;
    }

    public MultiKeyMap<Map<String, Vertex>> getSavedPartitions() {
        return savedPartitions;
    }

    @Override
    public String toString() {
        return "VariableGraph{" +
                "savedPartitions=" + savedPartitions +
                '}';
    }
}

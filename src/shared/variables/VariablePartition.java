package shared.variables;

import shared.MultiKeyMap;
import shared.computation.PartitionComputation;

import java.util.List;
import java.util.Map;

public class VariablePartition extends Variable {

    private final Map<String, String[]> values;
    private final MultiKeyMap<String[]> partitions;

    public VariablePartition(String name, long persistence, long timestamp, Map<String, String[]> values) {
        super(name, persistence, timestamp);
        this.values = values;
        this.partitions = new MultiKeyMap<>((String[]) values.keySet().toArray());
    }

    public String[] getAggregate(Map<String, String> compositeKey){
        return partitions.getValue(compositeKey);
    }

    public Map<String, String[]> getPartitions () {
        return values;
    }
}

package shared.variables;

import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.exceptions.WrongTypeRuntimeException;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class VariableEdge extends Variable {

    private static final long serialVersionUID = 200041L;

    /**
     * Map<VertexName, Map<EdgeName, Values>>
     */
    private Map<String, Map<String, Tuple>> edgesValues;

    private ArrayList<String> tupleNames;

    public VariableEdge(String name, long persistence, long timestamp, Map<String, Map<String, String[]>> edgesValues, @Nullable String tupleName) {
        super(name, persistence, timestamp);
        this.edgesValues = edgesValues.entrySet().stream().map(edge -> {
            Map<String, Tuple> collect = edge.getValue().entrySet().stream().map(value -> new Tuple2<>(value.getKey(), new Tuple1<>(value.getValue()))).collect(Collectors.toMap(tuple2 -> tuple2.f0, tuple2 -> (Tuple) tuple2.f1));
            return new Tuple2<>(edge.getKey(), collect);
        }).collect(Collectors.toMap(tuple2 -> tuple2.f0, tuple2 -> tuple2.f1));
        this.tupleNames = new ArrayList<>();
        this.tupleNames.add(tupleName);
    }

    public Map<String, Map<String, Tuple>> getEdgesValues() {
        return edgesValues;
    }

    public VariableEdge(String name, long persistence, Map<String, Map<String, Tuple>> edgesValues, long timestamp, ArrayList<String> tupleNames) {
        super(name, persistence, timestamp);
        this.edgesValues = edgesValues;
        this.tupleNames = tupleNames;
    }

    public String[] getSingleVarValue(String vertexName, String edgeName) {
        if (edgesValues.get(vertexName) == null) return null;
        Tuple value = this.edgesValues.get(vertexName).get(edgeName);
        if (! (value instanceof Tuple1) ) throw new WrongTypeRuntimeException(Tuple1.class, value.getClass());
        return ((String[])((Tuple1) value).f0);
    }

    public String[] getTupleField(String vertexName, String edgeName, String field) {
        if (edgesValues.get(vertexName) == null) return null;
        Tuple value = this.edgesValues.get(vertexName).get(edgeName);
        return value.getField(this.tupleNames.indexOf(field));
    }

    public Tuple getTuple(String vertexName, String edgeName) {
        if (edgesValues.get(vertexName) == null) return null;
        return edgesValues.get(vertexName).get(edgeName);
    }
}

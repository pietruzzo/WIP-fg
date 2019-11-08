package shared.variables;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.exceptions.WrongTypeRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class VariableVertex extends Variable {

    private static final long serialVersionUID = 200043L;

    /**
     * Map<VertexId, Values>
     */
    private Map<String, Tuple> verticesValues;
    private ArrayList<String> tupleNames;

    public VariableVertex(String name, long persistence, long timestamp, Map<String, String[]> verticesValues, String fieldName) {
        super(name, persistence, timestamp);
        this.verticesValues = new HashMap<>();
        verticesValues.entrySet().stream().map(entry -> new Tuple2<>(entry.getKey(), (Tuple)new Tuple1<>(entry.getValue())))
                .collect(Collectors.toMap(tuple-> tuple.f0, tuple-> tuple.f1));

        this.tupleNames = new ArrayList<>();
        this.tupleNames.add(fieldName);
    }

    public VariableVertex(String name, long persistence, long timestamp, Map<String, Tuple> verticesValues, String[] fieldName) {
        super(name, persistence, timestamp);
        this.verticesValues = verticesValues;
        this.tupleNames = new ArrayList<>(Arrays.asList(fieldName));
    }

    public Map<String, Tuple> getVerticesValues() {
        return verticesValues;
    }

    public String[] getVertexSingleValue(String node) {
        Tuple value = verticesValues.get(node);
        if (value == null) return null;
        if (! (value instanceof Tuple1) ) throw new WrongTypeRuntimeException(Tuple1.class, value.getClass());
        return (String[])((Tuple1) value).f0;
    }

    public String[] getTupleField(String vertexName, String field) {
        if (verticesValues.get(vertexName) == null) return null;
        Tuple value = this.verticesValues.get(vertexName);
        return value.getField(this.tupleNames.indexOf(field));
    }

    public Tuple2<ArrayList<String>, Tuple> getVertexTuple(String node) {

        return new Tuple2<>(this.tupleNames, verticesValues.get(node));
    }
}

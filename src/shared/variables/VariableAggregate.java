package shared.variables;


import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.jetbrains.annotations.Nullable;
import shared.exceptions.WrongTypeRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;

public class VariableAggregate extends Variable {

    private static final long serialVersionUID = 200040L;

    private final Tuple[] value;
    private ArrayList<String> tupleNames;


    public VariableAggregate(String name, long persistence, long timestamp, String[] values, @Nullable String tupleName) {
        super(name, persistence, timestamp);
        Tuple1<String[]> tuple = new Tuple1<>(values);
        this.value = new Tuple[]{tuple};

        this.tupleNames = new ArrayList<>();
        this.tupleNames.add(tupleName);
    }

    public VariableAggregate(String name, long persistence, long timestamp, Tuple[] values, ArrayList<String> tupleNames) {
        super(name, persistence, timestamp);
        this.value = values;
        this.tupleNames = tupleNames;
    }

    public String[] getSingleValue() {
        if (! (value instanceof Tuple1[]) && !(value.length == 1)) throw new WrongTypeRuntimeException(Tuple1[].class, value.getClass());
        return ((Tuple1<String[]>)value[0]).f0;
    }

    public String[] getFieldValue(String field) {
        if (! (value.length == 1)) throw new IndexOutOfBoundsException();
        return value[0].getField(this.tupleNames.indexOf(field));
    }


    public Tuple[] getValue() {
        return value;
    }

    public ArrayList<String> getTupleNames() {
        return tupleNames;
    }

    @Override
    public String toString() {
        return "VariableAggregate{" +
                "value=" + Arrays.toString(value) +
                ", tupleNames=" + tupleNames +
                '}';
    }
}

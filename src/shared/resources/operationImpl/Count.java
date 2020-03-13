package shared.resources.operationImpl;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import shared.streamProcessing.ExtractedStream;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Count extends CustomBinaryOperator<Tuple1<Double>> {

    @Override
    public Tuple1<Double> getIdentity() {
        return new Tuple1<>(0.0);
    }

    @Override
    public BinaryOperator<Tuple1<Double>> getBinaryOperator() {
        return (t1, t2) -> {
            t1.setField(t1.f0 + t2.f0, 0);
            return t1;
        };
    }

    @Override
    public Function<Tuple1<Double>, Tuple> extractResult() {
        return longTuple1 -> new Tuple1<>(new String[]{longTuple1.f0.toString()});
    }

    @Override
    public Tuple1<Double> apply(Tuple1<Double> o, Tuple o2) {
        o.setFields(o.f0+ Arrays.stream(super.labels.getField(o2, 0)).mapToDouble((e -> Double.parseDouble(e))).sum());
        return o;
    }

    @Override
    public ArrayList<String> getNewFieldNames(ExtractedStream.StreamType streamType) {
        ArrayList<String> fields = new ArrayList<>();
        fields.add("value");
        return fields;
    }
}

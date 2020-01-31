package shared.resources.operationImpl;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.streamProcessing.ExtractedStream;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Max extends CustomBinaryOperator<Tuple3<String[], String[], Long>> {

    @Override
    public Tuple3<String[], String[], Long> getIdentity() {
        return new Tuple3<>(new String[]{"Nan"}, new String[]{"Nan"},  Long.MIN_VALUE);
    }

    @Override
    public BinaryOperator<Tuple3<String[], String[],  Long>> getBinaryOperator() {
        return (t1, t2) -> {
            if (t1.f2 > t2.f2) return t1;
            else return t2;
        };
    }


    @Override
    public Function<Tuple3<String[],String[],  Long>, Tuple> extractResult() {
        return longTuple2 -> new Tuple3<>(longTuple2.f0, longTuple2.f1,  new String[]{String.valueOf(longTuple2.f2)});
    }


    @Override
    public Tuple3<String[], String[], Long> apply(Tuple3<String[], String[], Long> longTuple2, Tuple tuple) {
        String[] field = super.labels.getField(tuple, 0);

        Optional<String> maxOpt =
                Arrays.stream(field)
                .reduce((s1, s2) -> Long.parseLong(s1)> Long.parseLong(s2) ? s1 : s2);

        if (maxOpt.isEmpty()) return longTuple2;

        Long max = Long.parseLong(maxOpt.get());

        if (max > longTuple2.f2) {

            return new Tuple3<>(labels.getNodeId(tuple), labels.getEdgeId(tuple), max);

        }

        return longTuple2;

    }

    @Override
    public ArrayList<String> getNewFieldNames(ExtractedStream.StreamType streamType) {
        ArrayList<String> result = new ArrayList<>();
        result.add(ExtractedStream.NODELABEL);
        result.add(ExtractedStream.EDGELABEL);
        result.add("value");
        return result;
    }

}

package shared.resources.operationImpl;

import org.apache.flink.api.java.tuple.Tuple;
import shared.streamProcessing.ExtractedStream;
import shared.streamProcessing.abstractOperators.CustomFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Diff extends CustomFunction {

    /**
     * Get difference between max of first arg and max of second arg
     * if one of the two isn't defined, empty or null > delete the result tuple
     */
    @Override
    public Tuple apply(Tuple tuple) {
        ArrayList<String[]> newFields = new ArrayList<>();
        String[] first = super.labels.getField(tuple, 0);
        String[] second = super.labels.getField(tuple, 1);

        if (first == null || second == null || first.length==0 || second.length ==0) {
            return null;
        }

        Double a = Arrays.stream(first).mapToDouble(e -> Double.parseDouble(e)).max().orElse(Double.NaN);
        Double b = Arrays.stream(second).mapToDouble(e -> Double.parseDouble(e)).max().orElse(Double.NaN);

        newFields.add(new String[]{Double.toString(b-a)});
        return super.labels.generateTuple(tuple, newFields);
    }

    @Override
    public ArrayList<String> getNewFieldNames(ExtractedStream.StreamType streamType) {
        List<String> newField = new ArrayList<>();
        newField.add("value");
        return newFieldsKeepingStreamType(streamType, newField);
    }
}

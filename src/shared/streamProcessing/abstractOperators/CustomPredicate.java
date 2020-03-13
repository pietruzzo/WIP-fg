package shared.streamProcessing.abstractOperators;

import org.apache.flink.api.java.tuple.Tuple;
import shared.streamProcessing.ExtractedStream;

import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class CustomPredicate extends AbstractOperator implements Predicate<Tuple> {

    /**
     * Hide method because Predicate doesn't change tuples fields
     */
    @Override
    public final ArrayList<String> getNewFieldNames(ExtractedStream.StreamType streamType) {
        return null;
    }

}

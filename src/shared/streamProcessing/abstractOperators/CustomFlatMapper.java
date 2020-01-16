package shared.streamProcessing.abstractOperators;

import org.apache.flink.api.java.tuple.Tuple;

import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CustomFlatMapper extends AbstractOperator implements Function<Tuple, Stream<Tuple>> {
}

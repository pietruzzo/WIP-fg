package shared.streamProcessing.abstractOperators;

import org.apache.flink.api.java.tuple.Tuple;

import java.util.function.Function;

public abstract class CustomFunction extends AbstractOperator implements Function<Tuple, Tuple> {
}

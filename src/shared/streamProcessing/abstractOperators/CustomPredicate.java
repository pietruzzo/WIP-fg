package shared.streamProcessing.abstractOperators;

import org.apache.flink.api.java.tuple.Tuple;

import java.util.function.Predicate;

public abstract class CustomPredicate extends AbstractOperator implements Predicate<Tuple> {
}

package shared.streamProcessing.abstractOperators;

import org.apache.flink.api.java.tuple.Tuple;
import org.jetbrains.annotations.NotNull;
import shared.streamProcessing.SubstitutableLabels;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public abstract class CustomBinaryOperator <U> extends AbstractOperator implements BiFunction<U, Tuple, U> {

    public abstract U getIdentity();

    public abstract BinaryOperator<U> getBinaryOperator();

    public abstract Function<U, Tuple> extractResult();



}

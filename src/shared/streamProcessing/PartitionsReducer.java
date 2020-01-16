package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Tuple2<HashMap<String, String>, String> tuple
 * HashMap refers to partition
 * String refers to singleValue Aggregate
 */
public interface PartitionsReducer extends BinaryOperator<Tuple2<HashMap<String, String>, String>>, Cloneable {

    @Override
    Tuple2<HashMap<String, String>, String> apply(Tuple2<HashMap<String, String>, String> tuple, Tuple2<HashMap<String, String>, String> tuple2);

    @NotNull
    @Override
    <V> BiFunction<Tuple2<HashMap<String, String>, String>, Tuple2<HashMap<String, String>, String>, V> andThen(@NotNull Function<? super Tuple2<HashMap<String, String>, String>, ? extends V> after);


    //Need it to parallelize
    CustomBinaryOperator clone();
}

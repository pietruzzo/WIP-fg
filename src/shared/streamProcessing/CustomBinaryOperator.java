package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface CustomBinaryOperator extends BinaryOperator<Tuple>, Cloneable{

    //todo: is it a good way to implement it???

    @Override
    Tuple apply(Tuple tuple, Tuple tuple2);

    @NotNull
    @Override
    <V> BiFunction<Tuple, Tuple, V> andThen(@NotNull Function<? super Tuple, ? extends V> after);

    /**
     *
     * @param tuples one from each worker
     * @param <T>
     * @return
     */
    <T> T finalMasterStep(List<Tuple> tuples);

    //Need it to parallelize
    CustomBinaryOperator clone();
}

package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ExtractedIf {
    ExtractedIf map(Function<Tuple, Tuple> function);

    Map<Tuple, Tuple> reduce(Tuple identity, CustomBinaryOperator accumulator);

    void set(ExtractedIf extractedIf);

    ExtractedIf flatmap(Function<Tuple, Stream<Tuple>> mapper);

    ExtractedIf filter(Predicate<Tuple> filterFunction);
}

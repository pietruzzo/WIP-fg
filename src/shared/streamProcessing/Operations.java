package shared.streamProcessing;


import org.apache.flink.api.java.tuple.Tuple;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Operations {

    class Map implements Operations{
        public final Function<Tuple, Tuple> function;

        public Map(Function<Tuple, Tuple> function) {
            this.function = function;
        }
    }

    class Reduce implements Operations{
        public final Tuple identity;
        public final CustomBinaryOperator accumulator;

        public Reduce(Tuple identity, CustomBinaryOperator accumulator) {
            this.identity = identity;
            this.accumulator = accumulator;
        }
    }

    class FlatMap implements Operations{
        public final Function<Tuple, Stream<Tuple>> mapper;

        public FlatMap(Function<Tuple, Stream<Tuple>> mapper) {
            this.mapper = mapper;
        }
    }

    class Flattern implements Operations{
        public final String key;

        public Flattern(String key) {
            this.key = key;
        }
    }

    class GroupBy implements Operations{
        public final String[] groupingLabels;

        public GroupBy(String[] groupingLabels) {
            this.groupingLabels = groupingLabels;
        }
    }

    class Filter implements Operations{
        public final Predicate<Tuple> filterFunction;

        public Filter(Predicate<Tuple> filterFunction) {
            this.filterFunction = filterFunction;
        }
    }

    class Collect implements Operations{}

    class Emit implements Operations{}
}

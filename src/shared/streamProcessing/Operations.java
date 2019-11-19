package shared.streamProcessing;


import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple;
import shared.selection.SelectionSolver;

import java.util.ArrayList;
import java.util.List;
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

    class Extract implements Operations{

        public final String[] labels;
        public final boolean edges;

        public Extract(String[] labels, boolean edges) {
            this.labels = labels;
            this.edges = edges;
        }
    }

    class Reduce implements Operations{

        public final Tuple identity;
        public final CustomBinaryOperator accumulator;
        public final Long transaction_Id;
        public final ArrayList<String> fieldNames;

        public Reduce(Tuple identity, CustomBinaryOperator accumulator, @Nullable Long transaction_id, List<String> fieldsNames) {
            this.identity = identity;
            this.accumulator = accumulator;
            this.transaction_Id = transaction_id;
            this.fieldNames = new ArrayList<>(fieldsNames);
        }
    }

    class FlatMap implements Operations{

        public final Function<Tuple, Stream<Tuple>> mapper;

        public FlatMap(Function<Tuple, Stream<Tuple>> mapper) {
            this.mapper = mapper;
        }
    }


    class GroupBy implements Operations{

        public final String[] groupingLabels;

        public GroupBy(String[] groupingLabels) {
            this.groupingLabels = groupingLabels;
        }
    }

    class Merge implements Operations{

        public final String[] groupingLabels;

        public Merge(String[] groupingLabels) {
            this.groupingLabels = groupingLabels;
        }
    }

    class StreamVariable implements Operations{

        public final String VariableName;
        public final String timeAgo;
        public final SelectionSolver.Operation.WindowType wType;

        public StreamVariable(String variableName, String timeAgo, SelectionSolver.Operation.WindowType wType) {
            VariableName = variableName;
            this.timeAgo = timeAgo;
            this.wType = wType;
        }
    }

    class Filter implements Operations{

        public final Predicate<Tuple> filterFunction;

        public Filter(Predicate<Tuple> filterFunction) {
            this.filterFunction = filterFunction;
        }
    }

    class Collect implements Operations{}

    class Emit implements Operations{

        public final String variableName;
        public final long persistence;
        public final Long transaction_id;

        public Emit(String variableName, long persistence, @Nullable Long transaction_id) {
            this.variableName = variableName;
            this.persistence = persistence;
            this.transaction_id = transaction_id;
        }
    }
}

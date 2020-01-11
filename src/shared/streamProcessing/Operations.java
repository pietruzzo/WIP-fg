package shared.streamProcessing;


import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import org.apache.flink.api.java.tuple.Tuple;
import shared.selection.SelectionSolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Operations extends Serializable {

    class Map implements Operations{

        public final Function<Tuple, Tuple> function;

        public Map(Function<Tuple, Tuple> function) {
            this.function = function;
        }

        @Override
        public String toString() {
            return "Map{" +
                    "function=" + function +
                    '}';
        }
    }

    class Extract implements Operations{

        public final String[] labels;
        public final boolean edges;

        public Extract(String[] labels, boolean edges) {
            this.labels = labels;
            this.edges = edges;
        }

        @Override
        public String toString() {
            return "Extract{" +
                    "labels=" + Arrays.toString(labels) +
                    ", edges=" + edges +
                    '}';
        }
    }

    class Reduce implements Operations{

        public final Tuple identity;
        public final CustomBinaryOperator accumulator;
        public final Long transaction_Id;
        public final ArrayList<String> fieldNames;

        public Reduce(Tuple identity, CustomBinaryOperator accumulator, @Nullable Long transaction_id, String[] fieldsNames) {
            this.identity = identity;
            this.accumulator = accumulator;
            this.transaction_Id = transaction_id;
            this.fieldNames = new ArrayList<>(Arrays.asList(fieldsNames));
        }

        @Override
        public String toString() {
            return "Reduce{" +
                    "identity=" + identity +
                    ", accumulator=" + accumulator +
                    ", transaction_Id=" + transaction_Id +
                    ", fieldNames=" + fieldNames +
                    '}';
        }
    }

    class FlatMap implements Operations{

        public final Function<Tuple, Stream<Tuple>> mapper;

        public FlatMap(Function<Tuple, Stream<Tuple>> mapper) {
            this.mapper = mapper;
        }

        @Override
        public String toString() {
            return "FlatMap{" +
                    "mapper=" + mapper +
                    '}';
        }
    }


    class GroupBy implements Operations{

        public final String[] groupingLabels;

        public GroupBy(String[] groupingLabels) {
            this.groupingLabels = groupingLabels;
        }

        @Override
        public String toString() {
            return "GroupBy{" +
                    "groupingLabels=" + Arrays.toString(groupingLabels) +
                    '}';
        }
    }

    class Merge implements Operations{

        public final String[] groupingLabels;

        public Merge(String[] groupingLabels) {
            this.groupingLabels = groupingLabels;
        }

        @Override
        public String toString() {
            return "Merge{" +
                    "groupingLabels=" + Arrays.toString(groupingLabels) +
                    '}';
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

        @Override
        public String toString() {
            return "StreamVariable{" +
                    "VariableName='" + VariableName + '\'' +
                    ", timeAgo='" + timeAgo + '\'' +
                    ", wType=" + wType +
                    '}';
        }
    }

    class Filter implements Operations{

        public final Predicate<Tuple> filterFunction;

        public Filter(Predicate<Tuple> filterFunction) {
            this.filterFunction = filterFunction;
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "filterFunction=" + filterFunction +
                    '}';
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

        @Override
        public String toString() {
            return "Emit{" +
                    "variableName='" + variableName + '\'' +
                    ", persistence=" + persistence +
                    ", transaction_id=" + transaction_id +
                    '}';
        }
    }

    class Evaluate implements Operations{

        public final SelectionSolver.Operation.Operator operator;
        public final Long transaction_id;
        public final String value;
        public final String fireEvent;

        public Evaluate(SelectionSolver.Operation.Operator operator, Long transaction_id, String value, String fireEvent) {
            this.operator = operator;
            this.transaction_id = transaction_id;
            this.value = value;
            this.fireEvent = fireEvent;
        }

        @Override
        public String toString() {
            return "Evaluate{" +
                    "operator=" + operator +
                    ", transaction_id=" + transaction_id +
                    ", value='" + value + '\'' +
                    ", fireEvent='" + fireEvent + '\'' +
                    '}';
        }
    }

}

package shared.streamProcessing;


import org.jetbrains.annotations.Nullable;
import shared.selection.SelectionSolver;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.streamProcessing.abstractOperators.CustomFlatMapper;
import shared.streamProcessing.abstractOperators.CustomFunction;
import shared.streamProcessing.abstractOperators.CustomPredicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public interface Operations extends Serializable {

    class Map implements Operations{

        public final CustomFunction function;
        public final ArrayList<String> fieldNames;

        public Map(CustomFunction function, String[] fieldNames) {
            this.function = function;
            this.fieldNames = new ArrayList<>(Arrays.asList(fieldNames));
        }

        @Override
        public String toString() {
            return "Map{" +
                    "function=" + function +
                    ", fieldNames=" + fieldNames +
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

        public final CustomBinaryOperator accumulator;
        public final int transaction_id;
        public final ArrayList<String> fieldNames;

        public Reduce(CustomBinaryOperator accumulator, int transaction_id, String[] fieldsNames) {
            this.accumulator = accumulator;
            this.transaction_id = transaction_id;
            this.fieldNames = new ArrayList<>(Arrays.asList(fieldsNames));
        }

        public int getTransaction_id() {
            return transaction_id;
        }

        @Override
        public String toString() {
            return "Reduce{" +
                    ", accumulator=" + accumulator +
                    ", transaction_Id=" + transaction_id +
                    ", fieldNames=" + fieldNames +
                    '}';
        }
    }

    class FlatMap implements Operations{

        public final CustomFlatMapper mapper;
        public final ArrayList<String> fieldNames;

        public FlatMap( CustomFlatMapper mapper, String[] fieldNames) {
            this.mapper = mapper;
            this.fieldNames = new ArrayList<>(Arrays.asList(fieldNames));
        }

        @Override
        public String toString() {
            return "FlatMap{" +
                    "mapper=" + mapper +
                    ", fieldNames=" + fieldNames +
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
        public final long timeAgo;
        public final SelectionSolver.Operation.WindowType wType;

        public StreamVariable(String variableName, long timeAgo, SelectionSolver.Operation.WindowType wType) {
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

        public final CustomPredicate filterFunction;
        public final ArrayList<String> fieldNames;

        public Filter(CustomPredicate filterFunction, String[] fieldNames) {
            this.filterFunction = filterFunction;
            this.fieldNames = new ArrayList<>(Arrays.asList(fieldNames));
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "filterFunction=" + filterFunction +
                    ", fieldNames=" + fieldNames +
                    '}';
        }
    }

    class Collect implements Operations{}

    class Emit implements Operations{

        public final String variableName;
        public final long persistence;
        public final int transaction_id;

        public Emit(String variableName, long persistence, int transaction_id) {
            this.variableName = variableName;
            this.persistence = persistence;
            this.transaction_id = transaction_id;
        }

        public int getTransaction_id() {
            return transaction_id;
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
        public final int transaction_id;
        public final String value;
        public final String fireEvent;

        public Evaluate(SelectionSolver.Operation.Operator operator, int transaction_id, String value, String fireEvent) {
            this.operator = operator;
            this.transaction_id = transaction_id;
            this.value = value;
            this.fireEvent = fireEvent;
        }

        public int getTransaction_id() {
            return transaction_id;
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

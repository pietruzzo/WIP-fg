package shared.selection;

import akka.japi.Pair;
import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple3;

import java.sql.PseudoColumnUsage;
import java.util.ArrayList;
import java.util.List;

public class SelectionSolver {

    List<Element> elements;


    public interface Element {}

    public static class Solved implements Element{
        public final boolean solution;

        public Solved(boolean solution) {
            this.solution = solution;
        }
    }

    public static class BinaryBoolean implements Element{
        public enum OperatorType{AND, OR};
    }
    public static class UnaryBoolean implements Element{
        public enum OperatorType{NOT};
    }

    public static class Operation implements Element {
        public enum Operator{EQUAL, MAJOR, MINOR, MAJOREQUAL, MIMOREQUAL};
        public enum Type {VALUE, VARIABLE, LABEL};
        public enum WindowType {WITHIN, AGO, EVERYWITHIN};

        private Operator operator;
        /**
         * Values can be multiple in case of time windows, thus with 'variable' type set, otherwise it should be single element list
         */
        private Pair<String[], String[]> values;
        private Pair<Type, Type> type;

        /**
         * null if window is not defined, formt "10s"
         */
        public final Pair<String, String> withinTimeUnits;

        /**
         * null if window is not defined
         */
        public final Pair<WindowType, WindowType> windowType;

        public Operation(Operator operator, Pair<String, String> values, Pair<Type, Type> type, @Nullable Pair<String, String> within, @Nullable Pair<WindowType, WindowType> windowType) {
            this.operator = operator;
            this.values = new Pair<>(new String[1], new String[1]);
            this.values.first()[0] = values.first();
            this.values.second()[0] = values.second();
            this.type = type;
            if (within == null) this.withinTimeUnits = new Pair<>(null, null);
            else this.withinTimeUnits = within;
            if (windowType == null) this.windowType = new Pair<>(null, null);
            else this.windowType = windowType;
        }

        /**
         * @return variable names and related WithinTimeUnit for variable in operation or empty list if no variable types
         * WithinTimeUnit can be null
         */
        public List<Tuple3<String, String, WindowType>> getVariableList () {
            ArrayList<Tuple3<String, String, WindowType>> result = new ArrayList<>();
            if (type.first() == Type.VARIABLE) result.add(new Tuple3<>(values.first()[0], withinTimeUnits.first(), windowType.first()));
            if (type.second() == Type.VARIABLE) result.add(new Tuple3<>(values.second()[0], withinTimeUnits.second(), windowType.second()));
            return result;
        }

        /**
         * @return label names or empty list
         */
        public List<String> getLabelList () {
            ArrayList<String> result = new ArrayList<>();
            if (type.first() == Type.LABEL) result.add(values.first()[0]);
            if (type.second() == Type.LABEL) result.add(values.second()[0]);
            return result;
        }

        public void substituteValue(String name, List<String> values) {
            if (this.values.first()[0].equals(name)) this.values = new Pair(values, this.values.second());
            else if (this.values.second()[0].equals(name)) this.values = new Pair(this.values.first(), values);
            else System.err.println("Value substitution invoked but not effective");
        }
        public boolean solve () {
            if (!(type.first() == Type.VALUE && type.second() == Type.VALUE)) throw new IllegalStateException("Operation can be solved on values only");

            if ((windowType.first() == null ||  windowType.first() == WindowType.AGO) && (windowType.second() == null ||  windowType.second() == WindowType.AGO)){
                return executeOperation(this.values.first()[0], this.values.second()[0]);
            } else if ((windowType.first() == null ||  windowType.first() == WindowType.AGO) && windowType.second() == WindowType.WITHIN){
                return againstWithin(values.first()[0], values.second());
            } else if ((windowType.second() == null ||  windowType.second() == WindowType.AGO) && windowType.first() == WindowType.WITHIN){
                return againstWithin(values.second()[0], values.first());
            } else if ((windowType.first() == null ||  windowType.first() == WindowType.AGO) && windowType.second() == WindowType.EVERYWITHIN){
                return againstEveryWithin(values.first()[0], values.second());
            } else if ((windowType.second() == null ||  windowType.second() == WindowType.AGO) && windowType.first() == WindowType.EVERYWITHIN){
                return againstEveryWithin(values.second()[0], values.first());
            } else if (windowType.first() == WindowType.WITHIN && windowType.second() == WindowType.WITHIN) {
                return withinAgainstWithin(values.first(), values.second());
            } else if (windowType.first() == WindowType.WITHIN && windowType.second() == WindowType.EVERYWITHIN) {
                return withinAgainstEveryWithin(values.first(), values.second());
            } else if (windowType.first() == WindowType.EVERYWITHIN && windowType.second() == WindowType.WITHIN) {
                return withinAgainstEveryWithin(values.second(), values.first());
            } else if (windowType.first() == WindowType.EVERYWITHIN && windowType.second() == WindowType.EVERYWITHIN) {
                return everyWithinAgainstEveryWithin(values.first(), values.second());
            } else {
                System.err.println("ForbiddenState - Error");
                System.out.println("Operation.solve");
                return false;
            }
        }

        /**
         *
         * @param everyWithinValues1
         * @param everyWithinValues2
         * @return true if every pair of element satisfies condition
         */
        private boolean everyWithinAgainstEveryWithin (String[] everyWithinValues1, String[] everyWithinValues2){
            for (String current: everyWithinValues1) {
                if (!againstEveryWithin(current, everyWithinValues2)) return false;
            }
            return true;
        }
        /**
         *
         * @param withinValues
         * @param everyWithinValues
         * @return true if at least one element from withinValue match all element of second argument
         */
        private boolean withinAgainstEveryWithin (String[] withinValues, String[] everyWithinValues){
            for (String current: withinValues) {
                if (againstEveryWithin(current, everyWithinValues)) return true;
            }
            return false;
        }
        /**
         *
         * @param values1
         * @param values2
         * @return true if at least 1 element from values1 match an element from values2
         */
        private boolean withinAgainstWithin (String[] values1, String[] values2){
            for (String current: values1) {
                    if (againstWithin(current, values2)) return true;
            }
            return false;
        }
        /**
         *
         * @param value
         * @param values
         * @return true if there is at least a match of value against values
         */
        private boolean againstWithin (String value, String[] values){
            for (String current: values) {
                if (executeOperation(value, current)) return true;
            }
            return false;
        }

        /**
         *
         * @param value
         * @param values
         * @return true if it match everytime
         */
        private boolean againstEveryWithin (String value, String[] values){
            for (String current: values) {
                if (!executeOperation(value, current)) return false;
            }
            return true;
        }

        /**
         * Comparison is lexicographical or after a conversion (if both terms are convertible to long)
         * @param value1 @NotNull
         * @param value2 @NotNull
         * @return
         */
        private boolean executeOperation (String value1, String value2){
            Double converted1 = null, converted2 = null;
            boolean converted = false;
            if (operator == null) throw new IllegalArgumentException("Operator must be defined");
            if (operator == Operator.EQUAL) return value1.equals(value2);
            else { //Try to parse as double
                try{
                    converted1 = Double.parseDouble(value1);
                    converted2 = Double.parseDouble(value2);
                    converted = true;
                } catch (Exception e){
                    System.out.println("Parsing as double Failed: " + value1 + " " + value2);
                }

                if (converted) {
                    switch (operator) {
                        case MAJOR:
                            return converted1 > converted2;
                        case MINOR:
                            return converted1 < converted2;
                        case MAJOREQUAL:
                            return converted1 >= converted2;
                        case MIMOREQUAL:
                            return converted1 <= converted2;
                    }
                }
                else {
                    int result = value1.compareTo(value2);
                    if (result > 0 && operator == Operator.MAJOR) return true;
                    if (result >= 0 && operator == Operator.MAJOREQUAL) return true;
                    if (result < 0 && operator == Operator.MINOR) return true;
                    if (result <= 0 && operator == Operator.MIMOREQUAL) return true;
                }
                return false;
            }
        }
    }

}

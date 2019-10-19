package shared.selection;

import akka.japi.Pair;
import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import shared.VertexNew;
import shared.computation.Vertex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * At parse time no labels nor variables can be substituted
 * before launching the selection on the graph:
 *  - Substitute aggregates and fixed parameters variables
 *  - For each node substitute variable and labels and solve
 */
public class SelectionSolver implements Cloneable{

    /**
     * Edge property is _edgeName_property
     */



    private LinkedList<Element> elements;

    public SelectionSolver() {
        this.elements = new LinkedList<>();
    }

    public void addElementBoolean (Boolean bool) {
        this.elements.add(new Solved(bool));
    }

    public void addElementEdgeToken () {
        this.elements.add(new EdgeToken());
    }

    public void addElementUnaryOp (UnaryBoolean.OperatorType operatorType) {
        this.elements.add(new UnaryBoolean(operatorType));
    }

    public void addElementBinaryOp (BinaryBoolean.OperatorType operatorType) {
        this.elements.add(new BinaryBoolean(operatorType));
    }

    public void addElementOperation (SelectionSolver.Operation.Operator operator,
                                     Pair<String[], String[]> values,
                                     Pair<SelectionSolver.Operation.Type, SelectionSolver.Operation.Type> type,
                                     @Nullable Pair<String, String> within,
                                     @Nullable Pair<SelectionSolver.Operation.WindowType, SelectionSolver.Operation.WindowType> windowType) {
        this.elements.add(new Operation(operator, values, type, within, windowType));
    }


    private void addElement(Element element){
        this.elements.add(element);
    }

    /**
     * Strategy: Before substituting all labes/values, focus on part before EDGE token, then select edges
     */
    boolean solveVertex (VertexNew vertex) {
        //TODO Select on vertex
        //Substitute labels up to EDGE Token
        //solve up to EDGE
        //pop and return Solution from elements
        return false;

    }
    boolean solveEdges (VertexNew vertex) {
        //TODO select on edges
        return false;
    }

    /**
     * Get the list of the variables that needs to be substituted
     * vertex = true : getVariables on vertex selection part
     * edges = true : get variables on edge selection part
     * @return list of name - timewindow, windowtype
     */
    public List<Tuple3<String, String, Operation.WindowType>> getVariables (boolean vertex, boolean edges){

        List<Tuple3<String, String, Operation.WindowType>> result = new ArrayList<>();
        Element element = null;
        Iterator<Element> elementIterator = elements.iterator();
        while(vertex && elementIterator.hasNext() && !((element = elementIterator.next()) instanceof EdgeToken)) {
                if (element instanceof Operation) {
                    result.addAll(((Operation) element).getVariableList());
                }
        } while (edges && !(element instanceof EdgeToken) && elementIterator.hasNext()){
            element = elementIterator.next();
        } while (edges && elementIterator.hasNext()){
            if (element instanceof Operation) {
                result.addAll(((Operation) element).getVariableList());
            }
        }

        return result;
    }

    /**
     * Get the list of labels that needs to be substituted
     * vertex = true : getVariables on vertex selection part
     * edges = true : get variables on edge selection part
     */
    List<String> getLabels (boolean edges){
        List<String> result =  new ArrayList<>();

        for (Element element: elements) {
            if (element instanceof Operation){
                result.addAll(((Operation) element).getLabelList());
            }
        }
        return result;
    }

    /**
     * Substitute labels and variables with values
     * @param values list of name - values
     */
    void substituteLabels (List<Pair<String, String[]>> values) {
        for (Element element: elements) {
            if (element instanceof Operation){
                for (Pair<String, String[]> value: values) {
                    ((Operation) element).substituteLabel(value.first(), value.second());
                }
            }
        }
    }

    /**
     * Substitute labels and variables with values
     * @param values list of name - windowTime, windowType, Values
     */
    public void substituteVariables (List<Tuple4<String, String, Operation.WindowType, String[]>> values) {
        for (Element element: elements) {
            if (element instanceof Operation){
                for (Tuple4<String, String, Operation.WindowType, String[]> value: values) {
                    ((Operation) element).substituteVariable(value);
                }
            }
        }
    }

    @Override
    public SelectionSolver clone() {
        SelectionSolver selectionSolver = new SelectionSolver();
        for (Element e: this.elements) {
            selectionSolver.addElement(e.clone());
        }
        return selectionSolver;
    }

    private interface Element extends Serializable, Cloneable {
        Element clone();
    }

    private static class Solved implements Element{
        final boolean solution;

        Solved(boolean solution) {
            this.solution = solution;
        }

        @Override
        public Solved clone() {
            return new Solved(solution);
        }
    }

    private static class EdgeToken implements Element {
        @Override
        public EdgeToken clone() {
            return new EdgeToken();
        }
    }


    public static class BinaryBoolean implements Element{
        public enum OperatorType{AND, OR};

        private final OperatorType operatorType;

        BinaryBoolean(OperatorType operatorType) {
            this.operatorType = operatorType;
        }

        public boolean solve(Solved first, Solved second){
            switch (operatorType){
                case AND:
                    return first.solution && second.solution;
                case OR:
                    return first.solution || second.solution;
                default:
                    System.out.println("first = " + first + ", second = " + second);
                    System.err.println("not feasible case, returning false");
                    return false;
            }
        }

        @Override
        public BinaryBoolean clone() {
            return new BinaryBoolean(operatorType);
        }
    }

    public static class UnaryBoolean implements Element{
        public enum OperatorType{NOT};

        private final OperatorType operatorType;

        UnaryBoolean(OperatorType operatorType) {
            this.operatorType = operatorType;
        }

        boolean solve (Solved argument) {
            return !argument.solution;
        }

        @Override
        public UnaryBoolean clone() {
            return new UnaryBoolean(this.operatorType);
        }
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
        private final Pair<String, String> withinTimeUnits;

        /**
         * null if window is not defined
         */
        private final Pair<WindowType, WindowType> windowType;

        Operation(Operator operator, Pair<String[], String[]> values, Pair<Type, Type> type, @Nullable Pair<String, String> within, @Nullable Pair<WindowType, WindowType> windowType) {
            this.operator = operator;
            this.values = values;
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
        List<Tuple3<String, String, WindowType>> getVariableList() {
            ArrayList<Tuple3<String, String, WindowType>> result = new ArrayList<>();
            if (type.first() == Type.VARIABLE) result.add(new Tuple3<>(values.first()[0], withinTimeUnits.first(), windowType.first()));
            if (type.second() == Type.VARIABLE) result.add(new Tuple3<>(values.second()[0], withinTimeUnits.second(), windowType.second()));
            return result;
        }

        /**
         * @return label names or empty list
         */
        List<String> getLabelList() {
            ArrayList<String> result = new ArrayList<>();
            if (type.first() == Type.LABEL) result.add(values.first()[0]);
            if (type.second() == Type.LABEL) result.add(values.second()[0]);
            return result;
        }

        void substituteLabel(String name, String[] values) {
            if (this.type.first()== Type.LABEL && this.values.first()[0].equals(name)) {
                this.values = new Pair<>(values, this.values.second());
                this.type= new Pair<>(Type.VALUE, this.type.second());
            }
            if (this.type.second()== Type.LABEL && this.values.second()[0].equals(name)) {
                this.values = new Pair<>(this.values.first(), values);
                this.type = new Pair<>(type.first(), Type.VALUE);
            }

        }

        void substituteVariable(Tuple4<String, String, Operation.WindowType, String[]> values) {
            if (this.type.first()== Type.VARIABLE && this.withinTimeUnits.first().equals(values.f1) && this.windowType.first().equals(values.f2) && this.values.first()[0].equals(values.f0)) {
                this.values = new Pair<>(values.f3, this.values.second());
                this.type= new Pair<>(Type.VALUE, this.type.second());
            }
            if (this.type.second()== Type.VARIABLE && this.withinTimeUnits.second().equals(values.f1) && this.windowType.second().equals(values.f2) && this.values.second()[0].equals(values.f0)) {
                this.values = new Pair<>(this.values.first(), values.f3);
                this.type = new Pair<>(type.first(), Type.VALUE);
            }

        }

        /**
         * TODO: Make it handle multivalue reusing code!
         * @return
         */
        boolean solve () {
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

        @Override
        public Operation clone() {
            return new Operation(this.operator,
                    new Pair<>(this.values.first().clone(), this.values.second().clone()),
                    new Pair<>(this.type.first(), this.type.second()),
                    new Pair<>(this.withinTimeUnits.first(), this.withinTimeUnits.second()),
                    new Pair<>(this.windowType.first(), this.windowType.second())
                    );
        }
    }

}

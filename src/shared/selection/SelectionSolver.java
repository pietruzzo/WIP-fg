package shared.selection;

import akka.japi.Pair;
import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import shared.VertexNew;
import shared.computation.Vertex;
import shared.exceptions.WrongTypeRuntimeException;
import shared.variables.solver.VariableSolver;

import java.io.Serializable;
import java.util.*;

/**
 * At parse time no labels nor variables can be substituted
 * before launching the selection on the graph:
 *  - Substitute aggregates and fixed parameters variables
 *  - For each node substitute variable and labels and solve
 */
public class SelectionSolver implements Cloneable, Selection{



    private ArrayList<Element> elements;

    private Map<String, String> partition;

    public SelectionSolver() {
        this.elements = new ArrayList<>();
        partition = null;
    }

    public Map<String, String> getPartition() {
        return partition;
    }

    public void setPartition(Map<String, String> partition) {
        this.partition = partition;
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
        String[][] val1 = new String[1][1];
        String[][] val2 = new String[1][1];
        val1[0] = values.first();
        val2[0] = values.second();
        this.elements.add(new Operation(operator, new Pair<>(val1, val2), type, within, windowType));
    }


    private void addElement(Element element){
        this.elements.add(element);
    }

    void solveAggregates (VariableSolver variableSolver) {

        //Try with all variable to find an aggregate
        List<Tuple4<String, String, Operation.WindowType, String[][]>> varToBeSubstituted = new ArrayList<>();
        List<Tuple3<String, String, Operation.WindowType>> variables = this.getVariables(true, false);

        for (Tuple3<String, String, Operation.WindowType> variable: variables) {
            try {
                if (variable.f1 == null && variable.f2 == null) {
                    String[] variableValues = variableSolver.getAggregate(variable.f0, partition);
                    varToBeSubstituted.add(new Tuple4<>(variable.f0, variable.f1, variable.f2, new String[][]{variableValues}));
                } else if (variable.f1 != null && variable.f2 != null) {
                    List<String[]> variableValues = variableSolver.getAggregate(variable.f0, partition, variable.f1, variable.f2);
                    varToBeSubstituted.add(new Tuple4<>(variable.f0, variable.f1, variable.f2, (String[][]) variableValues.toArray()));
                } else
                    throw new NullPointerException("for variable " + variable.f0 + " windowTime (" + variable.f1 + "), windowType (" + variable.f1 + ") must be both null or ot null)");
            } catch (WrongTypeRuntimeException e) {}
        }
        this.substituteVariables(varToBeSubstituted);
    }
    /**
     * Strategy: Before substituting all labes/values, focus on part before EDGE token, then select edges
     * @return null if vertex is not selected
     * @return Vertex with selected edges
     */
    VertexNew solveVertex (VertexNew vertex, VariableSolver variableSolver) {
        //Select Vertex

        //0-Basecases
        if  (elements.isEmpty()) return  vertex;
        //1-Get vertex variables and substitute them

        List<Tuple4<String, String, Operation.WindowType, String[][]>> varToBeSubstituted = new ArrayList<>();
        List<Tuple3<String, String, Operation.WindowType>> variables = this.getVariables(true, false);

        for (Tuple3<String, String, Operation.WindowType> variable: variables) {
            if (variable.f1 == null && variable.f2 == null) {
                String[] variableValues = variableSolver.getVertexVariable(variable.f0, partition, vertex.getNodeId());
                varToBeSubstituted.add(new Tuple4<>(variable.f0, variable.f1, variable.f2, new String[][]{variableValues}));
            } else if (variable.f1 != null && variable.f2 != null) {
                List<String[]> variableValues = variableSolver.getVertexVariable(variable.f0, partition, vertex.getNodeId(), variable.f1, variable.f2);
                varToBeSubstituted.add(new Tuple4<>(variable.f0, variable.f1, variable.f2, (String[][])variableValues.toArray()));
            } else throw new NullPointerException("for vertex " + vertex.getNodeId() + ", for variable " + variable.f0 + " windowTime (" + variable.f1 + "), windowType (" + variable.f1 + ") must be both null or ot null)");
        }
        this.substituteVariables(varToBeSubstituted);

        //2-Substitute labels with values

        List<String> labelList = this.getLabels(true, false);
        List<Pair<String, String[]>> labToBeSubstituted = new ArrayList<>();
        for (String label: labelList) {
            String[] labelValues = vertex.getLabelVertex(label);
            labToBeSubstituted.add(new Pair<>(label, labelValues));
        }
        this.substituteLabels(labToBeSubstituted);

        //3-perform selection up to EDGE / END

        boolean pointEdgeToken = false;
        int i;
        for (i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof Operation)
                elements.set(i,  new Solved(((Operation) elements.get(i)).solve()));
            else if (elements.get(i) instanceof UnaryBoolean)
                elements.set(i , new Solved(((UnaryBoolean) elements.get(i)).solve((Solved)elements.get(i-1))));
            else if (elements.get(i) instanceof BinaryBoolean)
                elements.set(i, new Solved(((BinaryBoolean) elements.get(i)).solve((Solved)elements.get(i-1), (Solved)elements.get(i-2))));
            else if (elements.get(i) instanceof EdgeToken){
                pointEdgeToken = true;
                break;
            }
        }

        if (i >= 1 && pointEdgeToken /*exclude EDGETOKEN at first position*/ || !((Solved)elements.get(i-1)).solution /*node not selected*/) return null;
        if (!pointEdgeToken) {
            //No selection on edges, just return
            if (!((Solved)elements.get(i-1)).solution) return null;
            else return vertex;
        }

        elements = (ArrayList<Element>)elements.subList(i, elements.size());

        if (elements.size() == 1){
            //Selection on edge is empty -> return vertex
            return vertex;
        }
        //Select Edges
        //4-clone this for each edge
        //5-call method solveEdge on ech edge
        //6-collect selected edges
        VertexNew vertexResult = new VertexNew(vertex.getNodeId(), vertex.getState());
        for (String edge: vertex.getEdges()) {
            boolean selected = this.clone().selectEdge(vertex, variableSolver, edge);
            if (selected)
                vertexResult.addEdge(edge, vertex.getEdgeState(edge));
        }

        //7-return Vertex with selected edges

        return vertexResult;
    }

    /**
     *
     * @param edgeName
     * @return true id edge is selected, otherwise false
     */
    private boolean selectEdge (VertexNew vertex, VariableSolver variableSolver, String edgeName) {
        //Substitute Variables
        List<Tuple4<String, String, Operation.WindowType, String[][]>> varToBeSubstituted = new ArrayList<>();
        List<Tuple3<String, String, Operation.WindowType>> variables = this.getVariables(false, true);

        for (Tuple3<String, String, Operation.WindowType> variable: variables) {
            if (variable.f1 == null && variable.f2 == null) {
                String[] variableValues = variableSolver.getEdgeVariable(variable.f0,partition, vertex.getNodeId(), edgeName);
                varToBeSubstituted.add(new Tuple4<>(variable.f0, variable.f1, variable.f2, new String[][]{variableValues}));
            } else if (variable.f1 != null && variable.f2 != null) {
                List<String[]> variableValues = variableSolver.getEdgeVariable(variable.f0, partition, vertex.getNodeId(), edgeName, variable.f1, variable.f2);
                varToBeSubstituted.add(new Tuple4<>(variable.f0, variable.f1, variable.f2, (String[][])variableValues.toArray()));
            } else throw new NullPointerException("for vertex " + vertex.getNodeId() + ", for variable " + variable.f0 + " windowTime (" + variable.f1 + "), windowType (" + variable.f1 + ") must be both null or ot null)");
        }
        this.substituteVariables(varToBeSubstituted);

        //Substitute labels

        List<String> labelList = this.getLabels(false, true);
        List<Pair<String, String[]>> labToBeSubstituted = new ArrayList<>();
        for (String label: labelList) {
            String[] labelValues = vertex.getLabelEdge(label, edgeName);
            labToBeSubstituted.add(new Pair<>(label, labelValues));
        }
        this.substituteLabels(labToBeSubstituted);

        //Perform operations

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof Operation)
                elements.set(i,  new Solved(((Operation) elements.get(i)).solve()));
            else if (elements.get(i) instanceof UnaryBoolean)
                elements.set(i , new Solved(((UnaryBoolean) elements.get(i)).solve((Solved)elements.get(i-1))));
            else if (elements.get(i) instanceof BinaryBoolean)
                elements.set(i, new Solved(((BinaryBoolean) elements.get(i)).solve((Solved)elements.get(i-1), (Solved)elements.get(i-2))));
        }

        //Return true or false
        if(((Solved)elements.get(elements.size()-1)).solution) return true;
        else return false;

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
    List<String> getLabels (boolean vertex, boolean edge){
        List<String> result =  new ArrayList<>();
        boolean beforeEDGEToken = true;

        for (Element element: elements) {
            if ( ((vertex && beforeEDGEToken) || (edge && !beforeEDGEToken)) && element instanceof Operation){
                result.addAll(((Operation) element).getLabelList());
            } else if (element instanceof EdgeToken) {
                beforeEDGEToken = false;
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
    public void substituteVariables (List<Tuple4<String, String, Operation.WindowType, String[][]>> values) {
        for (Element element: elements) {
            if (element instanceof Operation){
                for (Tuple4<String, String, Operation.WindowType, String[][]> value: values) {
                    ((Operation) element).substituteVariable(value);
                }
            }
        }
    }

    @Override
    public SelectionSolver clone() {
        SelectionSolver selectionSolver = new SelectionSolver();
        selectionSolver.partition = this.partition;
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
        public enum Operator{EQUAL, GREATER, LESS, GREATEREQUAL, LESSEQUAL};
        public enum Type {VALUE, VARIABLE, LABEL};
        public enum WindowType {WITHIN, AGO, EVERYWITHIN};

        private Operator operator;
        /**
         * Values can be multiple in case of time windows, thus with 'variable' type set, otherwise it should be single element list
         */
        //private Pair<String[], String[]> values;
        private Pair<String[][], String[][]> values;
        private Pair<Type, Type> type;

        /**
         * null if window is not defined, formt "10s"
         */
        private final Pair<String, String> withinTimeUnits;

        /**
         * null if window is not defined
         */
        private final Pair<WindowType, WindowType> windowType;

        /**
         * it's not checked, but if one element of within is null, the corresponding element in windowType must be null and vice-versa
         * @param operator
         * @param values
         * @param type
         * @param within
         * @param windowType
         */
        Operation(Operator operator, Pair<String[][], String[][]> values, Pair<Type, Type> type, Pair<String, String> within, Pair<WindowType, WindowType> windowType) {
            this.operator = operator;
            this.values = values;
            this.type = type;
            this.withinTimeUnits = within;
            this.windowType = windowType;

        }

        /**
         * @return variable names and related WithinTimeUnit for variable in operation or empty list if no variable types
         * WithinTimeUnit can be null
         */
        List<Tuple3<String, String, WindowType>> getVariableList() {
            ArrayList<Tuple3<String, String, WindowType>> result = new ArrayList<>();
            if (type.first() == Type.VARIABLE) result.add(new Tuple3<>(values.first()[0][0], withinTimeUnits.first(), windowType.first()));
            if (type.second() == Type.VARIABLE) result.add(new Tuple3<>(values.second()[0][0], withinTimeUnits.second(), windowType.second()));
            return result;
        }

        /**
         * @return label names or empty list
         */
        List<String> getLabelList() {
            ArrayList<String> result = new ArrayList<>();
            if (type.first() == Type.LABEL) result.add(values.first()[0][0]);
            if (type.second() == Type.LABEL) result.add(values.second()[0][0]);
            return result;
        }

        void substituteLabel(String name, String[] values) {
            if (this.type.first()== Type.LABEL && this.values.first()[0][0].equals(name)) {
                this.values = new Pair<>(new String[][]{values}, this.values.second());
                this.type= new Pair<>(Type.VALUE, this.type.second());
            }
            if (this.type.second()== Type.LABEL && this.values.second()[0][0].equals(name)) {
                this.values = new Pair<>(this.values.first(), new String[][]{values});
                this.type = new Pair<>(type.first(), Type.VALUE);
            }

        }

        /**
         *
         * @param values
         */
        void substituteVariable(Tuple4<String, String, Operation.WindowType, String[][]> values) {
            if (this.type.first()== Type.VARIABLE && this.withinTimeUnits.first().equals(values.f1) && this.windowType.first().equals(values.f2) && this.values.first()[0][0].equals(values.f0)) {
                this.values = new Pair<>(values.f3, this.values.second());
                this.type= new Pair<>(Type.VALUE, this.type.second());
            }
            if (this.type.second()== Type.VARIABLE && this.withinTimeUnits.second().equals(values.f1) && this.windowType.second().equals(values.f2) && this.values.second()[0][0].equals(values.f0)) {
                this.values = new Pair<>(this.values.first(), values.f3);
                this.type = new Pair<>(type.first(), Type.VALUE);
            }

        }

        /**
         * Will it work at the end? "Ai tester l'ardua sentenza"
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
        private boolean everyWithinAgainstEveryWithin (String[][] everyWithinValues1, String[][] everyWithinValues2){
            for (String[] current: everyWithinValues1) {
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
        private boolean withinAgainstEveryWithin (String[][] withinValues, String[][] everyWithinValues){
            for (String[] current: withinValues) {
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
        private boolean withinAgainstWithin (String[][] values1, String[][] values2){
            for (String[] current: values1) {
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
        private boolean againstWithin (String[] value, String[][] values){
            for (String[] current: values) {
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
        private boolean againstEveryWithin (String[] value, String[][] values){
            for (String[] current: values) {
                if (!executeOperation(value, current)) return false;
            }
            return true;
        }

        /**
         * Comparison is lexicographical or after a conversion (if both terms are convertible to long)
         * @param values1 @NotNull
         * @param values2 @NotNull
         * @return
         */
        private boolean executeOperation (String[] values1, String[] values2){
            Double converted1 = null, converted2 = null;
            boolean converted;
            if (operator == null) throw new IllegalArgumentException("Operator must be defined");

            for (String value1: values1) {
                for (String value2: values2) {
                    converted = false;
                    if (operator == Operator.EQUAL) return value1.equals(value2);
                    else { //Try to parse as double
                        try{
                            converted1 = Double.parseDouble(value1);
                            converted2 = Double.parseDouble(value2);
                            converted = true;
                        } catch (Exception e){
                            System.out.println("Parsing as double Failed: " + value1 + " " + value2);
                        }

                        if (converted) { //Manage as Doubles
                            switch (operator) {
                                case GREATER:
                                    return converted1 > converted2;
                                case LESS:
                                    return converted1 < converted2;
                                case GREATEREQUAL:
                                    return converted1 >= converted2;
                                case LESSEQUAL:
                                    return converted1 <= converted2;
                            }
                        }
                        else { //Manage as strings
                            int result = value1.compareTo(value2);
                            if (result > 0 && operator == Operator.GREATER) return true;
                            if (result >= 0 && operator == Operator.GREATEREQUAL) return true;
                            if (result < 0 && operator == Operator.LESS) return true;
                            if (result <= 0 && operator == Operator.LESSEQUAL) return true;
                        }
                    }
                }
            }
            return false;
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

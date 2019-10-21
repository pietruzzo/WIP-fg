package shared.variables.solver;

import shared.Utils;
import shared.selection.SelectionSolver;
import shared.variables.Variable;
import shared.variables.VariableEdge;
import shared.variables.VariablePartition;
import shared.variables.VariableVertex;

import java.util.*;

public class VariableSolverSlave extends VariableSolver {



    /**
     * HashMap<variableName , ArrayList<variableVersion>>
     *     New versions are registered in tail inorder
     */
    //private final HashMap<String , LinkedList<VariableSimple>> variablesSimple;
    private final HashMap<String , LinkedList<VariableVertex>> variableVertex;
    private final HashMap<String , LinkedList<VariableEdge>> variablesEdge;
    private final HashMap<String , LinkedList<VariablePartition>> variablesPartition;

    public VariableSolverSlave() {
        //this.variablesSimple = new HashMap<>();
        this.variableVertex = new HashMap<>();
        this.variablesEdge = new HashMap<>();
        this.variablesPartition = new HashMap<>();
    }

    /**
     * Get value/values for current timestamp
     * @param nameVariable
     * @param nameNode
     * @return null if no variable versions are in list, otherwise a list of vertex-value
     */
    public String[] getValuesV (String nameVariable, String nameNode) {
        LinkedList<VariableVertex> result = variableVertex.get(nameVariable);
        if (result == null) return null;
        else return result.peekLast().getVertexValues(nameNode);

    }

    /**
     *
     * @param nameVariable
     * @param nameVertex
     * @param timeWindow in format %f(s, m, h)
     * @param windowType
     * @return all values or null if variable is undefined
     */
    public List<String[]> getValuesV (String nameVariable, String nameVertex, String timeWindow, SelectionSolver.Operation.WindowType windowType) {

        List<String[]> result = new ArrayList<>();
        LinkedList<VariableVertex> variables = variableVertex.get(nameVariable);
        if (variables == null) return null;
        long timeSolved = this.currentTimestamp - Utils.solveTime(timeWindow);

        Iterator<VariableVertex> iterator = variables.descendingIterator();
        VariableVertex current = null;
        String[] values = null;

        while (iterator.hasNext() && timeSolved > (current = iterator.next()).getTimestamp()){
            values = current.getVertexValues(nameVertex);
            if (windowType != null && windowType != SelectionSolver.Operation.WindowType.AGO && values!= null)
            result.add(values);
        }
        if (windowType!= null && current != null && current.getVertexValues(nameVertex) != null) {
            result.add(current.getVertexValues(nameVertex));
        }

        return result;
    }

    /**
     *
     * @param nameVariable
     * @param nameVertex
     * @param nameEdge
     * @return
     */
    public String[] getValuesE (String nameVariable, String nameVertex, String nameEdge) {
        List<String[]> result = getValuesE(nameVariable, nameVertex, nameEdge, "0s", SelectionSolver.Operation.WindowType.AGO);
        if (result == null) return null;
        else return result.get(0);
    }

    /**
     *
     * @param nameVariable
     * @param nameVertex
     * @param nameEdge
     * @param timeWindow
     * @param windowType
     * @return null if variable doesn't have any association or is not defined, otherwise return values
     */
    public List<String[]> getValuesE (String nameVariable, String nameVertex, String nameEdge, String timeWindow, SelectionSolver.Operation.WindowType windowType) {

        List<String[]> result = new ArrayList<>();
        LinkedList<VariableEdge> variables = variablesEdge.get(nameVariable);
        if (variables == null) return null;
        long timeSolved = this.currentTimestamp - Utils.solveTime(timeWindow);

        Iterator<VariableEdge> iterator = variables.descendingIterator();
        VariableEdge current = null;
        String[] values = null;

        while (iterator.hasNext() && timeSolved > (current = iterator.next()).getTimestamp()){
            values = current.getEdgeValues(nameVertex, nameEdge);
            if (windowType != null && windowType != SelectionSolver.Operation.WindowType.AGO && values!= null)
                result.add(values);
        }
        if (windowType!= null && current != null && current.getEdgeValues(nameVertex, nameEdge) != null) {
            result.add(current.getEdgeValues(nameVertex, nameEdge));
        }

        return result;
    }

    public List<String[]> getPartitionAggregate (String variableName, Map<String, String> compositeKey, String timeWindow, SelectionSolver.Operation.WindowType windowType) {
        List<String[]> result = new ArrayList<>();
        LinkedList<VariablePartition> variables = this.variablesPartition.get(variableName);
        if (variables == null) return null;
        long timeSolved = this.currentTimestamp - Utils.solveTime(timeWindow);

        Iterator<VariablePartition> iterator = variables.descendingIterator();
        VariablePartition current = null;
        String[] values = null;

        while (iterator.hasNext() && timeSolved > (current = iterator.next()).getTimestamp()){
            values = current.getAggregate(compositeKey);
            if (windowType != null && windowType != SelectionSolver.Operation.WindowType.AGO && values!= null)
                result.add(values);
        }
        if (windowType!= null && current != null && current.getAggregate(compositeKey) != null) {
            result.add(current.getAggregate(compositeKey));
        }

        return result;
    }

    @Override
    public void addVariable(Variable variable) {
        if (variable == null) throw new NullPointerException("variable passed is null");
        else if (variable instanceof VariableVertex) addToMap(this.variableVertex, (VariableVertex)variable);
        else if (variable instanceof VariableEdge) addToMap(this.variablesEdge, (VariableEdge)variable);
        else if (variable instanceof VariablePartition) addToMap(this.variablesPartition, (VariablePartition)variable);
        else {
            System.out.println("Wrong instance type for variable: " + variable.getName() + ",  " + variable.getClass().toGenericString());
        }
    }

    @Override
    public void removeOldVersions() {
        removeOldVariables(this.variablesEdge);
        removeOldVariables(this.variablesPartition);
        removeOldVariables(this.variableVertex);
    }


}

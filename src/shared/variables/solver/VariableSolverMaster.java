package shared.variables.solver;

import shared.Utils;
import shared.selection.SelectionSolver;
import shared.variables.Variable;
import shared.variables.VariableSimple;

import java.util.*;

public class VariableSolverMaster extends VariableSolver{

    /**
     * HashMap<variableName , ArrayList<variableVersion>>
     *     New versions are registered in tail inorder
     */
    private final HashMap<String , LinkedList<VariableSimple>> variablesSimple;

    public VariableSolverMaster() {
        this.variablesSimple = new HashMap<>();
    }

    /**
     *
     * @param variableName
     * @return value associated to variable or null if variable is not defined
     */
    public String[] getAggregate(String variableName){
        List<String[]> result = getAggregate(variableName, "0s", SelectionSolver.Operation.WindowType.AGO);
        if (result == null) return null;
        else return result.get(0);
    }

    /**
     *
     * @param nameVariable
     * @param timeWindow
     * @param windowType
     * @return null if variable is not defined, empty list if no values in the specified window
     */
    public List<String[]> getAggregate(String nameVariable, String timeWindow, SelectionSolver.Operation.WindowType windowType) {
        List<String[]> result = new ArrayList<>();
        LinkedList<VariableSimple> variables = variablesSimple.get(nameVariable);
        if (variables == null) return null;
        long timeSolved = this.currentTimestamp - Utils.solveTime(timeWindow);

        Iterator<VariableSimple> iterator = variables.descendingIterator();
        VariableSimple current = null;
        String[] values = null;

        while (iterator.hasNext() && timeSolved > (current = iterator.next()).getTimestamp()){
            if (windowType != null && windowType != SelectionSolver.Operation.WindowType.AGO && values!= null)
                result.add(values);
        }
        if (windowType!= null && current != null && current.getValue() != null) {
            result.add(current.getValue());
        }

        return result;
    }

    @Override
    public void addVariable(Variable variable) {
        if (variable instanceof VariableSimple) this.addToMap(variablesSimple, (VariableSimple)variable);
        else System.out.println("Wrong instance type for variable: " + variable.getName() + ",  " + variable.getClass().toGenericString());
    }

    @Override
    public void removeOldVersions(){
        this.removeOldVariables(this.variablesSimple);
    }
}

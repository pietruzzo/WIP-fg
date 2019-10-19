package shared.variables;

import akka.japi.Pair;
import shared.selection.SelectionSolver;
import shared.selection.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VariableSolver {

    /**
     * HashMap<variableName , ArrayList<variableVersion>>
     */
    private final HashMap<String , ArrayList<Variable>> variables;

    public VariableSolver() {
        this.variables = new HashMap<>();
    }

    public List<String[]> getValuesV (String name) {}

    /**
     *
     * @param name
     * @param timeWindow in format %f(s, m, h)
     * @param windowType
     * @return
     */
    public List<String[]> getValuesV (String name, String timeWindow, SelectionSolver.Operation.WindowType windowType) {}

    public List<Pair<String, String[]>> getValuesE (String name) {}

    public List<Pair<String, String[]>> getValuesE (String name, String timeWindow, SelectionSolver.Operation.WindowType windowType) {}

    public void addVariable(Variable variable) {}

    public void removeOld(Variable variable) {}
}

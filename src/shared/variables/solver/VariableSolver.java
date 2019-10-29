package shared.variables.solver;

import jdk.internal.jline.internal.Nullable;
import shared.Utils;
import shared.exceptions.VariableNotDefined;
import shared.exceptions.WrongTypeRuntimeException;
import shared.variables.*;
import shared.selection.SelectionSolver.Operation.WindowType;

import java.util.*;
import java.util.stream.Collectors;

public class VariableSolver {


    //Todo : use tail with navigableTreeMap (navigable can't be final)
    private final HashMap<String , LinkedList<Variable>> variables;
    private long currentTimestamp;

    public VariableSolver() {
        this.variables = new HashMap<>();
    }

    public String[] getAggregate(String variableName, @Nullable Map<String, String> partition){
        List<String[]> result = this.getAggregate(variableName, partition, null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }
    public String[] getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName){
        List<String[]> result = this.getVertexVariable(variableName, partition, vertexName, null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }
    public String[] getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName){
        List<String[]> result = this.getEdgeVariable(variableName, partition, vertexName, edgeName, null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }

    /**
     *
     * @param variableName
     * @param partition if defined, try to identify the partition
     * @param timeWindow if null use now
     * @param windowType if null use AGO
     * @throws WrongTypeRuntimeException variable is of wrong type
     * @throws VariableNotDefined if variable name is not registered
     * @return
     */
    public List<String[]> getAggregate(String variableName, @Nullable Map<String, String> partition, @Nullable String timeWindow, @Nullable WindowType windowType){

        List<Variable> selectedVars = getSelectedVariable(variableName, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableAggregate)) throw new WrongTypeRuntimeException(VariableAggregate.class, variable.getClass());
            result.add(((VariableAggregate) variable).getValue());
        }
        return result;
    }

    /**
     *
     * @param variableName
     * @param partition if defined, try to identify the partition
     * @param timeWindow if null use now
     * @param windowType if null use AGO
     * @return
     */
    public List<String[]> getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, @Nullable String timeWindow, @Nullable WindowType windowType){

        List<Variable> selectedVars = getSelectedVariable(variableName, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableVertex)) throw new WrongTypeRuntimeException(VariableVertex.class, variable.getClass());
            result.add(((VariableVertex) variable).getVertexValues(vertexName));
        }
        return result;
    }

    /**
     *
     * @param variableName
     * @param partition if defined, try to identify the partition
     * @param timeWindow if null use now
     * @param windowType if null use AGO
     * @return
     */
    public List<String[]> getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName , @Nullable String timeWindow, @Nullable WindowType windowType){

        List<Variable> selectedVars = getSelectedVariable(variableName, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableEdge)) throw new WrongTypeRuntimeException(VariableEdge.class, variable.getClass());
            result.add(((VariableEdge) variable).getEdgeValues(vertexName, edgeName));
        }
        return result;
    }


    /**
     *
     * @param variableName
     * @param partition
     * @param timeWindow
     * @param windowType
     * @throws VariableNotDefined if variable isn't defined
     * @return Can return empty set
     */
    private List<Variable> getSelectedVariable (String variableName, @Nullable Map<String, String> partition, String timeWindow, WindowType windowType) throws VariableNotDefined {
        Long timeWindowL = null;
        if (timeWindow != null) timeWindowL = Utils.solveTime(timeWindow);
        LinkedList<Variable> entireVarList = this.variables.get(variableName);
        if (entireVarList == null) throw new VariableNotDefined(variableName);
        List<Variable> windowed = extractWindow(entireVarList, timeWindowL, windowType);

        if (!(windowed.isEmpty()) && partition!= null && windowed.get(0) instanceof VariablePartition){
            List<VariablePartition> toBeExtractFromPartition = windowed.stream().map(variable -> (VariablePartition) variable).collect(Collectors.toList());
            windowed = extractfromPartition(toBeExtractFromPartition, partition);
        }

        return windowed;
    }
    /**
     * @param selectedVars not null
     * @param timeWindow @Nullable ->take latest
     * @param windowType @NotNull
     * @return
     */
    private ArrayList<Variable>extractWindow(LinkedList<Variable> selectedVars, Long timeWindow, WindowType windowType){

        ArrayList<Variable> result = new ArrayList<>();
        if (timeWindow == null) timeWindow = 0L;
        if (windowType == null) windowType = WindowType.AGO;

        long timeSolved = this.currentTimestamp - timeWindow;

        Iterator<Variable> iterator = selectedVars.descendingIterator();
        Variable current = null;

        while (iterator.hasNext() && timeSolved < (current = iterator.next()).getTimestamp()){
            if (windowType == WindowType.WITHIN || windowType == WindowType.EVERYWITHIN)
                result.add(current);
        }
        if (windowType == WindowType.AGO && current != null && timeSolved >= (current = iterator.next()).getTimestamp()) {
            result.add(current);
        }
        return result;
    }

    /**
     * Extract variable list from partition list
     * @param partitions
     * @throws IllegalArgumentException wrong key format
     * @return
     */
    private List<Variable> extractfromPartition(List<VariablePartition> partitions, Map<String, String> partition) throws IllegalArgumentException{
        ArrayList<Variable> result = new ArrayList<>();
        for (VariablePartition varp: partitions) {
            result.add(varp.getInsideVariable(partition));
        }
        return result;
    }

    /**
     * It assums increasing timestamp order
     * @param variable
     */
    public void addVariable(Variable variable) {
        this.variables.computeIfAbsent(variable.getName(), k -> new LinkedList<>());
        this.variables.get(variable.getName()).add(variable);
    }

    public void removeOldVersions() {
        long keepTimestamp;
        int versionsToDelete;
        for (LinkedList<Variable> list: this.variables.values()) {
            versionsToDelete = 0;
            keepTimestamp = this.currentTimestamp - list.peekLast().getPersistence();
            Variable current = null, next = null;
            Iterator<Variable> iterator = list.iterator();
            while (iterator.hasNext()){
                current = next;
                next = iterator.next();
                if (current!=null && next.getTimestamp() < keepTimestamp) {
                    versionsToDelete = versionsToDelete + 1;
                }
                if (next.getTimestamp() > keepTimestamp) break;
            }
            for (int i = 0; i < versionsToDelete; i++) {
                list.removeFirst();
            }
        }
    }

    /**
     * Expressed in ms, it need to be set manually in every new timestamp
     */

    public void setCurrentTimestamp (long timestamp) {
        this.currentTimestamp = timestamp;
    }
}

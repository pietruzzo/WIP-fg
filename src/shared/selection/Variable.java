package shared.selection;

import akka.japi.Pair;

import java.io.Serializable;
import java.util.HashMap;

public class Variable implements Serializable {

    private static final long serialVersionUID = 200020L;

    public final String name;
    public final boolean isAggregate;
    public Long survivalOldVersionsTimeWindow = null;

    /**
     * If FreeVariable
     **/
    private final HashMap<String, Pair<String, String>> freeVariable;

    private final String variableNotFree;

    /**
     * Constructor for not free variables
     * @param name
     * @param value
     */
    public Variable(String name, String value, boolean isAggregate) {
        this.name = name;
        this.isAggregate = isAggregate;
        this.freeVariable = null;
        this.variableNotFree = value;
    }

    /**
     * Constructor to support free variables
     * @param name
     * @param values
     */
    public Variable(String name, HashMap<String, Pair<String, String>> values, boolean isAggregate) {
        this.name = name;
        this.isAggregate = isAggregate;
        this.variableNotFree = null;
        this.freeVariable = values;
    }

    public void setSurvivalOldVersionsTimeWindow(long survivalOldVersionsTimeWindow) {
        this.survivalOldVersionsTimeWindow = survivalOldVersionsTimeWindow;
    }

    public long getSurvivalOldVersionsTimeWindow() {
        return survivalOldVersionsTimeWindow;
    }

    public boolean isFreeVariable(){
        return this.variableNotFree==null;
    }
    public String getName() {
        return name;
    }

    public boolean isAggregate() {
        return isAggregate;
    }

    public HashMap<String, Pair<String, String>> getFreeVariable() {
        return freeVariable;
    }

    public String getVariableNotFree() {
        return variableNotFree;
    }
}

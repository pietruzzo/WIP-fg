package shared.exceptions;

public class VariableNotDefined extends RuntimeException{

    private final String varName;

    public VariableNotDefined(String variableName){
        this.varName = variableName;
    }

    @Override
    public String getMessage() {
        return "Variable " + varName + " isn't defined in VariableSolver";
    }
}


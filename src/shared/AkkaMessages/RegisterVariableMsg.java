package shared.AkkaMessages;

import shared.selection.Variable;

import java.io.Serializable;

public class RegisterVariableMsg implements Serializable {

    private static final long serialVersionUID = 200022L;

    private final Variable variable;

    public RegisterVariableMsg(Variable variable) {
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }
}

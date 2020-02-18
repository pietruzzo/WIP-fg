package shared.AkkaMessages;

import java.io.Serializable;

public class RestoreVariableGraphMsg implements Serializable {

    private static final long serialVersionUID = 200054L;

    private final String variableName;
    private final String timeAgo;

    public RestoreVariableGraphMsg(String variableName, String timeAgo) {
        this.variableName = variableName;
        this.timeAgo = timeAgo;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    @Override
    public String toString() {
        return "RestoreVariableGraphMsg{" +
                "variableName='" + variableName + '\'' +
                ", timeAgo='" + timeAgo + '\'' +
                '}';
    }
}

package shared.AkkaMessages;

import java.io.Serializable;

public class NewPartitionMsg implements Serializable {
    private static final long serialVersionUID = 200045L;

    private final String partitionVariable;
    private final String[] freeLabels;
    private final String[] variableNames;

    public NewPartitionMsg(String partitionVariable, String[] freeLabels, String[] variableNames) {
        this.partitionVariable = partitionVariable;
        this.freeLabels = freeLabels;
        this.variableNames = variableNames;
    }

    public String getPartitionVariable() {
        return partitionVariable;
    }

    public String[] getFreeLabels() {
        return freeLabels;
    }

    public String[] getFreeVariableNames() {
        return variableNames;
    }
}

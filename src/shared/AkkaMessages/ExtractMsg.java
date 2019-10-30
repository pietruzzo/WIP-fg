package shared.AkkaMessages;

import shared.streamProcessing.Operations;

import java.io.Serializable;
import java.util.List;

public class ExtractMsg implements Serializable {

    private static final long serialVersionUID = 200046L;

    private final boolean edge; //If false its is vertex
    private final String label;
    private final List<Operations> operationsList;

    public ExtractMsg(boolean isEdge, String label, List<Operations> operationList) {
        this.edge = isEdge;
        this.label = label;
        this.operationsList = operationList;
    }

    public boolean isEdge() {
        return edge;
    }

    public String getLabel() {
        return label;
    }

    public List<Operations> getOperationsList() {
        return operationsList;
    }

}

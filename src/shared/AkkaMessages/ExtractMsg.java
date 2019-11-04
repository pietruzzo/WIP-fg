package shared.AkkaMessages;

import shared.streamProcessing.Operations;

import java.io.Serializable;
import java.util.List;

public class ExtractMsg implements Serializable {

    private static final long serialVersionUID = 200046L;

    private final List<Operations> operationsList;

    public ExtractMsg(boolean isEdge, String label, List<Operations> operationList) {
        this.operationsList = operationList;
    }

    public List<Operations> getOperationsList() {
        return operationsList;
    }

}

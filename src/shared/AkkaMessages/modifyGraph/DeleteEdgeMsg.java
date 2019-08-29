package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;


import java.io.Serializable;
import java.util.ArrayList;

public class DeleteEdgeMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200010L;

    final private String sourceName;
    final private String destinationName;

    public DeleteEdgeMsg(String sourceName, String destinationName) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    @Override
    public String toString() {
        return "Delete Edge: " + sourceName + " " + destinationName;
    }
}

package shared.AkkaMessages.modifyGraph;

import java.io.Serializable;

public class deleteEdge implements Serializable {
    private static final long serialVersionUID = 200010L;

    final private String sourceName;
    final private String destinationName;

    public deleteEdge(String sourceName, String destinationName) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

}

package shared.AkkaMessages.modifyGraph;

import java.io.Serializable;

public class addEdge implements Serializable {
    private static final long serialVersionUID = 200009L;

    final private String sourceName;
    final private String destinationName;

    public addEdge(String sourceName, String destinationName) {
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

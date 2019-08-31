package shared.AkkaMessages.modifyGraph;

import jdk.internal.jline.internal.Nullable;

public class DeleteEdgeMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200010L;

    final private String sourceName;
    final private String destinationName;
    final private Long timestamp;

    public DeleteEdgeMsg(String sourceName, String destinationName, @Nullable Long timestamp) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.timestamp = timestamp;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Delete Edge: " + sourceName + " " + destinationName + " " + timestamp;
    }
}

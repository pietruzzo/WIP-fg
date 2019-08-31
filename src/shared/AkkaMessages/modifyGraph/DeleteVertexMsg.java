package shared.AkkaMessages.modifyGraph;

public class DeleteVertexMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200011L;

    final private String vertexName;
    final private Long timestamp;

    public DeleteVertexMsg(String vertexName, Long timestamp) {
        this.vertexName = vertexName;
        this.timestamp = timestamp;
    }

    public String getVertexName() {
        return vertexName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Delete vertex: " + vertexName + " " + timestamp;
    }
}

package shared.AkkaMessages.modifyGraph;

import java.io.Serializable;

public class DeleteVertexMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200011L;

    final private String vertexName;

    public DeleteVertexMsg(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getVertexName() {
        return vertexName;
    }

    @Override
    public String toString() {
        return "Delete vertex: " + vertexName;
    }
}

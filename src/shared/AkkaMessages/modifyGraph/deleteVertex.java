package shared.AkkaMessages.modifyGraph;

import java.io.Serializable;

public class deleteVertex implements Serializable {
    private static final long serialVersionUID = 200011L;

    final private String vertexName;

    public deleteVertex(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getVertexName() {
        return vertexName;
    }
}

package shared.AkkaMessages.modifyGraph;

import shared.Vertex;

import java.io.Serializable;

public class updateVertex implements Serializable {
    private static final long serialVersionUID = 200012L;

    final private Vertex vertexUpdate;

    public updateVertex(Vertex vertexUpdate) {
        this.vertexUpdate = vertexUpdate;
    }

}

package shared.computation;

import java.io.Serializable;

public interface Vertex extends Serializable {

    String getNodeId();

    String[] getEdges();

}

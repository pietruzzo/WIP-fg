package shared.computation;

import java.io.Serializable;

public interface Vertex extends Serializable {

    String getNodeId();

    String[] getLabelVertex(String labelName);

    String[] getEdges();

    String[] getLabelEdge (String edge, String labelName);

}

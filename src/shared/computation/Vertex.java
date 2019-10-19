package shared.computation;

import shared.VertexNew;

import java.io.Serializable;
import java.util.HashMap;

public interface Vertex extends Serializable {

    String getNodeId();

    String[] getLabelVertex(String labelName);

    String[] getEdges();

    String[] getLabelEdge (String edge, String labelName);

}

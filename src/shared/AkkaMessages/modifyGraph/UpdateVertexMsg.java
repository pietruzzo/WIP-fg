package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;

import java.util.ArrayList;

public class UpdateVertexMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200012L;

    final public String vertexName;
    final private ArrayList<Pair<String, String[]>> attributes;
    final private Long timestamp;
    final private boolean isInsertion;

    public UpdateVertexMsg(String vertexName, ArrayList<Pair<String, String[]>> attributes, Long timestamp, boolean isInsertion) {
        this.vertexName = vertexName;
        this.attributes = attributes;
        this.timestamp = timestamp;
        this.isInsertion = isInsertion;
    }
    public ArrayList<Pair<String, String[]>> getAttributes(){
        //Shallow copy is enough since Pair is immutable
        return attributes;
    }

    public String getVertexName() {
        return vertexName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public boolean isInsertion() {
        return isInsertion;
    }

    @Override
    public String toString() {
        String attributesString = "";
        for (Pair<String, String[]> p: attributes) {
            for (String second: p.second()) {
                attributesString = attributesString + ", (" + p.first() + ", " + second + ")";
            }
        }
        return isInsertion ? "Insert Vertex: " + vertexName + attributesString + " " + timestamp : "Update Vertex: " + vertexName + attributesString + " " + timestamp;
    }
}

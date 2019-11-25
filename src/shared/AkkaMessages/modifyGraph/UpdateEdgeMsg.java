package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;

import java.util.ArrayList;

public class UpdateEdgeMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200060L;

    final public String sourceId;
    final public String destId;
    final private ArrayList<Pair<String, String[]>> attributes;
    final private Long timestamp;

    public UpdateEdgeMsg(String sourceId, String destId, ArrayList<Pair<String, String[]>> attributes, Long timestamp) {
        this.sourceId = sourceId;
        this.destId = destId;
        this.attributes = attributes;
        this.timestamp = timestamp;
    }
    public ArrayList<Pair<String, String[]>> getAttributes(){
        //Shallow copy is enough since Pair is immutable
        return attributes;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        String attributesString = "";
        for (Pair<String, String[]> p: attributes) {
            for (String second: p.second()) {
                attributesString = attributesString + ", (" + p.first() + ", " + second + ")";
            }
        }
        return "Update Vertex: " + sourceId + attributesString + " " + timestamp;
    }
}

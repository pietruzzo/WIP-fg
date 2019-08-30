package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;

import java.util.ArrayList;

public class UpdateVertexMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200012L;

    final public String vertexName;
    final private ArrayList<Pair<String, String>> attributes;

    public UpdateVertexMsg(String vertexName, ArrayList<Pair<String, String>> attributes) {
        this.vertexName = vertexName;
        this.attributes = attributes;
    }
    public ArrayList<Pair<String, String>> getAttributes(){
        //Shallow copy is enough since Pair is immutable
        return (ArrayList<Pair<String, String>>) attributes.clone();
    }

    public String getVertexName() {
        return vertexName;
    }

    @Override
    public String toString() {
        String attributesString = "";
        for (Pair<String, String> p: attributes) {
            attributesString = attributesString + ", (" + p.first() + ", " + p.second() + ")";
        }
        return "Update Vertex: " + vertexName + attributesString;
    }
}

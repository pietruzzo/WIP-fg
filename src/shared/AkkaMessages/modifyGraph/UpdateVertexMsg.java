package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;
import shared.Vertex;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateVertexMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200012L;

    final public String vertex;
    final private ArrayList<Pair<String, String>> attributes;

    public UpdateVertexMsg(String vertex, ArrayList<Pair<String, String>> attributes) {
        this.vertex = vertex;
        this.attributes = attributes;
    }
    public ArrayList<Pair<String, String>> getAttributes(){
        //Shallow copy is enough since Pair is immutable
        return (ArrayList<Pair<String, String>>) attributes.clone();
    }

    @Override
    public String toString() {
        String attributesString = "";
        for (Pair<String, String> p: attributes) {
            attributesString = attributesString + ", (" + p.first() + ", " + p.second() + ")";
        }
        return "Update Vertex: " + vertex + attributesString;
    }
}

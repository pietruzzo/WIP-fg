package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class AddEdgeMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200009L;

    final private String sourceName;
    final private String destinationName;
    final private ArrayList<Pair<String, String>> attributes;

    public AddEdgeMsg(String sourceName, String destinationName, ArrayList<Pair<String, String>> attributes) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.attributes = attributes;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public ArrayList<Pair<String, String>> getAttributes() {
        return (ArrayList<Pair<String, String>>) attributes.clone();
    }

    @Override
    public String toString() {
        String attributesString = "";
        for (Pair<String, String> p: attributes) {
            attributesString = attributesString + ", (" + p.first() + ", " + p.second() + ")";
        }
        return "Add Edge: " + sourceName + " " + destinationName + attributesString;
    }
}

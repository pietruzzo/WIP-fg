package shared.AkkaMessages.modifyGraph;

import akka.japi.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AddEdgeMsg implements ModifyGraphMsg {
    private static final long serialVersionUID = 200009L;

    final private String sourceName;
    final private String destinationName;
    final private ArrayList<Pair<String, String[]>> attributes;
    final private Long timestamp;

    public AddEdgeMsg(String sourceName, String destinationName, ArrayList<Pair<String, String[]>> attributes, @Nullable Long timestamp) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.attributes = attributes;
        this.timestamp = timestamp;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public ArrayList<Pair<String, String[]>> getAttributes() {
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
        return "Add Edge: " + sourceName + " " + destinationName + attributesString + " " + timestamp;
    }
}

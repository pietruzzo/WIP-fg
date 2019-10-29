package shared.AkkaMessages;

import java.io.Serializable;

public class ExtractMsg implements Serializable {

    private static final long serialVersionUID = 200046L;

    private final boolean edge; //If false its is vertex
    private final String label;

    public ExtractMsg(boolean isEdge, String label) {
        this.edge = isEdge;
        this.label = label;
    }

    public boolean isEdge() {
        return edge;
    }

    public String getLabel() {
        return label;
    }
}

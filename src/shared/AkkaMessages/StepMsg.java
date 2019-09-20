package shared.AkkaMessages;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Message that slaves exchange to each other
 */
public class StepMsg implements Serializable {

    private static final long serialVersionUID = 200004L;

    public final String destinationVertex;
    public final String originVertex;

    public final HashMap<String, String> computationValues;



    public StepMsg(String destinationVertex, String originVertex, HashMap<String, String> computationValues) {
        this.destinationVertex = destinationVertex;
        this.originVertex = originVertex;
        this.computationValues = computationValues;
    }


}

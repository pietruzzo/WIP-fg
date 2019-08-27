package shared.AkkaMessages;

import java.io.Serializable;
import java.util.Map;

public class StepMessage implements Serializable {

    private static final long serialVersionUID = 200004L;

    final String destinationVertex;
    final String originVertex;

    final Map<String, String> computationValues;


    public StepMessage(String destinationVertex, String originVertex, Map<String, String> computationValues) {
        this.destinationVertex = destinationVertex;
        this.originVertex = originVertex;
        this.computationValues = computationValues;
    }
}

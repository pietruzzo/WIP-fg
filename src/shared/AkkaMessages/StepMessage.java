package shared.AkkaMessages;

import shared.computation.ComputationId;

import java.io.Serializable;
import java.util.HashMap;

public class StepMessage implements Serializable {

    private static final long serialVersionUID = 200004L;

    final String destinationVertex;
    final String originVertex;

    final HashMap<String, String> computationValues;



    public StepMessage(String destinationVertex, String originVertex, HashMap<String, String> computationValues, ComputationId computation) {
        this.destinationVertex = destinationVertex;
        this.originVertex = originVertex;
        this.computationValues = computationValues;
    }


}

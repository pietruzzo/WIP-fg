package shared.data;

import java.io.Serializable;

/**
 * Message that slaves exchange to each other
 */
public class StepMsg<TMsg> implements Serializable {

    private static final long serialVersionUID = 200004L;

    public final String destinationVertex;
    public final String originVertex;

    public final TMsg computationValues;



    public StepMsg(String destinationVertex, String originVertex, TMsg computationValues) {
        this.destinationVertex = destinationVertex;
        this.originVertex = originVertex;
        this.computationValues = computationValues;
    }


}

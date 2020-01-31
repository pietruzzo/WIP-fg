package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;

import java.io.Serializable;
import java.util.List;

public interface SubstitutableLabels extends Serializable {

    /**
     *
     * @param tuple
     * @param argument argument order given by the pattern
     * @return
     */
    String[] getField(Tuple tuple, int argument);

    String[] getField(Tuple tuple);

    /**
     *
     * @param tuple
     * @return null if no node id
     */
    String[] getNodeId(Tuple tuple);

    /**
     *
     * @param tuple
     * @return null if no edge
     */
    String[] getEdgeId(Tuple tuple);



    /**
     * Generate tuple according to streamType (attaching node/edge IDs)
     * @param oldTuple
     * @param fields
     * @return
     */
    Tuple generateTuple (Tuple oldTuple, List<String[]> fields);

}

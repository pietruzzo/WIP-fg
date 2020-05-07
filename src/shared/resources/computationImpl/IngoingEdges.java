package shared.resources.computationImpl;

import akka.japi.Pair;
import shared.computation.Computation;
import shared.computation.Vertex;
import shared.data.StepMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Find max number of IngoingEdges with the possibility to flood the maximum number.
 * Maximum will be register only to reachable nodes, otherwise some nodes could stuck to local max
 *
 * Parameters:
 *          0: "spread" to spread maximum (value "true" for true)
 *
 * Return Values :
 *          0: (Maximum) outgoing edges label name
 */
public class IngoingEdges extends Computation {

    private boolean spread;

    private HashMap<String, Integer> ingoingEdgeNum;

    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {

        List<StepMsg> outbox = new ArrayList<>();

        if (iterationStep == 1) {
            ingoingEdgeNum.put(vertex.getNodeId(), incoming.size());

            if (!spread) {
                voteToHalt();
            } else {
                for (String destination: vertex.getEdges()) {
                    outbox.add(new StepMsg<>(destination, vertex.getNodeId(), incoming.size()) );
                }
            }

        } else {
            // From superstep = 2 -> collect maximum of neighbor and, if major that current, update and spread

            int max = ingoingEdgeNum.get(vertex.getNodeId());

            //Update max
            for (StepMsg sm: incoming) {
                if (((Integer) sm.computationValues) > max){
                    max = ((Integer) sm.computationValues);
                }
            }

            //if max is changed -> update and send
            if (max != ingoingEdgeNum.get(vertex.getNodeId())){
                ingoingEdgeNum.put(vertex.getNodeId(), max);
                for (String destination: vertex.getEdges()) {
                    outbox.add(new StepMsg<>(destination, vertex.getNodeId(), max) );
                }
            } else { // Otherwise don't send and halt
                voteToHalt();
            }

        }
        return outbox;
    }

    @Override
    public List<StepMsg> firstIterate(Vertex vertex) {

        List<StepMsg> outbox = new ArrayList<>();

        for (String destination: vertex.getEdges()) {
            outbox.add( new StepMsg(destination, vertex.getNodeId(), null) );
        }

        return outbox;
    }

    @Override
    public List<Pair<String, String[]>> computeResults(Vertex vertex) {

        List<Pair<String, String[]>> returnValues = new ArrayList<>();

        returnValues.add(new Pair<>(this.computationParameters.returnVarNames().get(0), new String[]{
                String.valueOf(this.ingoingEdgeNum.get(vertex.getNodeId()))
        }));

        return returnValues;
    }

    @Override
    public void preStart() {

        this.spread = computationParameters.getParameter("spread").equals("true");
        ingoingEdgeNum = new HashMap<>();
    }

}

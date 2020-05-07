package shared.resources.computationImpl;

import akka.japi.Pair;
import shared.computation.Computation;
import shared.computation.Vertex;
import shared.data.StepMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Find number of OutgoingEdges with the possibility to flood the maximum number.
 * Maximum will be register only to reachable nodes
 *
 * Parameters:
 *          0: "spread" to spread maximum ( "true" to spread, false as Default)
 *
 * Return Values :
 *          0: (Maximum) outgoing edges label name
 */
public class OutgoingEdges extends Computation {

    private boolean spread;

    private ConcurrentHashMap<String, Integer> outgoingEdgesNum;

    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {

        List<StepMsg> outbox = new ArrayList<>();

        int max = incoming
                .stream()
                .map(msg -> (int) msg.computationValues)
                .reduce((i1, i2) -> i1 > i2 ? i1 : i2)
                .get();

        //STOP if new max is not greater than older one
        if (max <= outgoingEdgesNum.get(vertex.getNodeId())) {
            voteToHalt();

        }else {

            //Update value
            outgoingEdgesNum.put(vertex.getNodeId(), max);

            //Spread Value
            for (String destination: vertex.getEdges()) {
                outbox.add( new StepMsg(destination, vertex.getNodeId(), max) );
            }
        }

        return outbox;
    }

    @Override
    public List<StepMsg> firstIterate(Vertex vertex) {

        List<StepMsg> outbox = new ArrayList<>();

        this.outgoingEdgesNum.put(vertex.getNodeId(), vertex.getEdges().length);

        if (!this.spread)
            voteToHalt();

        else {
            for (String destination : vertex.getEdges()) {
                outbox.add(new StepMsg(destination, vertex.getNodeId(), vertex.getEdges().length));
            }
        }

        return outbox;
    }

    @Override
    public List<Pair<String, String[]>> computeResults(Vertex vertex) {

        List<Pair<String, String[]>> returnValues = new ArrayList<>();

        returnValues.add(new Pair<>(this.computationParameters.returnVarNames().get(0), new String[]{
                String.valueOf(this.outgoingEdgesNum.get(vertex.getNodeId()))
        }));

        return returnValues;
    }

    @Override
    public void preStart() {

        outgoingEdgesNum = new ConcurrentHashMap<>();
        String spread = computationParameters.getParameter("spread");
        if (spread != null) {
            this.spread = spread.equals("true");
        } else {
            this.spread = false;
        }

    }

}

package shared.resources.computationImpl;

import akka.japi.Pair;
import shared.AkkaMessages.StepMsg;
import shared.computation.Computation;
import shared.computation.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Find max number of OutgoingEdges with the possibility to flood the maximum number.
 * Maximum will be register only to reachable nodes, otherwise some nodes could stuck to local max
 *
 * Parameters:
 *          0: "spread" to spread maximum ( "true" to spread )
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
                .map(msg -> (int)msg.computationValues)
                .reduce((i1, i2) -> i1 > i2 ? i1 : i2)
                .get();

        //STOP if new max is not greater than older one
        if (max <= outgoingEdgesNum.get(vertex.getNodeId()))
            return null;

        outgoingEdgesNum.put(vertex.getNodeId(), max);

        for (String destination: vertex.getEdges()) {
            outbox.add( new StepMsg(destination, vertex.getNodeId(), max) );
        }
        return outbox;
    }

    @Override
    public List<StepMsg> firstIterate(Vertex vertex) {

        List<StepMsg> outbox = new ArrayList<>();

        this.outgoingEdgesNum.put(vertex.getNodeId(), vertex.getEdges().length);

        if (!this.spread)
            return null;

        else {

            for (String destination : vertex.getEdges()) {
                outbox.add(new StepMsg(destination, vertex.getNodeId(), vertex.getEdges().length));
            }
            return outbox;

        }
    }

    @Override
    public List<Pair<String, String[]>> computeResults(Vertex vertex) {

        List<Pair<String, String[]>> returnValues = new ArrayList<>();

        returnValues.add(new Pair<>(this.returnVarNames().get(0), new String[]{
                String.valueOf(this.outgoingEdgesNum.get(vertex.getNodeId()))
        }));

        return returnValues;
    }

    @Override
    public void preStart() {

        outgoingEdgesNum = new ConcurrentHashMap<>();
        this.spread = computationParameters.getParameter("spread").equals("true");

    }

}

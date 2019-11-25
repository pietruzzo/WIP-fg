package computationImpl;

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
 *          0: "spread" to spread maximum
 *
 * Return Values :
 *          0: (Maximum) outgoing edges label name
 */
public class OutgoingEdges implements Computation {

    private String returnLabelName;
    private boolean spread;

    private ConcurrentHashMap<String, Integer> outgoingEdgesNum;

    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {

        List<StepMsg> outbox = new ArrayList<>();

        if (iterationStep == 1) {
            outgoingEdgesNum.put(vertex.getNodeId(), incoming.size());

            for (String destination: vertex.getEdges()) {
                outbox.add( new StepMsg(destination, vertex.getNodeId(), incoming.size()) );
            }

            return outbox;
        }

        if (!spread)
            return null;

        else {

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

        returnValues.add(new Pair<>(this.returnLabelName, new String[]{
                String.valueOf(this.outgoingEdgesNum.get(vertex.getNodeId()))
        }));

        return returnValues;
    }

    /**
     *
     * @param parameters 0: "spread" keyword to spread maximum on reachable subgraph
     * @param resultLabelsNames
     */
    @Override
    public void preInitialize(String[] parameters, String[] resultLabelsNames) {

        if (parameters.length >= 1 && parameters[0].equals("spread")) {
            this.spread = true;
        } else {
            this.spread = false;
        }

    }

    @Override
    public void preStart() {
        outgoingEdgesNum = new ConcurrentHashMap<>();
    }

}

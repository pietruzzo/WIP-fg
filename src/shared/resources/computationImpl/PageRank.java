package shared.resources.computationImpl;

import akka.japi.Pair;
import shared.AkkaMessages.StepMsg;
import shared.computation.Computation;
import shared.computation.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of PageRank working on DirectedGraphs
 *
 * Parameters:
 *          0: maxIterations
 *          1: threshold : convergence threshold (null if you don't want to use it)
 *
 * Return Values :
 *          0: Rank label name
 */
public class PageRank extends Computation {

    private int maxNumOfIterations;
    private Double threshold;

    private ConcurrentHashMap<String, Double> weights;

    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {

        if (iterationStep >= maxNumOfIterations) return null;

        Double newWeight = incoming
                .stream()
                .map(msg -> (Double)msg.computationValues)
                .reduce((d1, d2) -> d1 + d2)
                .get();

        double delta = newWeight - weights.get(vertex.getNodeId());

        weights.put(vertex.getNodeId(), newWeight);

        if (threshold != null && delta < threshold) {
            return null;
        } else {

            List<StepMsg> outbox = new ArrayList<>();
            Double weightToSend =  newWeight / vertex.getEdges().length;

            for (String dest: vertex.getEdges()) {
                outbox.add(new StepMsg(dest, vertex.getNodeId(), weightToSend));
            }
            return outbox;
        }

    }

    @Override
    public List<StepMsg> firstIterate(Vertex vertex) {

        List<StepMsg> outbox = new ArrayList<>();
        double weightToSend =  1.0 / vertex.getEdges().length;

        this.weights.put(vertex.getNodeId(), 1.0);

        for (String dest: vertex.getEdges()) {
            outbox.add(new StepMsg(dest, vertex.getNodeId(), weightToSend));
        }
        return outbox;
    }

    @Override
    public List<Pair<String, String[]>> computeResults(Vertex vertex) {

        List<Pair<String, String[]>> returnLabels = new ArrayList<>();

        returnLabels.add(new Pair<>(
                this.returnVarNames().get(0),
                new String[]{String.valueOf(this.weights.get(vertex.getNodeId()))}
                ));

        return returnLabels;
    }

    @Override
    public void preStart() {

        this.maxNumOfIterations = Integer.parseInt(computationParameters.getParameter("maxIterations"));
        String threshold = computationParameters.getParameter("threshold");
        if (threshold == null || threshold.equals("null")){
            this.threshold = null;
        } else {
            this.threshold = Double.parseDouble(threshold);
        }

        this.weights = new ConcurrentHashMap<>();

    }
}

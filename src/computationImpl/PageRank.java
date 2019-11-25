package computationImpl;

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
 *          0: maxNumberofIterations,
 *          1: convergence threshold (null if you don't want to use it)
 *
 * Return Values :
 *          0: Rank label name
 */
public class PageRank implements Computation {

    private String returnLabelName;
    private int maxNumOfIterations;
    private Double threshold;

    private ConcurrentHashMap<String, Double> weights;

    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {

        if (iterationStep >= maxNumOfIterations) return null;

        Double newWeight = incoming
                .stream()
                .map(msg -> (double)msg.computationValues)
                .reduce((d1, d2) -> d1 + d2)
                .get();

        double delta = newWeight - weights.get(vertex.getNodeId());

        weights.put(vertex.getNodeId(), newWeight);

        if (threshold != null && delta < threshold) {
            return null;
        } else {

            List<StepMsg> outbox = new ArrayList<>();
            double weightToSend =  newWeight / vertex.getEdges().length;

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
                this.returnLabelName,
                new String[]{String.valueOf(this.weights.get(vertex.getNodeId()))}
                ));

        return returnLabels;
    }

    /**
     *
     * @param parameters 0: maxNumberofIterations, 1: convergence threshold (null if you don't want to use it)
     * @param resultLabelsNames 0: Rank label name
     */
    @Override
    public void preInitialize(String[] parameters, String[] resultLabelsNames) {
        this.maxNumOfIterations = Integer.valueOf(parameters[0]);
        String threshold = parameters[1];
        if (threshold.equals("null")){
            this.threshold = null;
        } else {
            this.threshold = Double.parseDouble(threshold);
        }
        this.returnLabelName = resultLabelsNames[0];
    }
}

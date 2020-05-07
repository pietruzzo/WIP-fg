package shared.resources.computationImpl;

import akka.japi.Pair;
import shared.computation.Computation;
import shared.computation.Vertex;
import shared.data.StepMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of PageRank working on DirectedGraphs
 *
 * PageRank  as defined in Pregel: A System for Large-Scale Graph Processing
 * (Grzegorz Malewicz, Matthew H. Austern, Aart J. C. Bik, James C. Dehnert, Ilan Horn,
 * Naty Leiser, and Grzegorz Czajkowski)
 * No convergence condition
 * The sum of Ranks is equal to numberOfVertices if numberVertices parameter isn't specified
 *
 * Parameters:
 *          0: maxIterations (default = 100)
 *          1: dumpingFactor (default = 0.85)
 *          2: numberVertices (default = 1)
 *
 * Return Values :
 *          0: Rank label name
 */
public class PageRank extends Computation {

    private int maxNumOfIterations;
    private Double initialValue;
    private Double dumpingFactor;
    private Double aFactor; // equal to ( 1 - dumpingFactor )

    private HashMap<String, Double> weights;

    @Override
    public List<StepMsg> iterate(Vertex vertex, List<StepMsg> incoming, int iterationStep) {



        //Calculate new weight for current vertex
        double sum = 0;
        for (StepMsg msg : incoming) {
            sum = sum + (Double)msg.computationValues;
        }

        double newWeight = aFactor + dumpingFactor * sum;

        weights.put(vertex.getNodeId(), newWeight );

        //Stop
        if (iterationStep >= maxNumOfIterations){
            voteToHalt();
            return null;
        }

        //Send weight
        List<StepMsg> outbox = new ArrayList<>();
        Double weightToSend = newWeight / vertex.getEdges().length;

        for (String dest: vertex.getEdges()) {
            outbox.add(new StepMsg(dest, vertex.getNodeId(), weightToSend));
        }
        return outbox;

    }

    @Override
    public List<StepMsg> firstIterate(Vertex vertex) {

        List<StepMsg> outbox = new ArrayList<>();
        double weightToSend =  initialValue / vertex.getEdges().length;
        this.weights.put(vertex.getNodeId(), initialValue);

        for (String dest: vertex.getEdges()) {
            outbox.add(new StepMsg(dest, vertex.getNodeId(), weightToSend));
        }
        return outbox;
    }

    @Override
    public List<Pair<String, String[]>> computeResults(Vertex vertex) {

        List<Pair<String, String[]>> returnLabels = new ArrayList<>();

        returnLabels.add(new Pair<>(
                this.computationParameters.returnVarNames().get(0),
                new String[]{String.valueOf(this.weights.get(vertex.getNodeId()))}
                ));

        return returnLabels;
    }

    @Override
    public void preStart() {

        String maxNumOfIterations = computationParameters.getParameter("maxIterations");
        String dumpingFactor = computationParameters.getParameter("dumpingFactor");
        String numberVertices = computationParameters.getParameter("numberVertices");

        try {
            this.maxNumOfIterations = Integer.parseInt(maxNumOfIterations);
        } catch (NullPointerException |NumberFormatException e) {
            this.maxNumOfIterations = 100;
        }

        try {
            this.dumpingFactor = Double.parseDouble(dumpingFactor);
        } catch (NullPointerException |NumberFormatException e) {
            this.dumpingFactor = 0.85;
        }

        try {
            this.initialValue = 1.0 / Integer.parseInt(numberVertices);
        } catch (NullPointerException |NumberFormatException e) {
            this.initialValue = 1.0;
        }

        this.aFactor = 1.0 - this.dumpingFactor;
        this.weights = new HashMap<>();

    }
}

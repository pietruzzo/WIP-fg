package shared.selection;

import akka.japi.Pair;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.Utils;
import shared.VertexM;
import shared.computation.ComputationRuntime;
import shared.computation.Vertex;
import shared.data.SynchronizedIterator;
import shared.variables.solver.VariableSolver;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class Partition {

    private Map<String, String> partition;
    private Map<String, Vertex> partitionVertices;
    private PartitioningSolver partitioningSolver;
    private VariableSolver variableSolver;
    private List<Pair<String, HashMap<String, String[]>>> collectedResultsVertices;
    private List<Tuple3<String, String, HashMap<String, String[]>>> collectedResultsEdges;

    public Partition(ComputationRuntime computationRuntime, PartitioningSolver partitioningSolver, VariableSolver variableSolver) {
        this.partition = computationRuntime.getPartition();
        this.partitionVertices = computationRuntime.getVertices();
        this.partitioningSolver = partitioningSolver;
        this.variableSolver = variableSolver;

        this.collectedResultsVertices = new ArrayList<>();
        this.collectedResultsEdges = new ArrayList<>();
    }

    public void computeSubpartitions() throws ExecutionException, InterruptedException {
        //Per ogni nodo faccio una copia del runtime ed eseguo in parallelo la selezione
        //Execute selection on nodes/edges

        for (Vertex vertex:partitionVertices.values()) {

            if (!partitioningSolver.partitionOnEdge){
                HashMap<String, String[]> result = partitioningSolver.getPartitionsVertex((VertexM)vertex, variableSolver);
                /*
                 * result contains for each vertex a list of values without repetitions
                 * if a vertex doesn't match any value it is cast to value null
                 */
                collectResultsVertices(new Pair<>(vertex.getNodeId(), result));

            } else {
                for (String edge: vertex.getEdges()) {
                    HashMap<String, String[]> result = partitioningSolver.getPartitionsEdge((VertexM)vertex, edge, variableSolver);
                    /*
                     * result contains for each edge a list of values without repetitions
                     * if an edge doesn't match any value it is cast to value null
                     */
                    collectResultsEdges(new Tuple3<>(vertex.getNodeId(), edge, result));
                }

            }

        }
    }

    public List<Pair<String, HashMap<String, String[]>>> getCollectedResultsVertices() {
        return collectedResultsVertices;
    }

    public List<Tuple3<String, String, HashMap<String, String[]>>> getCollectedResultsEdges() {
        return collectedResultsEdges;
    }

    private void collectResultsVertices(Pair<String, HashMap<String, String[]>> singleResult){
        this.collectedResultsVertices.add(singleResult);
    }

    private void collectResultsEdges(Tuple3<String, String, HashMap<String, String[]>> singleResult){
        this.collectedResultsEdges.add(singleResult);
    }



}

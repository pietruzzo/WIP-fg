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
    private ThreadPoolExecutor executors;
    private Map<String, Vertex> partitionVertices;
    private PartitioningSolver partitioningSolver;
    private VariableSolver variableSolver;
    private SynchronizedIterator<Vertex> vertexIterator;
    private List<Pair<String, HashMap<String, String[]>>> collectedResultsVertices;
    private List<Tuple3<String, String, HashMap<String, String[]>>> collectedResultsEdges;

    public Partition(ComputationRuntime computationRuntime, PartitioningSolver partitioningSolver, VariableSolver variableSolver, ThreadPoolExecutor executors) {
        this.partition = computationRuntime.getPartition();
        this.partitionVertices = computationRuntime.getVertices();
        this.partitioningSolver = partitioningSolver;
        this.variableSolver = variableSolver;
        this.executors = executors;
        this.collectedResultsVertices = new ArrayList<>();
        this.collectedResultsEdges = new ArrayList<>();
    }

    public void computeSubpartitions() throws ExecutionException, InterruptedException {
        vertexIterator = new SynchronizedIterator<>(partitionVertices.values().iterator());
        //Per ogni nodo faccio una copia del runtime ed eseguo in parallelo la selezione
        //Execute selection on nodes/edges
        Utils.parallelizeAndWait(executors, new SelectPartition(this));
    }

    public List<Pair<String, HashMap<String, String[]>>> getCollectedResultsVertices() {
        return collectedResultsVertices;
    }

    public List<Tuple3<String, String, HashMap<String, String[]>>> getCollectedResultsEdges() {
        return collectedResultsEdges;
    }

    private synchronized void collectResultsVertices(Pair<String, HashMap<String, String[]>> singleResult){
        this.collectedResultsVertices.add(singleResult);
    }

    private synchronized void collectResultsEdges(Tuple3<String, String, HashMap<String, String[]>> singleResult){
        this.collectedResultsEdges.add(singleResult);
    }



    private static class SelectPartition implements Utils.DuplicableRunnable {

        private final Partition partition;

        public SelectPartition(Partition partition) {
            this.partition = partition;
        }

        @Override
        public void run() {
            try{
                while(true) {
                    VertexM vertex = (VertexM) partition.vertexIterator.next();
                    PartitioningSolver partitioningSolver = partition.partitioningSolver.clone();

                    if (!partitioningSolver.partitionOnEdge){
                        HashMap<String, String[]> result = partitioningSolver.getPartitionsVertex(vertex, partition.variableSolver);
                        if (!result.isEmpty()){
                            boolean valid = true;
                            for (String[] values: result.values()) {
                                if (values.length == 0) {
                                    valid = false;
                                    break;
                                }
                            }
                            partition.collectResultsVertices(new Pair<>(vertex.getNodeId(), result));
                        }

                    } else {
                        for (String edge: vertex.getEdges()) {
                            HashMap<String, String[]> result = partitioningSolver.getPartitionsEdge(vertex, edge, partition.variableSolver);
                            if (!result.isEmpty()){
                                boolean valid = true;
                                for (String[] values: result.values()) {
                                    if (values.length == 0) {
                                        valid = false;
                                        break;
                                    }
                                }
                                partition.collectResultsEdges(new Tuple3<>(vertex.getNodeId(), edge, result));
                            }
                        }

                    }

                }
            } catch (NoSuchElementException e){
                //End of elements
            }
        }

        @Override
        public Utils.DuplicableRunnable getCopy() {
            return new SelectPartition(this.partition);
        }
    }

}

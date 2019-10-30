package shared.selection;

import shared.Utils;
import shared.VertexNew;
import shared.computation.Vertex;
import shared.data.SynchronizedIterator;
import shared.variables.solver.VariableSolver;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


public class SelectPartition { //On the single partition

    protected final SelectionSolver selectionSolver;
    protected final VariableSolver variableSolver;
    private final ThreadPoolExecutor executors;
    protected final SynchronizedIterator<VertexNew> vertexIterator;
    private final HashMap<String, String> partition;
    private final Map<String, Vertex> selectionResult;

    public SelectPartition(SelectionSolver selectionSolver, Iterator<VertexNew> vertexIterator, VariableSolver variableSolverSlave, ThreadPoolExecutor executors, HashMap<String, String> partition) {
        this.selectionSolver = selectionSolver;
        this.vertexIterator = new SynchronizedIterator<>(vertexIterator);
        this.executors = executors;
        this.variableSolver = variableSolverSlave;
        this.partition = partition;
        this.selectionResult = new HashMap<>();
    }

    /**
     * @return null if vertex isn't selected, otherwise vertex with valid edges
     */
    public List<Vertex> performSelection() throws ExecutionException, InterruptedException {
        //Substitute Aggregates
        selectionSolver.solveAggregates(variableSolver);

        //Execute selection on nodes/edges
        Utils.parallelizeAndWait(executors, new SelectNode(this));

        //Prune edges pointing to nodes not in partition -> todo this can be done only after asking other taskmanagers
        List<Vertex> result = new ArrayList<>(selectionResult.values());
        SynchronizedIterator<Vertex> synchronizedIterator = new SynchronizedIterator<>(result.iterator());
        Utils.parallelizeAndWait(executors, new PruneVertices(synchronizedIterator, selectionResult));

        //Return selected graph
        return result;
    }

    protected synchronized void registerVertex(Vertex vertex){
        if (vertex != null)
            selectionResult.put(vertex.getNodeId(), vertex);
    }

    private static class SelectNode implements Utils.DuplicableRunnable {

        private final SelectPartition selectPartition;

        public SelectNode(SelectPartition selectPartition) {
            this.selectPartition = selectPartition;
        }

        @Override
        public void run() {
            try{
                while(true) {
                    VertexNew vertex = selectPartition.vertexIterator.next();
                    SelectionSolver selectionSolver = selectPartition.selectionSolver.clone();
                    Vertex result = selectionSolver.solveVertex(vertex, selectPartition.variableSolver);
                    selectPartition.registerVertex(result);
                }
            } catch (NoSuchElementException e){
                //End of elements
            }
        }

        @Override
        public Utils.DuplicableRunnable getCopy() {
            return new SelectNode(this.selectPartition);
        }
    }


    private static class PruneVertices implements Utils.DuplicableRunnable {

        private final SynchronizedIterator<Vertex> vertexIterator;
        private final Map<String, Vertex> vertices;

        public PruneVertices(SynchronizedIterator<Vertex> vertexIterator, Map<String, Vertex> vertices) {
            this.vertexIterator = vertexIterator;
            this.vertices = vertices;
        }

        @Override
        public Utils.DuplicableRunnable getCopy() {
            return new PruneVertices(this.vertexIterator, this.vertices);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Vertex vertex = vertexIterator.next();
                    for (String edge : vertex.getEdges()) {
                        if (!vertices.containsKey(edge)) {
                            ((VertexNew)vertex).deleteEdge(edge);
                        }
                    }
                }
            } catch(NoSuchElementException e){
                    //End of elements
                }
        }
    }
}

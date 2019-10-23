package shared.selection;

import shared.Utils;
import shared.VertexNew;
import shared.computation.Vertex;
import shared.data.SynchronizedIterator;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


public class SelectPartition { //TODO handle multiple selections and parallelize them

    protected final SelectionSolver selectionSolver;
    protected final VariableSolverSlave variableSolver;
    private final ThreadPoolExecutor executors;
    protected final SynchronizedIterator<VertexNew> vertexIterator;
    private final HashMap<String, String> partition;
    private final List<Vertex> selectionResult;

    public SelectPartition(SelectionSolver selectionSolver, Iterator<VertexNew> vertexIterator, VariableSolverSlave variableSolverSlave, ThreadPoolExecutor executors, HashMap<String, String> partition) {
        this.selectionSolver = selectionSolver;
        this.vertexIterator = new SynchronizedIterator<>(vertexIterator);
        this.executors = executors;
        this.variableSolver = variableSolverSlave;
        this.partition = partition;
        this.selectionResult = new ArrayList<>();
    }

    /**
     * @ApiNote Requires aggregates are substituted
     * @return null if vertex isn't selected, otherwise vertex with valid edges
     * TODO: HANDLE PARTITIONING!
     */
    public List<Vertex> performSelection() throws ExecutionException, InterruptedException {
        Utils.parallelizeAndWait(executors, new SelectNode(this));
        return selectionResult;
    }

    protected synchronized void registerVertex(Vertex vertex){
        if (vertex != null)
            selectionResult.add(vertex);
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
}

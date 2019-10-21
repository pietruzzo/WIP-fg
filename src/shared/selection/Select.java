package shared.selection;

import akka.stream.javadsl.RunnableGraph;
import shared.Utils;
import shared.VertexNew;
import shared.computation.Vertex;
import shared.data.SynchronizedIterator;
import shared.variables.solver.VariableSolverSlave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


public class Select { //TODO handle multiple selections and parallelize them

    protected final SelectionSolver selectionSolver;
    protected final VariableSolverSlave variableSolver;
    protected final ThreadPoolExecutor executors;
    protected final SynchronizedIterator<VertexNew> vertexIterator;
    private final List<Vertex> selectionResult;

    public Select(SelectionSolver selectionSolver, Iterator<VertexNew> vertexIterator, VariableSolverSlave variableSolverSlave, ThreadPoolExecutor executors) {
        this.selectionSolver = selectionSolver;
        this.vertexIterator = new SynchronizedIterator<>(vertexIterator);
        this.executors = executors;
        this.variableSolver = variableSolverSlave;
        this.selectionResult = new ArrayList<>();
    }

    /**
     * @ApiNote requires selectionSolver.getVariables(true, true).isEmpty
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

    private static class SelectNode implements Runnable {

        private final Select select;

        public SelectNode(Select select) {
            this.select = select;
        }

        @Override
        public void run() {
            try{
                while(true) {
                    VertexNew vertex = select.vertexIterator.next();
                    SelectionSolver selectionSolver = select.selectionSolver.clone();
                    Vertex result = selectionSolver.solveVertex(vertex, select.variableSolver);
                    select.registerVertex(result);
                }
            } catch (NoSuchElementException e){
                //End of elements
            }
        }
    }

}

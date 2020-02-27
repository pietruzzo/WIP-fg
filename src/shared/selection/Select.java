package shared.selection;

import shared.VertexM;
import shared.computation.Vertex;
import shared.variables.solver.VariableSolver;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class Select { //On the single partition

    protected final SelectionSolver selectionSolver;
    protected final VariableSolver variableSolver;
    protected final Collection<VertexM> vertexIterator;
    private final HashMap<String, String> partition;
    private final Map<String, Vertex> selectionResult;

    public Select(SelectionSolver selectionSolver, Collection<VertexM> vertices, VariableSolver variableSolverSlave, HashMap<String, String> partition) {
        this.selectionSolver = selectionSolver.clone();
        this.vertexIterator = vertices;
        this.variableSolver = variableSolverSlave;
        this.partition = partition;
        this.selectionResult = new HashMap<>();
    }

    /**
     * @return null if vertex isn't selected, otherwise vertex with valid edges
     */
    public Map<String, Vertex> performSelection() throws ExecutionException, InterruptedException {
        //Substitute Aggregates
        selectionSolver.solveAggregates(variableSolver);

        //Execute selection on nodes/edges
        //Utils.parallelizeAndWait(executors, new SelectNode(this));
        for (VertexM vertex: this.vertexIterator) {
            SelectionSolver selectionSolver = this.selectionSolver.clone();
            Vertex result = selectionSolver.solveVertex(vertex, variableSolver);
            registerVertex(result);
        }


        //Prune edges pointing to nodes not in selection
        List<Vertex> result = new ArrayList<>(selectionResult.values());

        for (Vertex vertex : result) {
            for (String edge : vertex.getEdges()) {
                if (!selectionResult.containsKey(edge)) {
                    ((VertexM)vertex).deleteEdge(edge);
                }
            }
        }


        //Return selected graph
        return result.stream().collect(Collectors.toMap(vertex -> vertex.getNodeId(), vertex -> vertex));
    }

    protected void registerVertex(Vertex vertex){
        if (vertex != null)
            selectionResult.put(vertex.getNodeId(), vertex);
    }
}

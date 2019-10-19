package shared.selection;

import shared.VertexNew;
import shared.computation.Vertex;
import shared.variables.VariableSolver;


public class Select {

    private final SelectionSolver selectionSolver;
    private final VertexNew vertex;
    private boolean vertexSolved;

    public Select(SelectionSolver selectionSolver, VertexNew vertex, VariableSolver variableSolver) {
        this.selectionSolver = selectionSolver;
        this.vertex = vertex;
        this.vertexSolved = false;
    }

    /**
     * @ApiNote requires selectionSolver.getVariables(true, true).isEmpty
     * @return null if vertex isn't selected, otherwise vertex with valid edges
     */
    public Vertex selectVertex() {
        //TODO needs implementation
        //Select on vertex
        selectionSolver.solveVertex(vertex);
        //Select on edges
        /*

            Labels are stored on Vertex State and Edge State
            Aggregate variables have been substituted by master
            thus only non-aggregate variables needs substitution on task manager

            Variables can be retrieved one-shot and registered for both vertices and edges

         */

        // select vertex
        // generate one selectionSolver for each Edge
        // solve edges and collect them
        return null;
    }

}

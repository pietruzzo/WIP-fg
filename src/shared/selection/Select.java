package shared.selection;

import shared.VertexNew;
import shared.computation.Vertex;


public class Select {

    private final SelectionSolver selectionSolver;
    private final VertexNew vertex;
    private boolean vertexSolved;

    public Select(SelectionSolver selectionSolver, VertexNew vertex) {
        this.selectionSolver = selectionSolver;
        this.vertex = vertex;
        this.vertexSolved = false;
    }

    public Vertex performSelection() {
        //Select on vertex
        //Select on edges
        /*

         */
    }

}

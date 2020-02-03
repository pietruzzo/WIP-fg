package shared.selection;

import org.apache.flink.api.java.tuple.Tuple4;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectionSolverTest {



    @BeforeEach
    void setUp() {
    }

    @Test
    void getPartition() {

        SelectionSolver selectionSolver = new SelectionSolver();

        HashMap<String, String> partition = new HashMap();
        selectionSolver.setPartition(partition);
        assertEquals(selectionSolver.getPartition().get("all"), "all");
    }


    @Test
    void solveAggregates() {
        SelectionSolver selectionSolver = new SelectionSolver();

    }

    @Test
    void solveVertex() {
    }

    @Test
    void getVariables() {
    }

    @Test
    void getLabels() {
    }

    @Test
    void substituteLabels() {
    }

    @Test
    void substituteVariables() {
        Tuple4<String, String, SelectionSolver.Operation.WindowType, String[][]> values;
    }

    @Test
    void testClone() {
    }
}
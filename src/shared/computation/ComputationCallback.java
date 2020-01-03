package shared.computation;

import org.jetbrains.annotations.Nullable;
import shared.variables.solver.VariableSolver;

import java.util.Map;

public interface ComputationCallback {

    void registerComputationResult(String vertexName, String variableName, String[] values, @Nullable Map<String, String> partition);

    VariableSolver getVariableSolver();
}

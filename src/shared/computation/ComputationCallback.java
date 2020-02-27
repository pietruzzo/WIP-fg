package shared.computation;

import akka.actor.ActorRef;
import org.apache.flink.api.java.tuple.Tuple2;
import org.jetbrains.annotations.Nullable;
import shared.variables.solver.VariableSolver;

import java.util.Map;

public interface ComputationCallback {

    void registerComputationResult(String vertexName, Tuple2<String, Long> variableName, String[] values, @Nullable Map<String, String> partition);

    Map<Integer, ActorRef> getActors ();

    ActorRef getActor (String name);
}

package shared.computation;

public interface ComputationCallback {

    void updateState(String vertexName, String key, String[] values);
}

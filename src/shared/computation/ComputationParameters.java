package shared.computation;

import java.util.List;
import java.util.Map;

public interface ComputationParameters {

    /**
     * @param paramName name of parameter
     * @return single value parameter of type "value"
     */
    String getParameter (String paramName);

    List<String> returnVarNames ();

    /**
     *
     * @param paramName
     * @param vertex
     * @return all associated values to paramName, vertex pairs
     */
    String[] getParameter (String paramName, Vertex vertex);

    /**
     *
     * @param paramName
     * @param vertex
     * @return all associated values, for each edge, to paramName, vertex pairs
     */
    Map<String, String[]> getEdgeParameter (String paramName, Vertex vertex);
}

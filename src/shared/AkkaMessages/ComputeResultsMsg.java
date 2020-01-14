package shared.AkkaMessages;

import org.apache.flink.api.java.tuple.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ComputeResultsMsg implements Serializable {

    private static final long serialVersionUID = 200020L;

    private final LinkedHashMap<String, String> freeVars;
    private final List<Tuple2<String, Long>> returnVarNames;

    public ComputeResultsMsg(LinkedHashMap<String, String> freeVars, List<Tuple2<String, Long>> returnVarNames) {
        this.freeVars = freeVars;
        this.returnVarNames = returnVarNames;
    }


    /**
     * @return null if runs over all Free Variables
     */
    public LinkedHashMap<String, String> getFreeVars() {
        return freeVars;
    }

    public List<Tuple2<String, Long>> getReturnVarNames() {
        return returnVarNames;
    }
}
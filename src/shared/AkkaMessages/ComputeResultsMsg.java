package shared.AkkaMessages;

import org.apache.flink.api.java.tuple.Tuple2;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class ComputeResultsMsg implements Serializable {

    private static final long serialVersionUID = 200020L;

    private final LinkedHashMap<String, String> freeVars;

    public ComputeResultsMsg(LinkedHashMap<String, String> freeVars) {
        this.freeVars = freeVars;
    }


    /**
     * @return null if runs over all Free Variables
     */
    public LinkedHashMap<String, String> getFreeVars() {
        return freeVars;
    }

}
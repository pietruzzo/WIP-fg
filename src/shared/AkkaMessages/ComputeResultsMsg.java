package shared.AkkaMessages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ComputeResultsMsg implements Serializable {

    private static final long serialVersionUID = 200020L;

    private final LinkedHashMap<String, String> freeVars;
    private final List<String> returnVarNames;

    public ComputeResultsMsg(LinkedHashMap<String, String> freeVars, List<String> returnVarNames) {
        this.freeVars = freeVars;
        this.returnVarNames = returnVarNames;
    }


    /**
     * @return null if runs over all Free Variables
     */
    public LinkedHashMap<String, String> getFreeVars() {
        return freeVars;
    }

    public List<String> getReturnVarNames() {
        return returnVarNames;
    }
}
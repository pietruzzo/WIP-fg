package shared.AkkaMessages;

import java.io.Serializable;
import java.util.LinkedHashMap;

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
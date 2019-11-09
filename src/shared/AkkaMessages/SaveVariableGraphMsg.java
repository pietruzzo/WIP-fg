package shared.AkkaMessages;

import java.io.Serializable;

public class SaveVariableGraphMsg implements Serializable {

    private static final long serialVersionUID = 200053L;

    private final long timewindow;
    private final String varableName;

    public SaveVariableGraphMsg(long timewindow, String varableName) {
        this.timewindow = timewindow;
        this.varableName = varableName;
    }


    public long getTimewindow() {
        return timewindow;
    }

    public String getVarableName() {
        return varableName;
    }
}

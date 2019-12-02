package shared.patterns;

import master.PatternCallback;
import org.jetbrains.annotations.Nullable;
import shared.AkkaMessages.NewPartitionMsg;
import shared.AkkaMessages.RestoreVariableGraphMsg;
import shared.AkkaMessages.SaveVariableGraphMsg;

import java.io.Serializable;

public class GraphSaver extends Pattern {

    boolean store;
    String varName;
    String timeAgo;
    long survivalTime;

    public GraphSaver(Trigger trigger, @Nullable String variablesToBeGenerated, PatternCallback transportLayer) {
        super(trigger, variablesToBeGenerated, transportLayer);
    }

    public void setStore(String varName, long survivalTime) {
        this.store = true;
        this.varName = varName;
        this.survivalTime = survivalTime;
    }

    public void setRetrieve(String varName, String timeAgo) {
        this.store = false;
        this.varName = varName;
        this.timeAgo = timeAgo;
    }

    public void setResetPartitioning() {
        this.store = false;
        this.varName = null;
    }

    @Override
    boolean startPatternLogic() {
        if (store) {
            transportLayer.sendToAllSlaves(new SaveVariableGraphMsg(survivalTime, varName));
        } else {
            transportLayer.sendToAllSlaves(new RestoreVariableGraphMsg(varName, timeAgo));
        }
        transportLayer.becomeAwaitAckFromAll();
        return false;
    }

    @Override
    public boolean processMessage(@Nullable Serializable message) {
        return true;
    }
}

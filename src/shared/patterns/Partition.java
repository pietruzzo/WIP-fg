package shared.patterns;

import jdk.internal.jline.internal.Nullable;
import master.PatternCallback;
import shared.AkkaMessages.NewPartitionMsg;
import shared.selection.PartitioningSolver;

import java.io.Serializable;

public class Partition extends Pattern{

    private PartitioningSolver partitioningSolver;

    public Partition(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer, PartitioningSolver partitioningSolver) {
        super(trigger, variablesToBeGenerated, transportLayer);
        this.partitioningSolver = partitioningSolver;
    }


    @Override
    boolean startPatternLogic() {
        transportLayer.sendToAllSlaves(new NewPartitionMsg(this.partitioningSolver));
        transportLayer.setNextStateIterativeComputationState();
        transportLayer.becomeAwaitAckFromAll();
        return false;
    }

    @Override
    boolean processMessage(@Nullable Serializable message) {
        //After Acks return true
        return false;
    }
}

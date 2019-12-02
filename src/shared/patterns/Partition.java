package shared.patterns;

import org.jetbrains.annotations.Nullable;
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
        transportLayer.becomeAwaitAckFromAll();
        return false;
    }

    @Override
    public boolean processMessage(@Nullable Serializable message) {
        //After Acks return true
        return true;
    }
}

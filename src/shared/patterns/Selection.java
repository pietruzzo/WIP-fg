package shared.patterns;

import jdk.internal.jline.internal.Nullable;
import master.PatternCallback;
import shared.AkkaMessages.select.SelectMsg;
import shared.selection.SelectionSolver;

import java.io.Serializable;

public class Selection extends Pattern{

    private SelectionSolver selectionSolver;


    public Selection(Trigger trigger, String variablesToBeGenerated, PatternCallback transportLayer, SelectionSolver selectionSolver) {
        super(trigger, variablesToBeGenerated, transportLayer);
        this.selectionSolver = selectionSolver;
    }


    @Override
    boolean startPatternLogic() {
        transportLayer.sendToAllSlaves(new SelectMsg(this.selectionSolver));
        transportLayer.setNextStateIterativeComputationState();
        transportLayer.becomeAwaitAckFromAll();
        return false;
    }

    @Override
    boolean processMessage(@Nullable Serializable message) {
        //After acks
        return true;
    }
}

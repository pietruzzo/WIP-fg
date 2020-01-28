package shared.patterns;

import org.jetbrains.annotations.Nullable;
import master.PatternCallback;
import shared.AkkaMessages.select.SelectMsg;
import shared.PropertyHandler;
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
        PropertyHandler.writeOnPerformanceLog("ENTERING_SELECTION_"+ System.currentTimeMillis());
        transportLayer.sendToAllSlaves(new SelectMsg(this.selectionSolver));
        transportLayer.becomeAwaitAckFromAll();
        return false;
    }

    @Override
    public boolean processMessage(@Nullable Serializable message) {
        //After acks
        PropertyHandler.writeOnPerformanceLog("EXITING_SELECTION_"+ System.currentTimeMillis());
        return true;
    }
}

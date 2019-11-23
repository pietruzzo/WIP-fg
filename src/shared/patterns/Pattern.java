package shared.patterns;

import jdk.internal.jline.internal.Nullable;
import master.PatternCallback;

import java.io.Serializable;
import java.util.HashSet;

public abstract class Pattern {

    private Trigger trigger;

    private String variablesToBeGenerated;

    protected PatternCallback transportLayer;



    public Pattern (Trigger trigger, @Nullable String variablesToBeGenerated, PatternCallback transportLayer) {
        this.trigger = trigger;
        this.variablesToBeGenerated = variablesToBeGenerated;
        this.transportLayer = transportLayer;
    }


    /**
     * Apply action
     * @param currentTimestamp
     * @param triggerEvent
     * @param validVariables
     * @return true if it has finished or it haven't to run
     */
    public boolean applyIfTriggered(long currentTimestamp, Trigger.TriggerEnum triggerEvent, HashSet<String> validVariables) {

        boolean isFinished = true;

        if (trigger.isTriggered(currentTimestamp, triggerEvent, validVariables)) {

            isFinished = startPatternLogic();

            if (isFinished) {
                generateVariable(validVariables);
            }

        }

        return isFinished;
    }

    private void generateVariable (HashSet<String> validVariables) {

        if (this.variablesToBeGenerated != null) {
            validVariables.add(variablesToBeGenerated);
        }

    }



    /**
     *
     * @return true if this element has finished execution
     */
    abstract boolean startPatternLogic();

    abstract boolean processMessage(Serializable message);

}

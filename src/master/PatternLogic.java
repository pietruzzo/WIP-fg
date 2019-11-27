package master;

import org.jetbrains.annotations.Nullable;
import shared.patterns.Pattern;
import shared.patterns.Trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class PatternLogic {

    private List<Pattern> patternElements;
    private Iterator<Pattern> currentPattern;
    private Pattern currentElement;
    private PatternCallback transportLayer;

    private long currentTimestamp;
    private Trigger.TriggerEnum triggerEvent;
    private HashSet<String> validVariable;

    public PatternLogic(PatternCallback transportLayer) {
        this.transportLayer = transportLayer;
        this.patternElements = new ArrayList<>();
    }

    public void installPattern (List<Pattern> patternElements) {
        this.patternElements = patternElements;
    }

    public void startNewIteration (long currentTimestamp,
                                   Trigger.TriggerEnum triggerEvent,
                                   HashSet<String> validVariables) {

        this.currentTimestamp = currentTimestamp;
        this.triggerEvent = triggerEvent;
        this.validVariable = validVariables;

        currentPattern = patternElements.iterator();


    }

    /**
     * Run current element or take the following if finished
     * @return true if pattern execution has finished
     */
    public void runElement (@Nullable Serializable message) {

        if (currentElement.processMessage(message)) {

            if (currentPattern.hasNext()) {
                currentElement = currentPattern.next();
                currentElement.applyIfTriggered(currentTimestamp, triggerEvent, validVariable);
            } else {
                transportLayer.becomeReceiveChangeState();
            }
        }
    }

}

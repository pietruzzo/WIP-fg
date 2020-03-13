package master;

import org.jetbrains.annotations.Nullable;
import shared.AkkaMessages.NewTimestampMsg;
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
        this.currentElement = null;
        currentPattern = patternElements.iterator();

        transportLayer.sendToAllSlaves(new NewTimestampMsg(currentTimestamp));
        transportLayer.becomeAwaitAckFromAll();


    }

    /**
     * Run current element or take the following if finished
     * If pattern is finished, change receive state
     */
    public void runElement (@Nullable Serializable message) {

        if (patternElements.isEmpty()) {
            //No installed pattern
            transportLayer.becomeReceiveChangeState();
            return;
        } else if (currentElement == null) {
            //First step
            applyNext();

        } else if (currentElement.processMessage(message)) {

            applyNext();
        }
    }

    private void applyNext() {
        if (currentPattern.hasNext()) {
            currentElement = currentPattern.next();
            while (currentElement.applyIfTriggered(currentTimestamp, triggerEvent, validVariable)) {
                if (currentPattern.hasNext()) {
                    currentElement = currentPattern.next();
                } else {
                    transportLayer.becomeReceiveChangeState();
                    break;
                }
            }
        } else {
            transportLayer.becomeReceiveChangeState();
        }
    }

}

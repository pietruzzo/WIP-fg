package shared.patterns;


import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class Trigger {

    public enum TriggerEnum {
        ALL,
        VERTEX_ADDITION,
        VERTEX_DELETION,
        VERTEX_UPDATE,
        EDGE_ADDITION,
        EDGE_DELETION,
        EDGE_UPDATE,
        TRIGGER_TEMPORAL;
        //TRIGGER_SENSISTIVITY; -> Sensitivity variables

    }

    private TriggerEnum triggerType;
    private List<String> sensitivityVariables;

    //To handle TRIGGER_TEMPORAL
    private long everyEach;
    private long lastTriggered;

    public Trigger(TriggerEnum triggerType, @Nullable List<String> sensitivityVariables) {
        this.triggerType = triggerType;
        this.sensitivityVariables = sensitivityVariables;
    }

    public Trigger(long everyms, @Nullable List<String> sensitivityVariables) {
        this.triggerType = TriggerEnum.TRIGGER_TEMPORAL;
        this.sensitivityVariables = sensitivityVariables;
        this.everyEach = everyms;
        this.lastTriggered = 0; //Trigger at first run
    }


    public boolean isTriggered(long currentTimestamp, TriggerEnum triggerEvent, Set<String> validSensitivityVariables) {

        if ( ! checkSensitivity(validSensitivityVariables) ) {
            return false;
        }

        switch (this.triggerType){
            case ALL:
                return true;
            case VERTEX_ADDITION:
                return isTriggered_VERTEX_ADDITION(triggerEvent);
            case VERTEX_DELETION:
                return isTriggered_VERTEX_DELETION(triggerEvent);
            case VERTEX_UPDATE:
                return isTriggered_VERTEX_UPDATE(triggerEvent);
            case EDGE_ADDITION:
                return isTriggered_EDGE_ADDITION(triggerEvent);
            case EDGE_DELETION:
                return isTriggered_EDGE_DELETION(triggerEvent);
            case EDGE_UPDATE:
                return isTriggered_EDGE_UPDATE(triggerEvent);
            case TRIGGER_TEMPORAL:
                return isTriggered_TRIGGER_TEMPORAL(currentTimestamp);
            default:
                throw new RuntimeException("Unrecognized Token in trigger");

        }

    }

    private boolean checkSensitivity(Set<String> validSensitivity) {

        if (this.sensitivityVariables == null || this.sensitivityVariables.isEmpty()) {
            return true;
        }

        return validSensitivity.containsAll(this.sensitivityVariables);

    }

    private boolean isTriggered_VERTEX_ADDITION (TriggerEnum triggerEvent) {

        return ( this.triggerType.equals(triggerEvent) && triggerEvent.equals(TriggerEnum.VERTEX_ADDITION));

    }

    private boolean isTriggered_VERTEX_DELETION (TriggerEnum triggerEvent) {

        return ( this.triggerType.equals(triggerEvent) && triggerEvent.equals(TriggerEnum.VERTEX_DELETION) );

    }

    private boolean isTriggered_VERTEX_UPDATE (TriggerEnum triggerEvent) {

        return ( this.triggerType.equals(triggerEvent) && triggerEvent.equals(TriggerEnum.VERTEX_UPDATE) );

    }

    private boolean isTriggered_EDGE_ADDITION (TriggerEnum triggerEvent) {

        return ( this.triggerType.equals(triggerEvent) && triggerEvent.equals(TriggerEnum.EDGE_ADDITION) );

    }

    private boolean isTriggered_EDGE_DELETION (TriggerEnum triggerEvent) {

        return ( this.triggerType.equals(triggerEvent) && triggerEvent.equals(TriggerEnum.EDGE_DELETION) );

    }

    private boolean isTriggered_EDGE_UPDATE (TriggerEnum triggerEvent) {

        return ( this.triggerType.equals(triggerEvent) && triggerEvent.equals(TriggerEnum.EDGE_UPDATE) );

    }

    private boolean isTriggered_TRIGGER_TEMPORAL (long currentTimestamp) {

        boolean trigger = (currentTimestamp - lastTriggered) <= everyEach ;

        if (trigger) {
            this.lastTriggered = currentTimestamp;
        }

        return trigger;
    }


}

package shared.data;

import shared.AkkaMessages.StepMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class BoxMsg implements Serializable {
    private static final long serialVersionUID = 200015L;

    private final ArrayList<StepMessage> messages;

    public BoxMsg() {
        this.messages = new ArrayList<>();
    }

    public SynchronizedIterator<StepMessage> getSyncIterator(){
        return new SynchronizedIterator(messages.iterator());
    }
}

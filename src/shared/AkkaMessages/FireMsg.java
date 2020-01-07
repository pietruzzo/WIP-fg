package shared.AkkaMessages;

import java.io.Serializable;

public class FireMsg implements Serializable {

    private static final long serialVersionUID = 200059L;

    private final String event;

    public FireMsg(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return this.event;
    }
}

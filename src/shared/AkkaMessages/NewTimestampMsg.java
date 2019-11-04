package shared.AkkaMessages;

import java.io.Serializable;

public class NewTimestampMsg implements Serializable {

    private static final long serialVersionUID = 200052L;

    public final long timestamp;


    public NewTimestampMsg(long timestamp) {
        this.timestamp = timestamp;
    }
}

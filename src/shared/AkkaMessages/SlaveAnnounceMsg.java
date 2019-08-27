package shared.AkkaMessages;

import java.io.Serializable;

public class SlaveAnnounceMsg implements Serializable {
    private static final long serialVersionUID = 200005L;

    public final String slaveName;
    public final int numThreads;


    public SlaveAnnounceMsg(String slaveName, int numThreads) {
        this.slaveName = slaveName;
        this.numThreads = numThreads;
    }

    @Override
    public String toString(){
        return "New Slave: " + slaveName +" with " + numThreads + " threads.";
    }
}

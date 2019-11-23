package master;

import java.io.Serializable;

public interface PatternCallback {

    void fireEvent (String event);

    <Msg extends Serializable> void sendToAllSlaves(Msg message);

    long getCurrentTimestamp();

    void setNextStateIterativeComputationState ();

    void becomeAwaitAckFromAll();

    int getNumSlaves();
}

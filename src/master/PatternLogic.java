package master;

import java.io.Serializable;

public interface PatternLogic {

    void sendToCurrentPattern(Serializable message);
}

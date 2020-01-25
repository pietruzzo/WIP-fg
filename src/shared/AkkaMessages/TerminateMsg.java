package shared.AkkaMessages;

import java.io.Serializable;

public class TerminateMsg implements Serializable {

    private static final long serialVersionUID = 200063L;

    @Override
    public String toString() {
        return "TerminateMsg: no more inputs";
    }
}

package shared.AkkaMessages;

import shared.data.MultiKeyMap;

import java.io.Serializable;
import java.util.Set;

public class InvalidNodesMsg implements Serializable {

    private static final long serialVersionUID = 200051L;

    public final MultiKeyMap<Set<String>> invalidNodes;

    public InvalidNodesMsg(MultiKeyMap<Set<String>> nodesToValidate) {
        this.invalidNodes = nodesToValidate;
    }
}

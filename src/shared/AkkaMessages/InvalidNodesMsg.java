package shared.AkkaMessages;

import shared.data.MultiKeyMap;

import java.util.Set;

public class InvalidNodesMsg {

    private static final long serialVersionUID = 200051L;

    public final MultiKeyMap<Set<String>> invalidNodes;

    public InvalidNodesMsg(MultiKeyMap<Set<String>> nodesToValidate) {
        this.invalidNodes = nodesToValidate;
    }
}

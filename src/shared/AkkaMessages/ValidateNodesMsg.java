package shared.AkkaMessages;

import shared.data.MultiKeyMap;

import java.io.Serializable;
import java.util.Set;

public class ValidateNodesMsg implements Serializable {

    private static final long serialVersionUID = 200050L;

    public final MultiKeyMap<Set<String>> nodesToValidate;

    public ValidateNodesMsg(MultiKeyMap<Set<String>> nodesToValidate) {
        this.nodesToValidate = nodesToValidate;
    }
}

package shared.AkkaMessages;

import org.jetbrains.annotations.Nullable;
import shared.data.MultiKeyMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class ValidateNodesMsg implements Serializable {

    private static final long serialVersionUID = 200050L;

    public final MultiKeyMap<Set<String>> nodesToValidate;

    /**
     * If null on all partitions
     */
    public final HashMap<String, String> partition;

    public ValidateNodesMsg(MultiKeyMap<Set<String>> nodesToValidate, @Nullable HashMap<String, String> partition) {
        this.nodesToValidate = nodesToValidate;
        this.partition = partition;
    }
}

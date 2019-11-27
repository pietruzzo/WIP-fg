package shared.AkkaMessages.select;

import shared.data.MultiKeyMap;

import java.io.Serializable;
import java.util.HashMap;

public class selectNodeMsg implements Serializable {
    private static final long serialVersionUID = 200049L;

    public final long select_id;
    public final MultiKeyMap<HashMap<String, Boolean>> selected;

    public selectNodeMsg(long select_id, MultiKeyMap<HashMap<String, Boolean>> selected) {
        this.select_id = select_id;
        this.selected = selected;
    }
}

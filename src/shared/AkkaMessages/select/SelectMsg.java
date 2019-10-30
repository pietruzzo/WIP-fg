package shared.AkkaMessages.select;

import shared.selection.Selection;

import java.io.Serializable;

public class SelectMsg implements Serializable {
    private static final long serialVersionUID = 200048L;

    public final Selection operations;

    public SelectMsg(Selection operations) {
        this.operations = operations;
    }
}

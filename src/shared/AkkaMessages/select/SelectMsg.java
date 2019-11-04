package shared.AkkaMessages.select;

import shared.selection.SelectionSolver;

import java.io.Serializable;

public class SelectMsg implements Serializable {
    private static final long serialVersionUID = 200048L;

    public final SelectionSolver operations;

    public SelectMsg(SelectionSolver operations) {
        this.operations = operations;
    }
}

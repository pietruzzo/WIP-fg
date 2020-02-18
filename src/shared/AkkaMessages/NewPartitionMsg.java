package shared.AkkaMessages;

import shared.selection.PartitioningSolver;

import java.io.Serializable;

public class NewPartitionMsg implements Serializable {
    private static final long serialVersionUID = 200045L;

    private final PartitioningSolver partitioningSolver;

    public NewPartitionMsg(PartitioningSolver partitioningSolver) {
        this.partitioningSolver = partitioningSolver;
    }

    public PartitioningSolver getPartitioningSolver() {
        return partitioningSolver;
    }

    @Override
    public String toString() {
        return "NewPartitionMsg{" +
                "partitioningSolver=" + partitioningSolver +
                '}';
    }
}

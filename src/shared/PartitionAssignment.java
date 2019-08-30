package shared;

public class PartitionAssignment {

    public static int getPartition(String name, int numPartitions) {
        return name.hashCode() % numPartitions;
    }
}

package shared;

import akka.actor.ActorRef;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void getPartition() {

        String[] elements = { "0", "1", "2", "3", "4", "5"};
        int num_partitions = 4;

        assertEquals(Utils.getPartition(elements[0], num_partitions) , 0);
        assertEquals(Utils.getPartition(elements[1], num_partitions) , 1);
        assertEquals(Utils.getPartition(elements[2], num_partitions) , 2);
        assertEquals(Utils.getPartition(elements[3], num_partitions) , 3);
        assertEquals(Utils.getPartition(elements[4], num_partitions) , 0);
        assertEquals(Utils.getPartition(elements[5], num_partitions) , 1);
    }

    @Test
    void solveTime() {

        assertEquals(Utils.solveTime("23m2h12s"), 8592000);
        assertEquals(Utils.solveTime("23m2h12s1"), 8592001);
        assertEquals(Utils.solveTime("23m12s2h1"), 8592001);

    }
}
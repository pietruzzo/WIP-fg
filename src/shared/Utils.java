package shared;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Utils {

    public static int getPartition(String name, int numPartitions) {
        return name.hashCode() % numPartitions;
    }

    public static void parallelizeAndWait (ThreadPoolExecutor executors, Runnable task) throws ExecutionException, InterruptedException {

        Collection<Future> executions = new LinkedList<>();

        //Parallel execution
        for (int i = 0; i < executors.getMaximumPoolSize(); i++) {
            executions.add(executors.submit(task));
        }

        //Wait executors end
        for (Future<?> future: executions) {
            future.get();
        }
    }
}

package shared;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Utils {

    /**
     * Support seconds (s), minutes (m), hours (h)
     */
    public static final String TIME_DELIMETER_REGEX = "(smh)";

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

    /**
     * Convert time from string to millis according to convention
     * es: 23m2h12s -> 8592000
     * @return
     */
    public static long solveTime(String time){
        String[] tokens = time.split(TIME_DELIMETER_REGEX);
        int tokenIndex = 0;
        long result = 0;

        for (char c: time.toCharArray()) {
            if (c == 's'){
                result = result + (Long.parseLong(tokens[tokenIndex])*1000);
                tokenIndex = tokenIndex +1;
            }else if (c == 'm'){
                result = result + (Long.parseLong(tokens[tokenIndex])*60000);
                tokenIndex = tokenIndex +1;
            }else if (c == 'h'){
                result = result + (Long.parseLong(tokens[tokenIndex])*3600000);
                tokenIndex = tokenIndex +1;
            }
        }
        return result;
    }
}

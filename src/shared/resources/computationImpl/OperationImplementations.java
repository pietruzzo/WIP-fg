package shared.resources.computationImpl;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.streamProcessing.CustomBinaryOperator;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class OperationImplementations {

    private static Map<String, Object> streamFunctions;

    public static Function<Tuple, Tuple> getMap(String name){
        if (streamFunctions == null) initialize();
        return (Function<Tuple, Tuple>) streamFunctions.get(name);

    }

    /**
     *
     * @param name
     * @return field0 = Operator, field1 = Identity
     */
    public static Tuple2<CustomBinaryOperator, Tuple> getReduce(String name){
        if (streamFunctions == null) initialize();
        return (Tuple2<CustomBinaryOperator, Tuple>) streamFunctions.get(name);

    }

    public static Function<Tuple, Stream<Tuple>> getFlatMap(String name){
        if (streamFunctions == null) initialize();
        return (Function<Tuple, Stream<Tuple>>) streamFunctions.get(name);

    }

    public static Predicate<Tuple> getFilter(String name){
        if (streamFunctions == null) initialize();
        return (Predicate<Tuple>) streamFunctions.get(name);

    }

    private static void initialize () {
        //Put here new Operations TODO max, min, avg, select ...
    }

}

package shared.streamProcessing;

import shared.resources.operationImpl.Max;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.streamProcessing.abstractOperators.CustomFlatMapper;
import shared.streamProcessing.abstractOperators.CustomFunction;
import shared.streamProcessing.abstractOperators.CustomPredicate;

import java.util.HashMap;
import java.util.Map;

public class OperationImplementations {

    private static Map<String, Object> streamFunctions;

    public static CustomFunction getMap(String name){
        if (streamFunctions == null) initialize();
        return (CustomFunction) streamFunctions.get(name);

    }

    /**
     *
     * @param name
     * @return field0 = Operator, field1 = Identity
     */
    public static CustomBinaryOperator getReduce(String name){
        if (streamFunctions == null) initialize();
        return (CustomBinaryOperator) streamFunctions.get(name);

    }

    public static CustomFlatMapper getFlatMap(String name){
        if (streamFunctions == null) initialize();
        return (CustomFlatMapper) streamFunctions.get(name);

    }

    public static CustomPredicate getFilter(String name){
        if (streamFunctions == null) initialize();
        return (CustomPredicate) streamFunctions.get(name);

    }

    /**
     * Register here operations
     */
    public static void initialize () {
        streamFunctions = new HashMap<>();

        streamFunctions.put("max", new Max());
    }

}

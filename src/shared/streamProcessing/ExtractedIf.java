package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import org.jetbrains.annotations.Nullable;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.streamProcessing.abstractOperators.CustomFlatMapper;
import shared.streamProcessing.abstractOperators.CustomFunction;
import shared.streamProcessing.abstractOperators.CustomPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public interface ExtractedIf extends SubstitutableLabels{

    ExtractedIf map(CustomFunction function, ArrayList<String> args);

    Map<Tuple, Object> reduce(CustomBinaryOperator accumulator, ArrayList<String> args);

    void set(ExtractedIf extractedIf);

    ExtractedIf flatmap(CustomFlatMapper mapper, ArrayList<String> args);

    ExtractedIf filter(CustomPredicate filterFunction, ArrayList<String> args);

    ExtractedStream.StreamType getStreamType();

    static Map<String, String> initializePartitionIfNull (@Nullable Map<String, String> partition) {
        if (partition != null) {
            return partition;
        } else {
            Map<String, String> result = new HashMap<>();
            result.put("all", "all");
            return result;
        }

    }


}

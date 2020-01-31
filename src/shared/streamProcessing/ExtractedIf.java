package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.streamProcessing.abstractOperators.CustomFlatMapper;
import shared.streamProcessing.abstractOperators.CustomFunction;
import shared.streamProcessing.abstractOperators.CustomPredicate;

import java.util.ArrayList;
import java.util.Map;

public interface ExtractedIf extends SubstitutableLabels{

    ExtractedIf map(CustomFunction function, ArrayList<String> args);

    Map<Tuple, Object> reduce(CustomBinaryOperator accumulator, ArrayList<String> args);

    void set(ExtractedIf extractedIf);

    ExtractedIf flatmap(CustomFlatMapper mapper, ArrayList<String> args);

    ExtractedIf filter(CustomPredicate filterFunction, ArrayList<String> args);

}

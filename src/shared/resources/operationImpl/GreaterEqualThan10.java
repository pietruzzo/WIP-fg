package shared.resources.operationImpl;

import org.apache.flink.api.java.tuple.Tuple;
import shared.streamProcessing.abstractOperators.CustomPredicate;


public class GreaterEqualThan10 extends CustomPredicate {

    /**
     *
     * @param tuple
     * @return true if at least one value of field is greater than 10
     */
    @Override
    public boolean test(Tuple tuple) {
        String[] field = super.labels.getField(tuple, 0);
        for (String value: field) {
            try {
                if (Double.parseDouble(value) >= 10) {
                    return true;
                }
            } catch (NumberFormatException e) {
                //Skip
            }
        }
        return false;
    }

}

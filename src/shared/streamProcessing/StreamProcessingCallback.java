package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import shared.data.MultiKeyMap;
import shared.variables.VariableAggregate;

import java.util.Map;

public interface StreamProcessingCallback {

    Aggregate getAggregatedResult (Aggregate aggregate) throws Exception;

    class Aggregate {
        private AggregateType aggregateType;
        //For each partition, the aggregate
        private MultiKeyMap<VariableAggregate> variableAggregate = null;
        //For each partition, for each group, tre reduced tuple
        private MultiKeyMap<Map<Tuple, Tuple>> reducedPartitions = null;
        private Long transactionId;

        public Aggregate(MultiKeyMap<VariableAggregate> variableAggregate, Long transactionId ) {
            this.variableAggregate = variableAggregate;
            this.aggregateType = AggregateType.VARIABLE_AGGREGATE;
            this.transactionId = transactionId;
        }


        public Aggregate(Long transactionId, MultiKeyMap<Map<Tuple, Tuple>> reducedPartitions) {
            this.reducedPartitions = reducedPartitions;
            this.aggregateType = AggregateType.REDUCE;
            this.transactionId = transactionId;
        }

        public AggregateType getAggregateType() {
            return aggregateType;
        }

        public MultiKeyMap<VariableAggregate> getVariableAggregate() {
            return variableAggregate;
        }


        public MultiKeyMap<Map<Tuple, Tuple>> getReducedPartitions() {
            return reducedPartitions;
        }

        public Long getTransactionId() {
            return transactionId;
        }
    }

    enum AggregateType { VARIABLE_AGGREGATE, REDUCE}
}


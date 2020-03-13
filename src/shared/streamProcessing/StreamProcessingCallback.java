package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import shared.data.MultiKeyMap;
import shared.variables.VariableAggregate;

import java.io.Serializable;
import java.util.Map;

public interface StreamProcessingCallback {

    Aggregate getAggregatedResult (Aggregate aggregate) throws Exception;
    void forwardAndForgetToMaster (Aggregate aggregate) throws Exception;

    abstract class Aggregate implements Serializable {

        private int transactionId;

        Aggregate(int transactionId) {
            this.transactionId = transactionId;
        }


        public int getTransactionId() {
            return transactionId;
        }
    }

    class VariableAggregateAggregate extends Aggregate {

        //For each partition, the aggregate
        private MultiKeyMap<VariableAggregate> variableAggregate = null;

        public VariableAggregateAggregate(int transactionId, MultiKeyMap<VariableAggregate> variableAggregate) {
            super(transactionId);
            this.variableAggregate = variableAggregate;
        }

        public MultiKeyMap<VariableAggregate> getPartitionsVariableAggregate() {
            return variableAggregate;
        }

    }

    class ReduceAggregate extends Aggregate {

        //For each partition, for each group, tre reduced tuple
        private MultiKeyMap<Map<Tuple, Object>> reducedPartitions;

        public ReduceAggregate(int transactionId, MultiKeyMap<Map<Tuple, Object>> reducedPartitions) {
            super(transactionId);
            this.reducedPartitions = reducedPartitions;
        }

        public MultiKeyMap<Map<Tuple, Object>> getReducedPartitions() {
            return reducedPartitions;
        }

    }

    class EvaluationAggregate extends Aggregate {

        private boolean resultEvaluation;

        public EvaluationAggregate(int transactionId, boolean resultEvaluation) {
            super(transactionId);
            this.resultEvaluation = resultEvaluation;
        }

        public boolean isResultEvaluation() {
            return resultEvaluation;
        }
    }

    enum AggregateType { VARIABLE_AGGREGATE, REDUCE, EVALUATION}
}


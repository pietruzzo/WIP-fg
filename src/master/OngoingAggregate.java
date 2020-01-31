package master;

import akka.actor.ActorRef;
import org.apache.flink.api.java.tuple.Tuple;
import org.jetbrains.annotations.Nullable;
import shared.AkkaMessages.AggregateMsg;
import shared.data.MultiKeyMap;
import shared.streamProcessing.StreamProcessingCallback;
import shared.streamProcessing.StreamProcessingCallback.Aggregate;
import shared.streamProcessing.StreamProcessingCallback.AggregateType;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.variables.VariableAggregate;

import java.util.*;
import java.util.stream.Collectors;

public class OngoingAggregate {

    final AggregateType type;
    final List<ActorRef> collectedActors;
    final CustomBinaryOperator operator;
    final List<Aggregate> aggregates;
    final int expectedNumberOfSlaves;
    final ActorRef self;
    private Boolean evaluation = null;
    private final String toFire;

    public OngoingAggregate(int expectedNumberOfSlaves, AggregateType type, @Nullable CustomBinaryOperator operator, ActorRef self, @Nullable String toFire) {
        this.operator = operator;
        this.self = self;
        this.toFire = toFire;
        this.aggregates = new ArrayList();
        this.type = type;
        this.expectedNumberOfSlaves = expectedNumberOfSlaves;
        this.collectedActors = new ArrayList<>();
    }

    /**
     *
     * @return true if collected all
     */
    public boolean performOperatorIfCollectedAll(){

        if (collectedActors.size() < expectedNumberOfSlaves) return false;

        else if (type.equals(AggregateType.VARIABLE_AGGREGATE)) {
            performVariableAggregate();
            return true;
        }

        else if (type.equals(AggregateType.REDUCE)) {
            performReduce();
            return true;
        }

        else if (type.equals(AggregateType.EVALUATION)) {
            performEvaluation();
            return true;
        }

        else {
            throw new RuntimeException("Are currently supported VARIABLE_AGGREGATE and REDUCE_PARTITION AggregateTypes (" + type.toString() +").");
        }

    }

    /**
     *
     * @return event to fire or null if nothing to fire
     */
    public String getEvaluation() {

        if (evaluation)
            return toFire;
        return null;

    }

    private void performEvaluation(){

        List<Boolean> aggregates =
                this.aggregates
                        .stream()
                        .map(aggregate -> ((StreamProcessingCallback.EvaluationAggregate) aggregate).isResultEvaluation())
                        .collect(Collectors.toList());

        if (aggregates.contains(Boolean.TRUE) && ! aggregates.contains(Boolean.FALSE))
            this.evaluation = true;
        else if ( ! aggregates.contains(Boolean.TRUE) && aggregates.contains(Boolean.FALSE))
            this.evaluation = false;
        else
            throw new RuntimeException("Evaluations aren't compliant from partitions -> Not supported by now");

    }

    private void performVariableAggregate (){

        List<StreamProcessingCallback.VariableAggregateAggregate> aggregates =
                this.aggregates
                        .stream()
                        .map(aggregate -> (StreamProcessingCallback.VariableAggregateAggregate) aggregate)
                        .collect(Collectors.toList());

        MultiKeyMap<ArrayList<Tuple>> collected = new MultiKeyMap<>(aggregates.get(0).getPartitionsVariableAggregate().getKeys());

        aggregates.parallelStream().forEach(aggregate -> {

            aggregate.getPartitionsVariableAggregate().getAllElements().entrySet().stream().forEach(partition -> {

                //Create partition in result if absent, made atomic
                synchronized (collected) {
                    if (collected.getValue(partition.getKey()) == null) {
                        collected.putValue(partition.getKey(), new ArrayList<>());
                    }
                }

                //Add Tuples
                synchronized (collected) {
                    collected.getValue(partition.getKey()).addAll(Arrays.asList(partition.getValue().getValue()));
                }

            });

        });

        //Compute result partitions
        MultiKeyMap<VariableAggregate> result = new MultiKeyMap<>(aggregates.get(0).getPartitionsVariableAggregate().getKeys());
        VariableAggregate old = aggregates.get(0).getPartitionsVariableAggregate().getAllElements().values().iterator().next();

        collected.getAllElements()
                .entrySet()
                .parallelStream()
                .forEach(entry ->
                        result.putValue(entry.getKey(), new VariableAggregate(old.getName(), old.getPersistence(), old.getTimestamp(), (Tuple[])entry.getValue().toArray(Tuple[]::new) , old.getTupleNames()))
                );

        //Build new aggregate and return it

        //new aggregate
        Aggregate oldAggregate = aggregates.get(0);
        Aggregate newAggregate = new StreamProcessingCallback.VariableAggregateAggregate(oldAggregate.getTransactionId(), result);
        //response method
        answerToAll(newAggregate);
    }

    private void performReduce (){

        List<StreamProcessingCallback.ReduceAggregate> aggregates =
                this.aggregates
                        .stream()
                        .map(aggregate -> (StreamProcessingCallback.ReduceAggregate) aggregate)
                        .collect(Collectors.toList());

        MultiKeyMap<Map<Tuple, Object>> result = new MultiKeyMap<>(aggregates.get(0).getReducedPartitions().getKeys());
        MultiKeyMap<HashMap<Tuple, ArrayList<Object>>> reducedPartitions = new MultiKeyMap<>(aggregates.get(0).getReducedPartitions().getKeys());

        for (int i = 0; i < aggregates.size(); i++) {
            MultiKeyMap<Map<Tuple, Object>> collected = aggregates.get(i).getReducedPartitions();

            collected.getAllElements().entrySet().stream().forEach(map -> {

                //Create Partition in reducedPartition
                if (reducedPartitions.getValue(map.getKey()) == null ) {
                    reducedPartitions.putValue(map.getKey(), new HashMap<>());
                    result.putValue(map.getKey(), new HashMap<>());
                }

                map.getValue().entrySet().stream().forEach(group -> {

                    //Create Group in reducedPartition
                    reducedPartitions.getValue(map.getKey()).computeIfAbsent(group.getKey(), k -> new ArrayList<>());

                    //Put Tuples in ReducedPartitions
                    reducedPartitions.getValue(map.getKey()).get(group.getKey()).add(group.getValue());

                });

            });
        }

        //Perform Reduction
        reducedPartitions.getAllElements().entrySet().stream().forEach(map -> {

            map.getValue().entrySet().stream().forEach( list -> {
                Object resultForGroupPartition = list.getValue().stream().reduce(this.operator.getBinaryOperator()).get();
                result.getValue(map.getKey()).put(list.getKey(), resultForGroupPartition);
            });
        });

        //new aggregate
        Aggregate oldAggregate = aggregates.get(0);
        Aggregate newAggregate = new StreamProcessingCallback.ReduceAggregate(oldAggregate.getTransactionId(), result);
        //response method
        answerToAll(newAggregate);

    }

    private void answerToAll(Aggregate newAggregate) {
        for (ActorRef actor: this.collectedActors) {
            actor.tell(new AggregateMsg(newAggregate), this.self);
        }
    }



}

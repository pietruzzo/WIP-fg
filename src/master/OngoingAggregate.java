package master;

import akka.actor.ActorRef;
import jdk.internal.jline.internal.Nullable;
import org.apache.flink.api.java.tuple.Tuple;
import shared.AkkaMessages.AggregateMsg;
import shared.data.MultiKeyMap;
import shared.streamProcessing.CustomBinaryOperator;
import shared.streamProcessing.StreamProcessingCallback.Aggregate;
import shared.streamProcessing.StreamProcessingCallback.AggregateType;
import shared.variables.VariableAggregate;

import java.util.*;

public class OngoingAggregate {

    final AggregateType type;
    final List<ActorRef> collectedActors;
    final CustomBinaryOperator operator;
    final List<Aggregate> aggregates;
    final int expectedNumberOfSlaves;
    final ActorRef self;

    public OngoingAggregate(int expectedNumberOfSlaves, AggregateType type, @Nullable CustomBinaryOperator operator, ActorRef self) {
        this.operator = operator;
        this.self = self;
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

        else {
            throw new RuntimeException("Are currently supported VARIABLE_AGGREGATE and REDUCE_PARTITION AggregateTypes (" + type.toString() +").");
        }

    }

    private void performVariableAggregate (){

        MultiKeyMap<ArrayList<Tuple>> collected = new MultiKeyMap<>(aggregates.get(0).getVariableAggregate().getKeys());

        this.aggregates.parallelStream().forEach(aggregate -> {
            aggregate.getVariableAggregate().getAllElements().entrySet().stream().forEach(partition -> {

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
        MultiKeyMap<VariableAggregate> result = new MultiKeyMap<>(aggregates.get(0).getVariableAggregate().getKeys());
        VariableAggregate old = aggregates.get(0).getVariableAggregate().getAllElements().values().iterator().next();
        collected.getAllElements()
                .entrySet()
                .parallelStream()
                .forEach(entry ->
                        result.putValue(entry.getKey(), new VariableAggregate(old.getName(), old.getPersistence(), old.getTimestamp(), (Tuple[])entry.getValue().toArray() , old.getTupleNames()))
                );

        //Build new aggregate and return it

        //new aggregate
        Aggregate oldAggregate = this.aggregates.get(0);
        Aggregate newAggregate = new Aggregate(result, oldAggregate.getTransactionId());
        //response method
        answerToAll(newAggregate);
    }

    private void performReduce (){

        MultiKeyMap<Map<Tuple, Tuple>> result = new MultiKeyMap<>(aggregates.get(0).getReducedPartitions().getKeys());
        MultiKeyMap<HashMap<Tuple, ArrayList<Tuple>>> reducedPartitions = new MultiKeyMap<>(aggregates.get(0).getReducedPartitions().getKeys());

        for (int i = 0; i < aggregates.size(); i++) {
            MultiKeyMap<Map<Tuple, Tuple>> collected = aggregates.get(i).getReducedPartitions();

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
                Tuple resultForGroupPartition = list.getValue().stream().reduce(this.operator).get();
                result.getValue(map.getKey()).put(list.getKey(), resultForGroupPartition);
            });
        });

        //new aggregate
        Aggregate oldAggregate = this.aggregates.get(0);
        Aggregate newAggregate = new Aggregate(oldAggregate.getTransactionId(), result);
        //response method
        answerToAll(newAggregate);

    }

    private void answerToAll(Aggregate newAggregate) {
        for (ActorRef actor: this.collectedActors) {
            actor.tell(new AggregateMsg(newAggregate), this.self);
        }
    }



}

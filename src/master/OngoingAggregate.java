package master;

import akka.actor.ActorRef;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple0;
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
    private Boolean evaluation = false;
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
    public boolean performOperatorIfCollectedAll(HashSet<String> validVariables){

        if (collectedActors.size() < expectedNumberOfSlaves) return false;

        else if (type.equals(AggregateType.VARIABLE_AGGREGATE)) {
            performVariableAggregate(validVariables);
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

    private void performVariableAggregate (HashSet<String> validVariables){

        List<StreamProcessingCallback.VariableAggregateAggregate> aggregates =
                this.aggregates
                        .stream()
                        .map(aggregate -> (StreamProcessingCallback.VariableAggregateAggregate) aggregate)
                        .collect(Collectors.toList());

        MultiKeyMap<ArrayList<Tuple>> collected = new MultiKeyMap<>(aggregates.get(0).getPartitionsVariableAggregate().getKeys());

        aggregates.stream().forEach(aggregate -> {

            aggregate.getPartitionsVariableAggregate().getAllElements().entrySet().stream().forEach(partition -> {

                //Create partition in result if absent
                collected.putIfAbsent(partition.getKey(), new ArrayList<>());

                //Add Tuples
                    collected.getValue(partition.getKey()).addAll(Arrays.asList(partition.getValue().getValue()));


            });

        });

        //Remove duplicates from different workers
        collected.getAllElements().entrySet().stream().forEach(entry -> {
            ArrayList<Tuple> values = entry.getValue();
            Set<Tuple> toRemove = new HashSet<>();
            for (int i = 0; i < values.size(); i++) {
                for (int j = i+1; j < values.size(); j++) {
                    boolean deleteI = false;
                        if (values.get(i).getArity() == values.get(j).getArity()) {
                            for (int k = 0; k < values.get(i).getArity(); k++) {
                                boolean allFieldsEqual = true;
                                String[] a = values.get(i).getField(k);
                                String[] b = values.get(j).getField(k);
                                if (a.length == b.length){
                                    for (int l = 0; l < a.length; l++) {
                                        if (!(a[l].equals(b[l]))){
                                            allFieldsEqual = false;
                                            break;
                                        }
                                    }
                                    if (allFieldsEqual) deleteI = true;
                                }
                            }
                        }
                    if (deleteI) toRemove.add(values.get(i));
                }
            }
            values.removeAll(toRemove);
            entry.setValue(values);
        });

        //Compute result partitions
        MultiKeyMap<VariableAggregate> result = new MultiKeyMap<>(aggregates.get(0).getPartitionsVariableAggregate().getKeys());
        VariableAggregate old = aggregates.get(0).getPartitionsVariableAggregate().getAllElements().values().iterator().next();


        collected.getAllElements()
                .entrySet()
                .forEach(entry ->
                        result.putValue(entry.getKey(), new VariableAggregate(old.getName(), old.getPersistence(), old.getTimestamp(), (Tuple[])entry.getValue().toArray(Tuple[]::new) , old.getTupleNames()))
                );

        //Build new aggregate and return it

        //new aggregate
        Aggregate oldAggregate = aggregates.get(0);
        Aggregate newAggregate = new StreamProcessingCallback.VariableAggregateAggregate(oldAggregate.getTransactionId(), result);

        //Update validVariables
        for (VariableAggregate varAgg : result.getAllElements().values()) {
            Tuple[] value = varAgg.getValue();
            if (value != null && (value.length>1 || value.length==1 && !(value[0] instanceof Tuple0))){
                validVariables.add(old.getName());
                break;
            }
        }

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

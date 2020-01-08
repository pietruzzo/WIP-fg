package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.computation.ComputationRuntime;
import shared.data.MultiKeyMap;
import shared.exceptions.InvalidOperationChain;
import shared.variables.*;
import shared.variables.solver.VariableSolver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartitionStreamsHandler {

    private List<Operations> operationsList;
    private VariableSolver variableSolver;
    private MultiKeyMap<ComputationRuntime> runtimes;
    private StreamProcessingCallback callback;

    public PartitionStreamsHandler(MultiKeyMap<ComputationRuntime> runtimes, List<Operations> operationsList, VariableSolver variableSolver, StreamProcessingCallback callback){

        this.runtimes = runtimes;

        //Check the begin of stream

        if( ! (operationsList.get(0) instanceof Operations.Extract) || ! (operationsList.get(0) instanceof Operations.StreamVariable)) {
            throw new InvalidOperationChain("Chain must begin with extract operation (" + operationsList.get(0).getClass().toGenericString() + ").");
        }

        this.variableSolver = variableSolver;

        this.callback = callback;
    }

    public void solveOperationChain () throws Exception {

        MultiKeyMap<ExtractedIf> partitions = null;

        for (Operations operation:this.operationsList) {

            if (operation instanceof Operations.Map) {

                Operations.Map opMap = (Operations.Map) operation;
                partitions.getAllElements().values().forEach(partition -> partition.set(partition.map(opMap.function)));

            }
            else if (operation instanceof Operations.Extract) {

                //Get streams from ComputationRuntimes

                final MultiKeyMap<ExtractedIf> fPartitions = new MultiKeyMap<>(runtimes.getKeys());

                Operations.Extract extractOperator = (Operations.Extract) operationsList.get(0);

                runtimes.getAllElements().entrySet().parallelStream()
                        .forEach(entry -> {
                            fPartitions.putValue(entry.getKey(), new ExtractedStream(entry.getValue().getPartition(), Arrays.asList(extractOperator.labels), extractOperator.edges, entry.getValue()));
                        });

                partitions = fPartitions;

            }
            else if (operation instanceof Operations.Collect) {

                final MultiKeyMap<ExtractedIf> newPartitions = new MultiKeyMap<>(partitions.getKeys());

                partitions.getAllElements().entrySet().stream()
                        .forEach(partitionEntry -> newPartitions.putValue(partitionEntry.getKey(), ((ExtractedGroupedStream)partitionEntry.getValue()).collect()));

                partitions = newPartitions;
            }
            else if (operation instanceof Operations.Emit) {

                Operations.Emit opEmit = (Operations.Emit) operation;

                //if variable node, edge -> put in variable solver,
                // otherwise send to master variable and use ASK to get complete aggregate
                Variable variable = this.emitVariable(opEmit.variableName, opEmit.persistence, partitions);

                if (variable instanceof VariableVertex || variable instanceof VariableEdge){
                    variableSolver.addVariable(variable);
                } else if (variable instanceof VariableAggregate) {

                    /*
                    Aggregate results if only one partition:   generate a MultikeyMap with one partition with key "all"   -
                     */
                    MultiKeyMap<VariableAggregate> allPartition = new MultiKeyMap<>(new String[]{"all"});
                    HashMap<String, String> key = new HashMap<>();
                    key.put("all", "all");
                    allPartition.putValue(key, (VariableAggregate)variable);
                    VariableAggregate variableAggregate = ((StreamProcessingCallback.VariableAggregateAggregate)
                            callback.getAggregatedResult(
                                    new StreamProcessingCallback.VariableAggregateAggregate(opEmit.transaction_id, allPartition)))
                                    .getPartitionsVariableAggregate().getAllElements().values().iterator().next();
                    variableSolver.addVariable(variableAggregate);

                } else if ( variable instanceof VariablePartition){

                    //Can be node, edge, aggregate
                    Variable insideElement = ((VariablePartition) variable).getAllInsideVariables().getAllElements().values().iterator().next();

                    if (insideElement instanceof VariableVertex || insideElement instanceof VariableEdge){
                        variableSolver.addVariable(variable);

                    } else if (insideElement instanceof VariableAggregate) {

                        MultiKeyMap<VariableAggregate> aggregates = new MultiKeyMap<>(((VariablePartition)variable).getAllInsideVariables().getKeys());

                        ((VariablePartition)variable).getAllInsideVariables()
                                .getAllElements()
                                .entrySet()
                                .forEach(entry -> aggregates.putValue(entry.getKey(), (VariableAggregate)entry.getValue()));


                        final MultiKeyMap<Variable> resultAggregate = new MultiKeyMap<>(((VariablePartition) variable).getAllInsideVariables().getKeys());

                        ((StreamProcessingCallback.VariableAggregateAggregate) callback
                                .getAggregatedResult(new StreamProcessingCallback.VariableAggregateAggregate(opEmit.transaction_id, aggregates)))
                                .getPartitionsVariableAggregate()
                                .getAllElements()
                                .entrySet()
                                .forEach(entry -> resultAggregate.putValue(entry.getKey(), entry.getValue()));

                        variableSolver.addVariable(new VariablePartition(variable.getName(), variable.getPersistence(), variable.getTimestamp(), resultAggregate));

                    }
                }
            }
            else if (operation instanceof Operations.Evaluate) {

                Operations.Evaluate opEvaluation = (Operations.Evaluate) operation;
                boolean result = false;

                //More than one subgraph -> ERROR
                if (partitions.getAllElements().values().size() != 1) {
                    throw new InvalidOperationChain(
                            "Evaluation can be performed only on 1 graph/subgraph -> so reduce number of partitions in before applying it." +
                                    "\n number of subgraphs: " + partitions.getAllElements().values().size());
                }

                //More than one Group -> ERROR
                ExtractedIf partition = partitions.getAllElements().values().iterator().next();
                if ( !(partition instanceof ExtractedStream)) {
                    throw new InvalidOperationChain(
                            "Evaluation can be performed only if not grouped -> so consider to collect before launching it." +
                                    partition.getClass().toGenericString());
                }

                //Get the tuple
                List<Tuple> collectedResult = ((ExtractedStream) partition).getStream().collect(Collectors.toList());

                //More than one tuple -> ERROR
                if (collectedResult.size() > 1) {
                    throw new InvalidOperationChain(
                            "More than one tuple on Evaluation is not supported " + collectedResult.size());
                }

                //Zero values -> return false;
                if (collectedResult.size() == 0) {
                    result = false;
                }

                //Check that tuple is Tuple1 and has only one value
                if (collectedResult.get(0) instanceof Tuple1 ) {
                    Tuple1<String[]> tuple1 = (Tuple1<String[]>) collectedResult.get(0);
                    if (tuple1.f0.length != 1) throw new RuntimeException("Tuple for evaluation must be single value");

                    //Perform comparison
                    result = opEvaluation.operator.apply(tuple1.f0, new String[]{opEvaluation.value});


                            callback
                                    .forwardAndForgetToMaster(new StreamProcessingCallback.EvaluationAggregate(opEvaluation.transaction_id, result));

                }

            }
            else if (operation instanceof Operations.Filter) {

                Operations.Filter opFilter = (Operations.Filter) operation;
                partitions.getAllElements().values().stream().forEach(partition -> partition.set(partition.filter(opFilter.filterFunction)));

            }
            else if (operation instanceof Operations.FlatMap) {

                Operations.FlatMap opFlatMap = (Operations.FlatMap) operation;
                partitions.getAllElements().values().stream().forEach(partition -> partition.set(partition.flatmap(opFlatMap.mapper)));

            }
            else if (operation instanceof Operations.GroupBy) {

                Operations.GroupBy opGroupBy = (Operations.GroupBy) operation;
                partitions.getAllElements().values().stream().forEach(partition -> partition.set(((ExtractedStream)partition).groupby(opGroupBy.groupingLabels)));

            }
            else if (operation instanceof Operations.Merge) {

                Operations.Merge opMerge = (Operations.Merge) operation;
                partitions.getAllElements().values().forEach(partition -> partition.set(((ExtractedStream)partition).merge(opMerge.groupingLabels)));

            }
            else if (operation instanceof Operations.Reduce) {

                Operations.Reduce opReduce = (Operations.Reduce) operation;
                final MultiKeyMap<Map<Tuple, Tuple>> reduced = new MultiKeyMap<>(partitions.getKeys());
                partitions.getAllElements().entrySet().stream().forEach(entry -> reduced.putValue(entry.getKey(), entry.getValue().reduce(opReduce.identity, opReduce.accumulator)));

                //send to master reduced results -> USE ASK to get response

                MultiKeyMap<Map<Tuple, Tuple>> returned = ((StreamProcessingCallback.ReduceAggregate)
                        callback.getAggregatedResult(
                        new StreamProcessingCallback.ReduceAggregate(opReduce.transaction_Id, reduced)))
                        .getReducedPartitions();

                //For each returned partition restore -> ExtractedStream or ExtractedGroupedStream
                partitions.getAllElements().replaceAll((entryKey, oldValue) -> {

                    if (oldValue instanceof ExtractedStream) {

                        ArrayList<Tuple> tuples = new ArrayList<>();
                        tuples.add(returned.getValue(entryKey).values().iterator().next());
                        ExtractedStream oldExtracted = (ExtractedStream) oldValue;
                        return new ExtractedStream(oldExtracted.getPartition(), opReduce.fieldNames, ExtractedStream.StreamType.AGGREGATE, tuples.stream());

                    } else {

                        Map<Tuple, Tuple> tuples = returned.getValue(entryKey);
                        Map<Tuple, Stream<Tuple>> newStreams = tuples.entrySet().stream()
                                .map(entry -> {
                                    List<Tuple> tuple = new ArrayList<>();
                                    tuple.add(entry.getValue());
                                    return new Tuple2<>(entry.getKey(), tuple.stream());
                                }).collect(Collectors.toMap(tuple-> tuple.f0, tuple -> tuple.f1));

                        ExtractedGroupedStream oldExtracted = (ExtractedGroupedStream) oldValue;
                        return new ExtractedGroupedStream(oldExtracted.getPartition(), opReduce.fieldNames, newStreams, ExtractedStream.StreamType.AGGREGATE);

                    }
                });



            }
            else if (operation instanceof Operations.StreamVariable) {

                Operations.StreamVariable opStream = (Operations.StreamVariable) operation;

                //If partition isn't already defined -> Stream
                if (partitions == null) {
                    partitions = variableSolver.getStream(opStream.VariableName, opStream.wType, opStream.timeAgo);
                }
                //If partition is already defined -> stream and  JOIN
                else {
                    MultiKeyMap<ExtractedIf> secondStreams = variableSolver.getStream(opStream.VariableName, opStream.wType, opStream.timeAgo);
                    partitions.getAllElements().values().stream().forEach(partition -> {
                        partition.set(((ExtractedStream)partition).joinStream((ExtractedStream)secondStreams.getValue(((ExtractedStream) partition).getPartition())));
                    });
                }
            }
            else {
                throw new InvalidOperationChain("operation not recognized: " + operation.getClass());
            }

        }
    }



    private ExtractedStream collectPartitions(MultiKeyMap<ExtractedStream> partitions){
        /*
            Get partitions fieldName list
            Compute new fields name ( appended to ExtractedStream.JOIN_PARTITION)
            Get StreamType
            Stream partitions
                Calculate partition fields
                Stream tuples from each partiton
                    Append partition fields
         */
        String[] actualPartitionFields = partitions.getKeys();

        List<String> newPartitionFieldsName = Arrays.stream(actualPartitionFields)
                .map(field -> ExtractedStream.JOIN_PARTITION + field)
                .collect(Collectors.toList());

        ExtractedStream.StreamType type = partitions.getAllElements().values().iterator().next().getStreamType();

        Stream<Tuple> collectedStream = partitions.getAllElements()
                .entrySet()
                .parallelStream()
                .map(entryPartition -> {

                    ArrayList<String> partitionFieldsValues = new ArrayList();

                    for (String fieldName: actualPartitionFields) {
                        partitionFieldsValues.add(entryPartition.getKey().getKeysMapping().get(fieldName));
                    }

                    return entryPartition.getValue().getStream().map(tuple -> {
                        Tuple withPartition = Tuple.newInstance(tuple.getArity() + partitionFieldsValues.size());
                        for (int i = 0; i < tuple.getArity(); i++) {
                            withPartition.setField(tuple.getField(i), i);
                        }
                        for (int i = 0; i < partitionFieldsValues.size(); i++) {
                            withPartition.setField(partitionFieldsValues.get(i), i + tuple.getArity());
                        }
                        return withPartition;
                    });
                })
                .flatMap(stream -> stream);

        return new ExtractedStream(null, new ArrayList<>(newPartitionFieldsName), type, collectedStream);
    }

    private ExtractedStream reducePartitions(PartitionsReducer reducer){
        //todo: NOT IMPLEMENTED RIGHT NOW
        return null;
    }

    private Variable emitVariable(String variableName, long persistence, MultiKeyMap<ExtractedIf> partitions){
        /*

            get first extracted stream -> if it is null emit
                Create a multimap that contains Variables: node, edge or aggregate
            otherwise Create a multimap that contains Variable for variablePartition
                for each ExtractedStream in the multimap -> get variable and collect it in multimap

            return Variable
         */
        ExtractedStream partition = (ExtractedStream)partitions.getAllElements().entrySet().iterator().next().getValue();

        if (partitions.getAllElements().entrySet().size() == 1
                && ( partition.getPartition() == null || partition.getPartition().isEmpty()) ) {
            return partition.emit(this.variableSolver, variableName, persistence);
        }

        MultiKeyMap<Variable> newPartitions = new MultiKeyMap<>(partitions.getKeys());

        partitions.getAllElements().values().parallelStream().forEach(extractedIf -> {
            ExtractedStream extractedStream = (ExtractedStream) extractedIf;
            newPartitions.putValue(new HashMap(extractedStream.getPartition()), extractedStream.emit(variableSolver, variableName, persistence));
        });

        return new VariablePartition(variableName, persistence, variableSolver.getCurrentTimestamp(), newPartitions);
    }

}

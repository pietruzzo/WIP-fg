package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.computation.ComputationRuntime;
import shared.computation.Vertex;
import shared.data.MultiKeyMap;
import shared.exceptions.InvalidOperationChain;
import shared.selection.SelectionSolver;
import shared.variables.Variable;
import shared.variables.solver.VariableSolver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartitionStreamsHandler {

    private MultiKeyMap<ExtractedStream> partitions;
    private List<Operations> operationsList;

    public PartitionStreamsHandler(MultiKeyMap<ComputationRuntime> runtimes, List<Operations> operationsList){
        //Distinguish between extraction from runtimes and collect. In this constructor -> extract from runtime

        if( ! (operationsList.get(0) instanceof Operations.Extract) ) throw new InvalidOperationChain("Chain must begin with extract operation (" + operationsList.get(0).getClass().toGenericString() + ").");
        Operations.Extract extractOperator = (Operations.Extract) operationsList.get(0);
        operationsList.remove(0);

        partitions = new MultiKeyMap<>(runtimes.getKeys());
        runtimes.getAllElements().entrySet().parallelStream()
                .forEach(entry -> {
                    partitions.putValue(entry.getKey(), new ExtractedStream(entry.getValue().getPartition(), Arrays.asList(extractOperator.labels), extractOperator.edges, entry.getValue()));
                });

    }

    public PartitionStreamsHandler (String variableName, SelectionSolver.Operation.WindowType windowType, String timestamp, VariableSolver variableSolver, List<Operations> operationsList){
        this.partitions = variableSolver.getStream(variableName, windowType, timestamp);
        this.operationsList = operationsList;
    }



    private ExtractedStream collectPartitions(){
        /*
            Get partitions fieldName list
            Compute new fields name ( appended to ExtractedStream.JOIN_PARTITION)
            Get StreamType
            Stream partitions
                Calculate partition fields
                Stream tuples from each partiton
                    Append partition fields
         */
        String[] actualPartitionFields = this.partitions.getKeys();

        List<String> newPartitionFieldsName = Arrays.stream(actualPartitionFields)
                .map(field -> ExtractedStream.JOIN_PARTITION + field)
                .collect(Collectors.toList());

        ExtractedStream.StreamType type = this.partitions.getAllElements().values().iterator().next().getStreamType();

        Stream<Tuple> collectedStream = this.partitions.getAllElements()
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
        //todo: implement or not?
        return null;
    }

    private Variable emitVariable(){}

}

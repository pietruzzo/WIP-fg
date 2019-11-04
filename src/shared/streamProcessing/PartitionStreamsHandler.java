package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple2;
import shared.computation.ComputationRuntime;
import shared.computation.Vertex;
import shared.data.MultiKeyMap;
import shared.exceptions.InvalidOperationChain;
import shared.variables.Variable;
import shared.variables.solver.VariableSolver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartitionStreamsHandler {

    private MultiKeyMap<ExtractedStream> partitions;
    private List<Operations> operationsList;

    public PartitionStreamsHandler(MultiKeyMap<ComputationRuntime> runtimes, List<Operations> operationsList){
        //Distinguish between extraction from runtimes and collect. In this constructor -> extract
        if( ! (operationsList.get(0) instanceof Operations.Extract) ) throw new InvalidOperationChain("Chain must begin with extract operation (" + operationsList.get(0).getClass().toGenericString() + ").");
        Operations.Extract extractOperator = (Operations.Extract) operationsList.get(0);
        operationsList.remove(0);

        partitions = new MultiKeyMap<>(runtimes.getKeys());
        runtimes.getAllElements().entrySet().parallelStream()
                .forEach(entry -> {
                    partitions.putValue(entry.getKey(), new ExtractedStream(entry.getValue().getPartition(), Arrays.asList(extractOperator.labels), extractOperator.edges, entry.getValue()));
                });

    }

    public PartitionStreamsHandler (String variableName,  VariableSolver variableSolver){
        //TODO: extract stream from variable

    }


    private ExtractedStream collectPartitions(){}

    private ExtractedStream reducePartitions(PartitionsReducer reducer){}

    private Variable emitVariable(){}

}

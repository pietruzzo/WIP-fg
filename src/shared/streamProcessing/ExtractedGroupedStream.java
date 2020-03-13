package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.streamProcessing.abstractOperators.CustomFlatMapper;
import shared.streamProcessing.abstractOperators.CustomFunction;
import shared.streamProcessing.abstractOperators.CustomPredicate;

import java.util.*;
import java.util.stream.Stream;

import static shared.streamProcessing.ExtractedStream.*;

public class ExtractedGroupedStream implements ExtractedIf {

    private Map<String, String> partition;
    private ArrayList<String> tupleFields;
    private int[] arguments;
    private ExtractedStream.StreamType streamType;
    private Map<Tuple, Stream<Tuple>> groupedStreams;

    public ExtractedGroupedStream(Map<String, String> partition, ArrayList<String> tupleFields, ExtractedStream.StreamType streamType, Map<Tuple, List<Tuple>> groupedStreams) {
        this.partition = ExtractedIf.initializePartitionIfNull(partition);
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.groupedStreams = new HashMap<>();

        for (Map.Entry<Tuple, List<Tuple>> entry: groupedStreams.entrySet()) {
            this.groupedStreams.put(entry.getKey(), entry.getValue().stream());
        }
    }

    public ExtractedGroupedStream(Map<String, String> partition, ArrayList<String> tupleFields, Map<Tuple, Stream<Tuple>> groupedStreams, ExtractedStream.StreamType streamType) {
        this.partition = ExtractedIf.initializePartitionIfNull(partition);
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.groupedStreams = groupedStreams;
    }


    @Override
    public String[] getField(Tuple tuple, int argument) {
        if (argument == 0 && arguments == null) return getField(tuple);
        return tuple.getField(arguments[argument]);
    }

    @Override
    public String[] getField(Tuple tuple) {
        return tuple.getField(tuple.getArity()-1);
    }

    @Override
    public Tuple generateTuple(Tuple oldTuple, List<String[]> fields) {

        return getTuple(oldTuple, fields, streamType, tupleFields);
    }

    @Override
    public String[] getNodeId(Tuple tuple) {
        int index = tupleFields.indexOf(NODELABEL);
        if (index == -1) return null;
        return tuple.getField(index);
    }

    @Override
    public String[] getEdgeId(Tuple tuple) {
        int index = tupleFields.indexOf(EDGELABEL);
        if (index == -1) return null;
        return tuple.getField(index);
    }



    @Override
    public void set(ExtractedIf extractedIf) {
        ExtractedGroupedStream extractedStream = (ExtractedGroupedStream)extractedIf;
        this.partition = extractedStream.partition;
        this.tupleFields = extractedStream.tupleFields;
        this.streamType = extractedStream.streamType;
        this.groupedStreams = extractedStream.groupedStreams;

    }

    public Map<String, String> getPartition() {
        return partition;
    }

    public ExtractedStream collect(){
        Stream<Tuple> newStream =  this.groupedStreams.values().stream().flatMap(list -> list);
        return new ExtractedStream(partition, (ArrayList<String>)this.tupleFields.clone(), streamType, newStream);
    }

    @Override
    public ExtractedIf map(CustomFunction function, ArrayList<String> args){
        function.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().map(function).filter(Objects::nonNull);;
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, function.getNewFieldNames(this.streamType), this.streamType);
    }

    @Override
    public Map<Tuple, Object> reduce(CustomBinaryOperator accumulator, ArrayList<String> args){
        accumulator.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Map<Tuple, Object> newGroupedAccumulated  = new HashMap<>();

        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Object newAccumulated = stream.getValue().reduce(accumulator.getIdentity(), accumulator, accumulator.getBinaryOperator());
            newGroupedAccumulated.put(stream.getKey(), newAccumulated);
        }
        return newGroupedAccumulated;
    }

    @Override
    public ExtractedIf flatmap(CustomFlatMapper mapper, ArrayList<String> args){
        mapper.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().flatMap(mapper).filter(Objects::nonNull);;
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, mapper.getNewFieldNames(this.streamType), this.streamType);
    }

    @Override
    public ExtractedIf filter(CustomPredicate filterFunction, ArrayList<String> args){
        filterFunction.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().filter(filterFunction);
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, this.tupleFields, this.streamType);
    }


    private ExtractedIf getExtractedStream(Map<Tuple, Stream<Tuple>> groupedStreams, ArrayList<String> tupleFields, ExtractedStream.StreamType streamType){
        return new ExtractedGroupedStream(this.partition, (ArrayList<String>)tupleFields.clone(), groupedStreams, streamType);
    }

}

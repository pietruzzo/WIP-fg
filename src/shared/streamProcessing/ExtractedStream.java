package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.computation.ComputationRuntime;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractedStream {

    public static final String NODELABEL = "Node";
    public static final String EDGELABEL = "Dest";
    public enum StreamType {NODE, EDGE, AGGREGATE_VALUE, AGGREGATE_TUPLE};

    private final Map<String, String> partition;
    private final long timestamp;
    private ArrayList<String> tupleFields;
    private final StreamType streamType;
    private final Stream<Tuple> stream;

    public ExtractedStream(Map<String, String> partition, long timestamp, List<String> tupleFields, boolean isEdgeExtraction, ComputationRuntime computationRuntime) {
        this.partition = partition;
        this.timestamp = timestamp;

        //Tuple field handling
        this.tupleFields = new ArrayList<>();
        tupleFields.add(NODELABEL);
        if (isEdgeExtraction) tupleFields.add(EDGELABEL);
        this.tupleFields.addAll(tupleFields);

        if (isEdgeExtraction) streamType = StreamType.EDGE;
        else    streamType = StreamType.NODE;

        //Create parallel stream
        if (!isEdgeExtraction) {
            this.stream = computationRuntime.getVertices().values().parallelStream().map(value -> {
                Tuple t = Tuple.newInstance(this.tupleFields.size());
                t.setField(value.getNodeId(), 0);
                for (int i = 1; i < t.getArity(); i++) {
                    t.setField(value.getLabelVertex(this.tupleFields.get(i)), i);
                }
                return t;
            });
        } else {
            this.stream = computationRuntime.getVertices().values().parallelStream().map(value -> {
                ArrayList<Tuple> returnTuples = new ArrayList<>();
                for (String edgeLink: value.getEdges()) {
                    Tuple t = Tuple.newInstance(this.tupleFields.size());
                    t.setField(value.getNodeId(), 0);
                    t.setField(edgeLink, 1);
                    for (int i = 2; i < t.getArity(); i++) {
                        t.setField(value.getLabelEdge(edgeLink, this.tupleFields.get(i)), i);
                    }
                    returnTuples.add(t);
                }
                return returnTuples;
            }).flatMap(list -> list.parallelStream());
        }
    }

    public ExtractedStream(Map<String, String> partition, long timestamp, ArrayList<String> tupleFields, StreamType streamType, Stream<Tuple> stream) {
        this.partition = partition;
        this.timestamp = timestamp;
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.stream = stream;
    }

    public Stream<Tuple> getStream(){
        return this.stream;
    }

    public ExtractedStream map(Function<Tuple, Tuple> function){
        Stream<Tuple> newStream = stream.map(function);
        return this.getExtractedStream(newStream, this.tupleFields, this.streamType);
    }

    public Tuple reduce(Tuple identity, CustomBinaryOperator accumulator){
        return this.stream.reduce(identity, accumulator);
    }

    public ExtractedStream flatternMultivalue(String key){
        Stream<Tuple> newStream = this.stream.map(tuple -> flattern(tuple, key)).flatMap(list -> list.parallelStream());
        return getExtractedStream(newStream, this.tupleFields, this.streamType);
    }

    public ExtractedStream flatmap(Function<Tuple, Stream<Tuple>> mapper){
        Stream<Tuple> newStream = this.stream.flatMap(mapper);
        return getExtractedStream(newStream, this.tupleFields, this.streamType);
    }

    public ExtractedStream filter(Predicate<Tuple> filterFunction){
        Stream<Tuple> newStream = stream.filter(filterFunction);
        return getExtractedStream(newStream, this.tupleFields, this.streamType);
    }

    public GroupedExtracted groupby(String[] groupingLabels){
        Map<Tuple, List<Tuple>> newStream = stream.collect(Collectors.groupingBy(tuple -> {
            Tuple key = Tuple.newInstance(groupingLabels.length);
            for (int i = 0; i < groupingLabels.length; i++) {
                String label = groupingLabels[i];
                int position = this.tupleFields.indexOf(label);
                key.setField(tuple.getField(position), i);
            }
            return key;
        }, Collectors.toList()));

        return new GroupedExtracted(this.partition, this.timestamp, (ArrayList<String>)this.tupleFields.clone(), this.streamType, newStream);
    }

    public List<Tuple> emit(){
       return this.stream.collect(Collectors.toList());
    }

    //Support methods

    private ExtractedStream getExtractedStream(Stream<Tuple> stream, ArrayList<String> tupleFields, StreamType streamType){
        return new ExtractedStream(this.partition, this.timestamp, (ArrayList<String>)tupleFields.clone(), streamType, stream);
    }

    private ArrayList<Tuple> flattern (Tuple tuple, String key) {
        ArrayList<Tuple> results = new ArrayList<>();

        int i = 0;
        while (!this.tupleFields.get(i).equals(key)){
            i = i +1;
        }


        for (int j = 0; j < ((String[])tuple.getField(i)).length; j++) {
            Tuple newTuple = tuple.copy();
            String[] newfield = new String[]{((String[])tuple.getField(i))[j]};
            newTuple.setField(newfield, i);
            results.add(newTuple);
        }
        return results;
    }
}

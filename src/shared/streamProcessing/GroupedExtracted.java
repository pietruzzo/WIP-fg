package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GroupedExtracted {

    private final Map<String, String> partition;
    private final long timestamp;
    private ArrayList<String> tupleFields;
    private final ExtractedStream.StreamType streamType;
    private final Map<Tuple, Stream<Tuple>> groupedStreams;

    public GroupedExtracted(Map<String, String> partition, long timestamp, ArrayList<String> tupleFields, ExtractedStream.StreamType streamType, Map<Tuple, List<Tuple>> groupedStreams) {
        this.partition = partition;
        this.timestamp = timestamp;
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.groupedStreams = new HashMap<>();

        for (Map.Entry<Tuple, List<Tuple>> entry: groupedStreams.entrySet()) {
            this.groupedStreams.put(entry.getKey(), entry.getValue().parallelStream());
        }
    }

    public GroupedExtracted(Map<String, String> partition, ArrayList<String> tupleFields, ExtractedStream.StreamType streamType, Map<Tuple, Stream<Tuple>> groupedStreams, long timestamp) {
        this.partition = partition;
        this.timestamp = timestamp;
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.groupedStreams = groupedStreams;
    }

    public ExtractedStream collect(){
        Stream<Tuple> newStream =  this.groupedStreams.values().stream().flatMap(list -> list);
        return new ExtractedStream(partition, timestamp, (ArrayList<String>)this.tupleFields.clone(), streamType, newStream);
    }

    public GroupedExtracted map(Function<Tuple, Tuple> function){
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().map(function);
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, this.tupleFields, this.streamType);
    }

    public Map<Tuple, Tuple> reduce(Tuple identity, CustomBinaryOperator accumulator){
        Map<Tuple, Tuple> newGroupedAccumulated  = new HashMap<>();

        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Tuple newAccumulated = stream.getValue().reduce(identity.copy(), accumulator.clone());
            newGroupedAccumulated.put(stream.getKey(), newAccumulated);
        }
        return newGroupedAccumulated;
    }

    public GroupedExtracted flatmap(Function<Tuple, Stream<Tuple>> mapper){
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().flatMap(mapper);
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, this.tupleFields, this.streamType);
    }

    public GroupedExtracted filter(Predicate<Tuple> filterFunction){
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().filter(filterFunction);
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, this.tupleFields, this.streamType);
    }


    private GroupedExtracted getExtractedStream(Map<Tuple, Stream<Tuple>> groupedStreams, ArrayList<String> tupleFields, ExtractedStream.StreamType streamType){
        return new GroupedExtracted(this.partition, (ArrayList<String>)tupleFields.clone(), streamType, groupedStreams, timestamp);
    }

}

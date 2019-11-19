package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ExtractedGroupedStream implements ExtractedIf { //TODO: è possibile evitarlo?

    private Map<String, String> partition;
    private ArrayList<String> tupleFields;
    private ExtractedStream.StreamType streamType;
    private Map<Tuple, Stream<Tuple>> groupedStreams;

    public ExtractedGroupedStream(Map<String, String> partition, ArrayList<String> tupleFields, ExtractedStream.StreamType streamType, Map<Tuple, List<Tuple>> groupedStreams) {
        this.partition = partition;
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.groupedStreams = new HashMap<>();

        for (Map.Entry<Tuple, List<Tuple>> entry: groupedStreams.entrySet()) {
            this.groupedStreams.put(entry.getKey(), entry.getValue().parallelStream());
        }
    }

    public ExtractedGroupedStream(Map<String, String> partition, ArrayList<String> tupleFields, Map<Tuple, Stream<Tuple>> groupedStreams, ExtractedStream.StreamType streamType) {
        this.partition = partition;
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.groupedStreams = groupedStreams;
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
    public ExtractedIf map(Function<Tuple, Tuple> function){
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().map(function);
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, this.tupleFields, this.streamType);
    }

    @Override
    public Map<Tuple, Tuple> reduce(Tuple identity, CustomBinaryOperator accumulator){
        Map<Tuple, Tuple> newGroupedAccumulated  = new HashMap<>();

        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Tuple newAccumulated = stream.getValue().reduce(identity.copy(), accumulator.clone());
            newGroupedAccumulated.put(stream.getKey(), newAccumulated);
        }
        return newGroupedAccumulated;
    }

    @Override
    public ExtractedIf flatmap(Function<Tuple, Stream<Tuple>> mapper){
        Map<Tuple, Stream<Tuple>> newGroupedStreams  = new HashMap<>();
        for (Map.Entry<Tuple, Stream<Tuple>> stream: groupedStreams.entrySet()) {
            Stream<Tuple> newStream = stream.getValue().flatMap(mapper);
            newGroupedStreams.put(stream.getKey(), newStream);
        }
        return this.getExtractedStream(newGroupedStreams, this.tupleFields, this.streamType);
    }

    @Override
    public ExtractedIf filter(Predicate<Tuple> filterFunction){
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

package shared.streamProcessing;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import shared.computation.ComputationRuntime;
import shared.exceptions.InvalidOperationChain;
import shared.exceptions.WrongTypeRuntimeException;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractedStream {

    public static final String NODELABEL = "Node";
    public static final String EDGELABEL = "Dest";
    public static final String JOIN_FIRST = "f.";
    public static final String JOIN_SECOND = "s.";
    public enum StreamType {NODE, EDGE, AGGREGATE};

    private final Map<String, String> partition;
    private ArrayList<String> tupleFields;
    private final StreamType streamType;
    private final Stream<Tuple> stream;

    public ExtractedStream(Map<String, String> partition, List<String> tupleFields, boolean isEdgeExtraction, ComputationRuntime computationRuntime) {
        this.partition = partition;

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
                t.setField(new String[]{value.getNodeId()}, 0);
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
                    t.setField(new String[]{value.getNodeId()}, 0);
                    t.setField(new String[]{edgeLink}, 1);
                    for (int i = 2; i < t.getArity(); i++) {
                        t.setField(value.getLabelEdge(edgeLink, this.tupleFields.get(i)), i);
                    }
                    returnTuples.add(t);
                }
                return returnTuples;
            }).flatMap(list -> list.parallelStream());
        }
    }

    public ExtractedStream(Map<String, String> partition, ArrayList<String> tupleFields, StreamType streamType, Stream<Tuple> stream) {
        this.partition = partition;
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

    public ExtractedStream joinStream(ExtractedStream otherStream){ //On the single partition
        if (    (this.streamType == StreamType.EDGE && otherStream.streamType == StreamType.NODE )
            || (this.streamType == StreamType.NODE && otherStream.streamType == StreamType.EDGE ) ) {
            throw new IllegalArgumentException("It isn't possible to join NODE and EDGE streams");
        }
        ArrayList<String> newTupleFields = this.newTupleFields(this.tupleFields, otherStream.tupleFields, this.streamType, otherStream.streamType);

        if (this.streamType == StreamType.NODE && otherStream.streamType == StreamType.NODE){
            ExtractedStream firstStream = this.relocateFields(StreamType.NODE, newTupleFields, false);
            ExtractedStream secondStream = this.relocateFields(StreamType.NODE, newTupleFields, true);
            ExtractedStream e = new ExtractedStream(this.partition, newTupleFields, StreamType.NODE, Stream.concat(firstStream.stream, secondStream.stream));
            return e.merge(new String[]{NODELABEL});

        } else if (this.streamType == StreamType.EDGE && otherStream.streamType == StreamType.EDGE){
            ExtractedStream firstStream = this.relocateFields(StreamType.EDGE, newTupleFields, false);
            ExtractedStream secondStream = this.relocateFields(StreamType.EDGE, newTupleFields, true);
            ExtractedStream e = new ExtractedStream(this.partition, newTupleFields, StreamType.EDGE, Stream.concat(firstStream.stream, secondStream.stream));
            return e.merge(new String[]{NODELABEL, EDGELABEL});

        } else if (this.streamType == StreamType.AGGREGATE && !(otherStream.streamType == StreamType.AGGREGATE)){

                Stream<Tuple> newStream = otherStream.stream.map(otherTuple-> this.stream.map(thisTuple -> {

                    Tuple newTuple = Tuple.newInstance(this.tupleFields.size() + otherStream.tupleFields.size());
                    for (int i = 0; i < otherStream.tupleFields.size(); i++) {
                        newTuple.setField(otherTuple.getField(i), i);
                    }
                    for (int i = otherTuple.getArity(); i < newTuple.getArity(); i++) {
                        newTuple.setField(thisTuple.getField(i - otherTuple.getArity()), i);
                    }
                    return newTuple;

                })).flatMap(str -> str);

                return new ExtractedStream(this.partition, newTupleFields, otherStream.streamType, newStream);

        } else if (otherStream.streamType == StreamType.AGGREGATE){ //This is not aggregate, 'other' is

            Stream<Tuple> newStream = otherStream.stream.map(otherTuple-> this.stream.map(thisTuple -> {

                Tuple newTuple = Tuple.newInstance(this.tupleFields.size() + otherStream.tupleFields.size());
                for (int i = 0; i < this.tupleFields.size(); i++) {
                    newTuple.setField(thisTuple.getField(i), i);
                }
                for (int i = thisTuple.getArity(); i < newTuple.getArity(); i++) {
                    newTuple.setField(otherTuple.getField(i - thisTuple.getArity()), i);
                }
                return newTuple;

            })).flatMap(str -> str);

            return new ExtractedStream(this.partition, newTupleFields, this.streamType, newStream);

        } else {
            throw new InvalidOperationChain("ERROR STATE ");
        }
    }

    public GroupedExtracted groupby(String[] groupingLabels){ //NB: HashMap has hashing of the reference for the position of value
        Map<Tuple, List<Tuple>> newStream = stream.collect(Collectors.groupingByConcurrent(tuple -> {
            Tuple key = Tuple.newInstance(groupingLabels.length);
            for (int i = 0; i < groupingLabels.length; i++) {
                String label = groupingLabels[i];
                int position = this.tupleFields.indexOf(label);
                key.setField(tuple.getField(position), i);
            }
            return key;
        }, Collectors.toList()));

        return new GroupedExtracted(this.partition, (ArrayList<String>)this.tupleFields.clone(), this.streamType, newStream);
    }

    public ExtractedStream merge (String[] groupingLabels) {

        Map<Tuple, List<Tuple>> groups = this.stream.collect(Collectors.groupingByConcurrent(tuple -> {
            Tuple key = Tuple.newInstance(groupingLabels.length); //TODO può funzionare? (Deep Equality)
            for (int i = 0; i < groupingLabels.length; i++) {
                String label = groupingLabels[i];
                int position = this.tupleFields.indexOf(label);
                key.setField(tuple.getField(position), i);
            }
            return key;
        }, Collectors.toList()));
        //Unify in one tuple each group

        List<Tuple> result = groups.values().parallelStream().map(list -> {
            Tuple identity = Tuple.newInstance(list.get(0).getArity());
            for (int i = 0; i < identity.getArity(); i++) {
                identity.setField(new String[0], i);
            }
            return list.parallelStream().reduce(identity, (tuple, tuple2) -> {
                Tuple reduced = Tuple.newInstance(tuple.getArity());
                for (int i = 0; i < tuple.getArity(); i++) {
                    HashSet<String> values = new HashSet<>();
                    values.addAll(Arrays.asList(tuple.getField(i)));
                    values.addAll(Arrays.asList(tuple2.getField(i)));
                    tuple.setField(values, i);
                }
                return reduced;
            });
        }).collect(Collectors.toList());

        //region: new streamType
        StreamType newStreamType;
        if (Arrays.asList(groupingLabels).contains(NODELABEL) && Arrays.asList(groupingLabels).contains(EDGELABEL)){

            newStreamType = this.streamType;

        } else if (Arrays.asList(groupingLabels).contains(NODELABEL) ){

            if (this.streamType == StreamType.EDGE) {

                newStreamType = StreamType.NODE;

            } else {

                newStreamType = this.streamType;

            }

        } else {

            newStreamType = StreamType.AGGREGATE;

        }
        //endregion

        return this.getExtractedStream(result.stream(), this.tupleFields, newStreamType);
    }

    public List<Tuple> emit(){
       return this.stream.collect(Collectors.toList());
    }

    //Support methods

    private ExtractedStream getExtractedStream(Stream<Tuple> stream, ArrayList<String> tupleFields, StreamType streamType){
        return new ExtractedStream(this.partition, (ArrayList<String>)tupleFields.clone(), streamType, stream);
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

    private ArrayList<String> newTupleFields(ArrayList<String> tupleFields1, ArrayList<String> tupleFields2, StreamType first, StreamType second){

        ArrayList<String> newTupleFields = new ArrayList<>();

        if (first == StreamType.AGGREGATE && second == StreamType.AGGREGATE){

            for (String s: tupleFields1) {
                newTupleFields.add(JOIN_FIRST + s);
            }

            for (String s: tupleFields2) {
                newTupleFields.add(JOIN_SECOND + s);
            }
        }
        else if (first == StreamType.AGGREGATE && second == StreamType.EDGE) {

            newTupleFields.add(tupleFields2.get(0));
            newTupleFields.add(tupleFields2.get(1));

            for (int i = 2; i < tupleFields2.size(); i++) {
                String s = tupleFields2.get(i);
                newTupleFields.add(JOIN_SECOND + s);
            }

            for (String s: tupleFields1) {
                newTupleFields.add(JOIN_FIRST + s);
            }
        }
        else if (first == StreamType.AGGREGATE && second == StreamType.NODE) {

            newTupleFields.add(tupleFields2.get(0));

            for (int i = 1; i < tupleFields2.size(); i++) {
                String s = tupleFields2.get(i);
                newTupleFields.add(JOIN_SECOND + s);
            }

            for (String s: tupleFields1) {
                newTupleFields.add(JOIN_FIRST + s);
            }

        }
        else if (first == StreamType.NODE && (second == StreamType.AGGREGATE || second == StreamType.NODE)) {

            newTupleFields.add(tupleFields1.get(0));

            for (int i = 1; i < tupleFields1.size(); i++) {
                String s = tupleFields1.get(i);
                newTupleFields.add(JOIN_FIRST + s);
            }

            for (String s: tupleFields2) {
                newTupleFields.add(JOIN_SECOND + s);
            }

        }
        else if (first == StreamType.EDGE && (second == StreamType.AGGREGATE || second == StreamType.EDGE)){

            newTupleFields.add(tupleFields1.get(0));
            newTupleFields.add(tupleFields1.get(1));

            for (int i = 2; i < tupleFields1.size(); i++) {
                String s = tupleFields1.get(i);
                newTupleFields.add(JOIN_FIRST + s);
            }

            for (String s: tupleFields2) {
                newTupleFields.add(JOIN_SECOND + s);
            }

        }
        else {
            throw new InvalidOperationChain("Join of NODE-EDGE and EDGE-NODE are not allowed");
        }

        return newTupleFields;
    }

    private ExtractedStream relocateFields(StreamType type, ArrayList<String> extendedTupleFields, boolean putAtTheEnd){
        int keepFirstN = 0;
        if (type == StreamType.NODE) keepFirstN = 1;
        else if (type == StreamType.EDGE) keepFirstN = 2;

        final int keepFirstNFinal = keepFirstN;
        Stream<Tuple> stream = this.stream.map(tuple -> {
            Tuple newTuple = Tuple.newInstance(extendedTupleFields.size());
            for (int i = 0; i < extendedTupleFields.size(); i++) {
                if (i< keepFirstNFinal) {
                    newTuple.setField(tuple.getField(i), i);
                } else {
                    newTuple.setField(new String[0], i);
                }
            }
            if (putAtTheEnd){
                for (int i = newTuple.getArity() - tuple.getArity() + keepFirstNFinal ; i < newTuple.getArity(); i++) {
                    int j = i - newTuple.getArity() + tuple.getArity();
                    newTuple.setField(tuple.getField(j), i);
                }
            } else {
                for (int i = 0; i < tuple.getArity(); i++) {
                    newTuple.setField(tuple.getField(i), i);
                }
            }
            return newTuple;
        });

        return new ExtractedStream(this.partition, extendedTupleFields, type, stream);
    }

}

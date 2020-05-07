package shared.streamProcessing;

import master.JobManagerActor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple0;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.jetbrains.annotations.Nullable;
import shared.Utils;
import shared.VertexM;
import shared.computation.ComputationRuntime;
import shared.exceptions.InvalidOperationChain;
import shared.streamProcessing.abstractOperators.CustomBinaryOperator;
import shared.streamProcessing.abstractOperators.CustomFlatMapper;
import shared.streamProcessing.abstractOperators.CustomFunction;
import shared.streamProcessing.abstractOperators.CustomPredicate;
import shared.variables.Variable;
import shared.variables.VariableAggregate;
import shared.variables.VariableEdge;
import shared.variables.VariableVertex;
import shared.variables.solver.VariableSolver;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractedStream implements ExtractedIf{

    public static final String NODELABEL = "Node";
    public static final String EDGELABEL = "Dest";
    public static final String JOIN_PARTITION = "p.";
    public static final String JOIN_FIRST = "f.";
    public static final String JOIN_SECOND = "s.";
    public enum StreamType {NODE, EDGE, AGGREGATE};

    private Map<String, String> partition;
    private ArrayList<String> tupleFields;
    private int[] arguments;
    private StreamType streamType;
    private Stream<Tuple> stream;

    public ExtractedStream(Map<String, String> partition, List<String> tupleFields, boolean isEdgeExtraction, ComputationRuntime computationRuntime) {
        this.partition = ExtractedIf.initializePartitionIfNull(partition);

        //Tuple field handling
        this.tupleFields = new ArrayList<>();
        this.tupleFields.add(NODELABEL);

        if (isEdgeExtraction) {
            this.tupleFields.add(EDGELABEL);
            streamType = StreamType.EDGE;
        } else {
            streamType = StreamType.NODE;
        }

        this.tupleFields.addAll(tupleFields);


        //Create stream
        if (!isEdgeExtraction) {
            this.stream = computationRuntime.getVertices().values().stream().map(value -> {
                Tuple t = Tuple.newInstance(this.tupleFields.size());
                t.setField(new String[]{value.getNodeId()}, 0);
                for (int i = 1; i < t.getArity(); i++) {
                    t.setField(((VertexM)value).getLabelVertex(this.tupleFields.get(i)), i);
                }
                return t;
            });
        } else {
            this.stream = computationRuntime.getVertices().values().stream().map(value -> {
                ArrayList<Tuple> returnTuples = new ArrayList<>();
                for (String edgeLink: value.getEdges()) {
                    //Add edges only from source
                    if (((VertexM)value).getLabelEdge(edgeLink, JobManagerActor.DESTINATION_EDGE.first()) == null) {
                        Tuple t = Tuple.newInstance(this.tupleFields.size());
                        t.setField(new String[]{value.getNodeId()}, 0);
                        t.setField(new String[]{edgeLink}, 1);
                        for (int i = 2; i < t.getArity(); i++) {
                            t.setField(((VertexM)value).getLabelEdge(edgeLink, this.tupleFields.get(i)), i);
                        }
                        returnTuples.add(t);
                    }
                }
                return returnTuples;
            }).flatMap(list -> list.stream());
        }
    }

    public ExtractedStream(@Nullable Map<String, String> partition, ArrayList<String> tupleFields, StreamType streamType, Stream<Tuple> stream) {
        this.partition = ExtractedIf.initializePartitionIfNull(partition);
        this.tupleFields = tupleFields;
        this.streamType = streamType;
        this.stream = stream;
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
    public Tuple generateTuple(Tuple oldTuple, List<String[]> fields) {
        return getTuple(oldTuple, fields, streamType, tupleFields);
    }


    @Override
    public StreamType getStreamType() {
        return streamType;
    }

    @Override
    public void set(ExtractedIf extractedIf) {
        ExtractedStream extractedStream = (ExtractedStream) extractedIf;
        this.stream = extractedStream.stream;
        this.tupleFields = extractedStream.tupleFields;
        this.streamType = extractedStream.streamType;
        this.partition = extractedStream.partition;

    }

    public Map<String, String> getPartition() {
        return partition;
    }

    public Stream<Tuple> getStream(){
        return this.stream;
    }

    public ExtractedStream map(CustomFunction function, ArrayList<String> args){
        function.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Stream<Tuple> newStream = stream.map(function).filter(Objects::nonNull);
        return this.getExtractedStream(newStream, function.getNewFieldNames(this.streamType), this.streamType);
    }

    public Map<Tuple, Object> reduce(CustomBinaryOperator accumulator, ArrayList<String> args){
        accumulator.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Map<Tuple, Object> map = new HashMap<>();
        map.put(new Tuple0(), (Tuple) stream.reduce(accumulator.getIdentity(), accumulator, accumulator.getBinaryOperator()));
        return map;
    }

    public ExtractedStream flatternMultivalue(String key){
        Stream<Tuple> newStream = this.stream.map(tuple -> flattern(tuple, key)).flatMap(list -> list.stream());
        return getExtractedStream(newStream, this.tupleFields, this.streamType);
    }

    public ExtractedStream flatmap(CustomFlatMapper mapper, ArrayList<String> args){
        mapper.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
        Stream<Tuple> newStream = this.stream.flatMap(mapper).filter(Objects::nonNull);;
        return getExtractedStream(newStream, mapper.getNewFieldNames(this.streamType), this.streamType);
    }

    public ExtractedStream filter(CustomPredicate filterFunction, ArrayList<String> args){
        filterFunction.setLabels(this);
        arguments = ExtractedStream.solveArguments(tupleFields, args);
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
            Stream<Tuple> firstStream = this.flatternMultivalue(NODELABEL).stream;
            Stream<Tuple> secondStream = otherStream.flatternMultivalue(NODELABEL).stream;

            final int positionNodeThis = this.tupleFields.indexOf(NODELABEL);
            final int positionNodeOther = otherStream.tupleFields.indexOf(NODELABEL);

            Map<String, List<Tuple>> secondAsMap = secondStream.collect(Collectors.groupingBy(e -> ((String[]) e.getField(positionNodeOther))[0]));

            final Stream<Tuple> joinedStream = firstStream.map(tupleFirst -> {
                ArrayList<Tuple> joined = new ArrayList<>();
                List<Tuple> tuples = secondAsMap.get(tupleFirst.getField(positionNodeThis));
                if (tuples == null) return joined;
                for (Tuple other : tuples) {
                    Tuple newTuple = Tuple.newInstance(tupleFirst.getArity() + other.getArity() - 1);
                    int j;
                    for (j = 0; j < tupleFirst.getArity(); j++) {
                        newTuple.setField(tupleFirst.getField(j), j);
                    }
                    for (int i = j; i < newTuple.getArity(); i++) {
                        if (!(i == tupleFirst.getArity() + positionNodeOther)) {

                            newTuple.setField(other.getField(i - tupleFirst.getArity()), j);
                            j = j + 1;
                        }
                    }
                    joined.add(newTuple);
                }
                return joined;
            }).flatMap(Collection::stream);

            return new ExtractedStream(this.partition, newTupleFields, StreamType.NODE, joinedStream);

        } else if (this.streamType == StreamType.EDGE && otherStream.streamType == StreamType.EDGE){

            Stream<Tuple> firstStream = this.flatternMultivalue(NODELABEL).flatternMultivalue(EDGELABEL).stream;
            Stream<Tuple> secondStream = otherStream.flatternMultivalue(NODELABEL).flatternMultivalue(EDGELABEL).stream;

            final int positionNodeThis = this.tupleFields.indexOf(NODELABEL);
            final int positionNodeOther = otherStream.tupleFields.indexOf(NODELABEL);

            final int positionEdgeThis = this.tupleFields.indexOf(EDGELABEL);
            final int positionEdgeOther = otherStream.tupleFields.indexOf(EDGELABEL);


            Map<Tuple2<String, String>, List<Tuple>> secondAsMap =
                    secondStream
                            .collect(Collectors
                                    .groupingBy(e -> new Tuple2<>(
                                            ((String[]) e.getField(positionNodeOther))[0],
                                            ((String[]) e.getField(positionEdgeOther))[0])));

            final Stream<Tuple> joinedStream = firstStream.map(tupleFirst -> {
                ArrayList<Tuple> joined = new ArrayList<>();
                List<Tuple> tuples = secondAsMap.get(new Tuple2<>(tupleFirst.getField(positionNodeThis), tupleFirst.getField(positionEdgeThis)));
                if (tuples == null) return joined;
                for (Tuple other : tuples) {
                    Tuple newTuple = Tuple.newInstance(tupleFirst.getArity() + other.getArity() - 2);
                    int j;
                    for (j = 0; j < tupleFirst.getArity(); j++) {
                        newTuple.setField(tupleFirst.getField(j), j);
                    }
                    for (int i = j; i < newTuple.getArity(); i++) {
                        if (!(i == tupleFirst.getArity() + positionNodeOther) && !(i == tupleFirst.getArity() + positionEdgeOther)) {
                            newTuple.setField(other.getField(i - tupleFirst.getArity()), j);
                            j = j + 1;
                        }
                    }
                    joined.add(newTuple);
                }
                return joined;
            }).flatMap(Collection::stream);

            return new ExtractedStream(this.partition, newTupleFields, StreamType.EDGE, joinedStream);


        } else if (this.streamType == StreamType.AGGREGATE && otherStream.streamType != StreamType.AGGREGATE){

            return otherStream.joinStream(this);

        }else if (otherStream.streamType == StreamType.AGGREGATE) { //both aggregate or first not aggregate ans second aggregate

            List<Tuple> otherCollected = otherStream.stream.collect(Collectors.toList());

            Stream<Tuple> newStream = this.stream.map(thisTuple ->
                    otherCollected.stream()
                            .map(otherTuple -> {
                                Tuple result = Tuple.newInstance(thisTuple.getArity() + otherTuple.getArity());
                                for (int i = 0; i < thisTuple.getArity(); i++) {
                                    result.setField(thisTuple.getField(i), i);
                                }
                                for (int i = thisTuple.getArity(); i < result.getArity(); i++) {
                                    result.setField(otherTuple.getField(i-thisTuple.getArity()), i);
                                }
                                return result;
                            }))
                    .flatMap(stream -> stream);

            return new ExtractedStream(this.partition, newTupleFields, this.streamType, newStream);
            /*
                Stream<Tuple> newStream = otherStream.stream.map(
                        otherTuple -> this.stream.map(thisTuple -> {

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
             */
        }/*else if (this.streamType == StreamType.AGGREGATE && !(otherStream.streamType == StreamType.AGGREGATE)){

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

        }*/   else {
            throw new InvalidOperationChain("ERROR STATE ");
        }
    }

    public ExtractedIf groupby(String[] groupingLabels){ //NB: HashMap has hashing of the reference for the position of value

        //Flattern
        ExtractedStream extractedStream = this;
        for (String key: groupingLabels) {
            extractedStream = flatternMultivalue(key);
        }

        //Group
        Map<Tuple, List<Tuple>> newStream = extractedStream.stream
                .collect(Collectors.groupingByConcurrent(tuple -> {
                    Tuple key = Tuple.newInstance(groupingLabels.length);
                    for (int i = 0; i < groupingLabels.length; i++) {
                        String label = groupingLabels[i];
                        int position = this.tupleFields.indexOf(label);
                        key.setField(((String[])tuple.getField(position))[0], i);
                    }
                    return key;
                }, Collectors.toList()));

        return new ExtractedGroupedStream(this.partition, (ArrayList<String>)this.tupleFields.clone(), this.streamType, newStream);
    }

    public ExtractedStream merge (String[] groupingLabels) {

        //Flattern
        for (String key: groupingLabels) {
            this.stream = flatternMultivalue(key).stream;
        }

        Map<Tuple, List<Tuple>> groups = this.stream.collect(Collectors.groupingBy(tuple -> {
            Tuple key = Tuple.newInstance(groupingLabels.length);
            for (int i = 0; i < groupingLabels.length; i++) {
                String label = groupingLabels[i];
                int position = this.tupleFields.indexOf(label);
                key.setField(((String[])tuple.getField(position))[0], i);
            }
            return key;
        }, Collectors.toList()));


        //Unify in one tuple each group

        List<Tuple> result = groups.values().stream().map(list -> {
            Tuple identity = Tuple.newInstance(list.get(0).getArity());
            for (int i = 0; i < identity.getArity(); i++) {
                identity.setField(new String[0], i);
            }
            return list.stream().reduce(identity, (tuple, tuple2) -> {
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

    public Variable emit(VariableSolver variableSolver, String varName, long persistence){

        if (streamType == StreamType.NODE) {

            Map<String, Tuple> map = this.stream.collect(Collectors.toMap(tuple -> {
                if(tuple.getField(0) instanceof String[]){
                    return ((String[])tuple.getField(0))[0];
                } else { //String
                    return tuple.getField(0);
                }
                    }, tuple -> {
                Tuple newTuple = Tuple.newInstance(tuple.getArity() - 1);
                for (int i = 0; i < newTuple.getArity(); i++) {
                    newTuple.setField(tuple.getField(i+1), i);
                }
                return newTuple;
            }));

            ArrayList<String> fieldNames = new ArrayList<>();
            for (int i = 1; i < this.tupleFields.size(); i++) {
                fieldNames.add(this.tupleFields.get(i));
            }

            return new VariableVertex(varName, persistence, variableSolver.getCurrentTimestamp(), map, fieldNames.toArray(String[]::new));

        } else if (streamType == StreamType.EDGE) {

            //Map<String, List<Tuple>> groupedVertex = this.stream.collect(Collectors.groupingByConcurrent(tuple -> tuple.getField(0), Collectors.toList()));
            final ConcurrentHashMap<String, Map<String, Tuple>> vertexMap = new ConcurrentHashMap<>();
            this.stream.forEach(tuple-> {

                Map<String, Tuple> edges;
                String src, dest;
                if(tuple.getField(0) instanceof String[]){
                    src =  ((String[])tuple.getField(0))[0];
                } else { //String
                    src =  tuple.getField(0);
                }

                edges = vertexMap.computeIfAbsent(src, v -> new ConcurrentHashMap<>());

                //Compute key (edgeName)
                if(tuple.getField(1) instanceof String[]){
                    dest =  ((String[])tuple.getField(1))[0];
                } else { //String
                    dest =  tuple.getField(1);
                }

                //Compute value tuple
                Tuple newTuple = Tuple.newInstance(tuple.getArity() - 2);
                for (int i = 0; i < newTuple.getArity(); i++) {
                    newTuple.setField(tuple.getField(i+2), i);
                }

                edges.put(dest, newTuple);
            });

            ArrayList<String> fieldNames = new ArrayList<>();
            for (int i = 2; i < this.tupleFields.size(); i++) {
                fieldNames.add(this.tupleFields.get(i));
            }

            return new VariableEdge(varName, persistence, variableSolver.getCurrentTimestamp(), vertexMap, fieldNames.toArray(String[]::new));

        } else { //Aggregate
            Tuple[] tuples = this.stream.collect(Collectors.toList()).toArray(Tuple[]::new);
            return new VariableAggregate(varName, persistence, variableSolver.getCurrentTimestamp(), tuples, tupleFields);
        }


    }

    //Support methods

    private ExtractedStream getExtractedStream(Stream<Tuple> stream, ArrayList<String> tupleFields, StreamType streamType){
        return new ExtractedStream(this.partition, (ArrayList<String>)tupleFields.clone(), streamType, stream);
    }

    /**
     * Tuple2: {[hello], [a, b, c]} on second key -> {[hello], [a]} {[hello], [b]} {[hello], [c]}
     * @param tuple
     * @param key
     * @return A tuple for each value of key field
     */
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

    /**
     * Ne tuple field for join
     * @param tupleFields1
     * @param tupleFields2
     * @param first
     * @param second
     * @return
     */
    private ArrayList<String> newTupleFields(ArrayList<String> tupleFields1, ArrayList<String> tupleFields2, StreamType first, StreamType second){

        ArrayList<String> fieldsSecond = (ArrayList<String>) tupleFields2.clone();

        ArrayList<String> newTupleFields = new ArrayList<>(tupleFields1.stream().map(field -> JOIN_FIRST + field).collect(Collectors.toList()));

        if (second == StreamType.NODE || second == StreamType.EDGE) fieldsSecond.remove(NODELABEL);
        if (second == StreamType.EDGE) fieldsSecond.remove(EDGELABEL);

        newTupleFields.addAll(fieldsSecond.stream().map(field -> JOIN_SECOND + field).collect(Collectors.toList()));

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

    static int[] solveArguments (ArrayList<String> fieldNames, ArrayList<String> argsNames){

        if ( argsNames == null || argsNames.isEmpty()){
            return null;
        }

        int[] result = new int[argsNames.size()];
        for (int i = 0; i < argsNames.size(); i++) {
            result[i] = fieldNames.indexOf(argsNames.get(i));
            if (result[i] == -1)
                throw new RuntimeException("ExtractedStream.solveArguments: mapping "+ fieldNames.toString() + " to " + argsNames.toString() + " failed");
        }
        return result;
    }
    static Tuple getTuple(Tuple oldTuple, List<String[]> fields, ExtractedStream.StreamType streamType, ArrayList<String> tupleFields) {
        Tuple result;

        if (streamType == ExtractedStream.StreamType.NODE) {
            result = Tuple.newInstance(fields.size()+1);
            result.setField(oldTuple.getField(tupleFields.indexOf(NODELABEL)), 0);

            for (int i = 0; i < fields.size(); i++) {
                result.setField(fields.get(i), i + 1);
            }
        } else if (streamType == ExtractedStream.StreamType.EDGE) {
            result = Tuple.newInstance(fields.size()+2);
            result.setField(oldTuple.getField(tupleFields.indexOf(NODELABEL)), 0);
            result.setField(oldTuple.getField(tupleFields.indexOf(EDGELABEL)), 1);

            for (int i = 0; i < fields.size(); i++) {
                result.setField(fields.get(i), i + 2);
            }
        } else if (streamType == ExtractedStream.StreamType.AGGREGATE) {
            result = Tuple.newInstance(fields.size());
            for (int i = 0; i < fields.size(); i++) {
                result.setField(fields.get(i), i);
            }
        } else {
            throw new RuntimeException("Unrecognized stream type: " + streamType);
        }

        return result;
    }


}

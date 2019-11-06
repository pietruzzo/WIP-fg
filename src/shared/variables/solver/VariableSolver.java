package shared.variables.solver;

import jdk.internal.jline.internal.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import shared.Utils;
import shared.data.CompositeKey;
import shared.data.MultiKeyMap;
import shared.exceptions.VariableNotDefined;
import shared.exceptions.WrongTypeRuntimeException;
import shared.streamProcessing.ExtractedStream;
import shared.variables.*;
import shared.selection.SelectionSolver.Operation.WindowType;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VariableSolver {


    //Done : use tail with navigableTreeMap (navigable can't be final)
  //  private final HashMap<String , LinkedList<Variable>> variables;
    private final HashMap<String, NavigableMap<Long, Variable>> varablesNew;
    private long currentTimestamp;

    public VariableSolver() {
        //this.variables = new HashMap<>();
        this.varablesNew = new HashMap<>();
    }

    public String[] getAggregate(String variableName, @Nullable Map<String, String> partition){
        return this.getAggregate(variableName, partition, null);
    }
    public String[] getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName){
        return this.getVertexVariable(variableName, partition, vertexName, null);
    }
    public String[] getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName){
        return this.getEdgeVariable(variableName, partition, vertexName, edgeName, null);
    }

    public String[] getAggregate(String variableName, @Nullable Map<String, String> partition, @Nullable String field){
        List<String[]> result = this.getAggregate(variableName, field, partition, null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }
    public String[] getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, @Nullable String field){
        List<String[]> result = this.getVertexVariable(variableName, partition, vertexName, field,  null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }
    public String[] getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName, @Nullable String field){
        List<String[]> result = this.getEdgeVariable(variableName, partition, vertexName, edgeName, field,  null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }

    /**
     *
     * @param variableName
     * @param partition if defined, try to identify the partition
     * @param timeWindow if null use now
     * @param windowType if null use AGO
     * @throws WrongTypeRuntimeException variable is of wrong type
     * @throws VariableNotDefined if variable name is not registered
     * @return
     */
    public List<String[]> getAggregate(String variableName, @Nullable String field, @Nullable Map<String, String> partition, @Nullable String timeWindow, @Nullable WindowType windowType){

        List<Variable> selectedVars = getSelectedVariable(variableName, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableAggregate)) throw new WrongTypeRuntimeException(VariableAggregate.class, variable.getClass());
            if (field == null) result.add(((VariableAggregate) variable).getSingleValue());
            else result.add(((VariableAggregate) variable).getFieldValue(field));
        }
        return result;
    }

    /**
     *
     * @param variableName
     * @param partition if defined, try to identify the partition
     * @param timeWindow if null use now
     * @param windowType if null use AGO
     * @return
     */
    public List<String[]> getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, @Nullable String field, @Nullable String timeWindow, @Nullable WindowType windowType){

        List<Variable> selectedVars = getSelectedVariable(variableName, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableVertex)) throw new WrongTypeRuntimeException(VariableVertex.class, variable.getClass());

            if (field == null) result.add(((VariableVertex) variable).getVertexSingleValue(vertexName));
            else result.add(((VariableVertex) variable).getTupleField(vertexName, field));
        }
        return result;
    }

    /**
     *
     * @param variableName
     * @param partition if defined, try to identify the partition
     * @param timeWindow if null use now
     * @param windowType if null use AGO
     * @return
     */
    public List<String[]> getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName , @Nullable String field, @Nullable String timeWindow, @Nullable WindowType windowType){

        List<Variable> selectedVars = getSelectedVariable(variableName, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableEdge)) throw new WrongTypeRuntimeException(VariableEdge.class, variable.getClass());

            if (field == null) result.add(((VariableEdge) variable).getSingleVarValue(vertexName, edgeName));
            else result.add(((VariableEdge) variable).getTupleField(vertexName, edgeName, field));

        }
        return result;
    }

    /**
     *
     * @param variableName
     * @param windowType
     * @param timestamp
     * @return NULL if Empty
     */
    public MultiKeyMap<ExtractedStream>getStream(String variableName, WindowType windowType, String timestamp) {

        MultiKeyMap<Stream<Tuple>> result;

        ArrayList<Variable> variableVersions = this.extractWindow(this.varablesNew.get(variableName), Utils.solveTime(timestamp), windowType);
        if (variableVersions.isEmpty()) return null;

        if (variableVersions.get(0) instanceof VariablePartition) {
            result = new MultiKeyMap<>(((VariablePartition) variableVersions.get(0)).getAllInsideVariables().getKeys());

            List<Tuple2<Map<String, String>, Tuple>> collect = variableVersions.stream().map(variable -> { //FOR EACH VERSION
                VariablePartition variablePartition = (VariablePartition) variable;
                return variablePartition.getAllInsideVariables().getAllElements().entrySet().stream().map(entry -> {  //FOR EACH PARTITION
                    Stream<Tuple2<Map<String, String>, Tuple>> t = getStreamFromVariable(entry.getValue(), entry.getKey().getKeysMapping());
                    return t;
                }); //RETURN THE LIST OF TUPLE2: partition, stream<Tuples>
            }).flatMap(stream->stream).flatMap(stream -> stream).collect(Collectors.toList());

            ConcurrentMap<Map<String, String>, List<Tuple2<Map<String, String>, Tuple>>> byPartition = collect.parallelStream().collect(Collectors.groupingByConcurrent(tuple2 -> tuple2.f0, Collectors.toList()));
            byPartition.entrySet().parallelStream().map(entry -> {
                if (variableVersions.get(0) instanceof VariableAggregate){
                    //Group all fields
                    return new Tuple2<>(entry.getKey(), groupFields(entry.getValue().parallelStream().map(partitionTuple-> partitionTuple.f1), 0));
                } else if (variableVersions.get(0) instanceof VariableVertex){
                    //Group by 0 and group other fields
                    return new Tuple2<>(entry.getKey(), groupFields(entry.getValue().parallelStream().map(partitionTuple-> partitionTuple.f1), 1));
                } else {
                    //Group by 0, 1 and group other fields
                    return new Tuple2<>(entry.getKey(), groupFields(entry.getValue().parallelStream().map(partitionTuple-> partitionTuple.f1), 2));
                }
            }).forEach(tuple -> {
                result.putValue(new HashMap<>(tuple.f0), tuple.f1);
            });
        } else {
            result = new MultiKeyMap<>(new String[]{"all"});
            HashMap<String, String> compositeKey = new HashMap<>();
            compositeKey.put("all", "all");

            Stream<Tuple> singlePartitionStream = variableVersions.stream().map(variable -> {
                HashMap<String, String> partition = new HashMap<>();
                partition.put("all", "all");
                Stream<Tuple2<Map<String, String>, Tuple>> streamFromVariable = getStreamFromVariable(variable, partition);

                return streamFromVariable.map(partitionTuple -> partitionTuple.f1);
            }).flatMap(tuple -> tuple);

            if (variableVersions.get(0) instanceof VariableAggregate){
                //Group all fields
                result.putValue(compositeKey, groupFields(singlePartitionStream, 0));
            } else if (variableVersions.get(0) instanceof VariableVertex){
                //Group by 0 and group other fields
                result.putValue(compositeKey, groupFields(singlePartitionStream, 1));
            } else {
                //Group by 0, 1 and group other fields
                result.putValue(compositeKey, groupFields(singlePartitionStream, 2));
            }

        }

        //region: Extract Fields and Type
        Variable variable;
        final ArrayList<String> fields = new ArrayList<>();
        ExtractedStream.StreamType streamType;
        if (variableVersions.get(0) instanceof VariablePartition) {
            variable = ((VariablePartition)variableVersions.get(0)).getAllInsideVariables().getAllElements().values().iterator().next();
        } else {
            variable = variableVersions.get(0);
        }

        if (variable instanceof VariableAggregate) {
            fields.addAll(((VariableAggregate)variable).getTupleNames());
            streamType = ExtractedStream.StreamType.AGGREGATE;
        }
        else if (variable instanceof VariableVertex) {
            fields.add(ExtractedStream.NODELABEL);
            fields.addAll(((VariableAggregate)variable).getTupleNames());
            streamType = ExtractedStream.StreamType.NODE;
        }
        else if (variable instanceof VariableEdge) {
            fields.add(ExtractedStream.NODELABEL);
            fields.add(ExtractedStream.EDGELABEL);
            fields.addAll(((VariableAggregate)variable).getTupleNames());
            streamType = ExtractedStream.StreamType.EDGE;
        } else {
            throw new WrongTypeRuntimeException(Variable.class, variable.getClass());
        }

        //endregion

        MultiKeyMap<ExtractedStream> extractedStream = new MultiKeyMap<>(result.getKeys());

        result.getAllElements().entrySet().parallelStream().forEach(element -> {
            if (variableVersions.get(0) instanceof VariablePartition) {
                extractedStream.putValue(element.getKey().getKeysMapping(), new ExtractedStream(element.getKey().getKeysMapping(), fields, streamType, element.getValue()));
            } else {
                extractedStream.putValue(element.getKey().getKeysMapping(), new ExtractedStream(null, fields, streamType, element.getValue()));
            }
        });
        return extractedStream;
    }

    /**
     *
     * @param tupleStream stream of tuples
     * @param tokeep number of identificator fields that won't be merged
     * @return stream of tuples with merged fields
     */
    private Stream<Tuple> groupFields(Stream<Tuple> tupleStream, int tokeep){

        ConcurrentMap<CompositeKey, List<Tuple>> grouped = tupleStream.collect(Collectors.groupingByConcurrent(tuple -> {
            HashMap<String, String> key = new HashMap<>();
            for (int i = 0; i < tokeep; i++) {
                key.put(String.valueOf(i), ((String[]) tuple.getField(i))[0]);
            }
            return new CompositeKey(key);
        }, Collectors.toList()));

        return grouped.entrySet().parallelStream().map(entryList -> {
            return entryList.getValue().parallelStream().reduce((tuple1, tuple2)-> {
                for (int i = 0; i < tuple1.getArity(); i++) {
                    tuple1.getField(i);
                    tuple2.getField(i);
                    String[] concatenated = Arrays.copyOf((String[]) tuple1.getField(i), ((String[]) tuple1.getField(i)).length + ((String[]) tuple2.getField(i)).length);
                    System.arraycopy(tuple2.getField(i), 0, concatenated, ((String[]) tuple1.getField(i)).length, ((String[]) tuple2.getField(i)).length);
                    tuple1.setField(concatenated, i);
                }
                return tuple1;
            }).get();
        });
    }

    private Stream<Tuple2<Map<String, String>, Tuple>> getStreamFromVariable(Variable insideVariable, HashMap<String, String> partition){
        if (insideVariable instanceof VariableVertex) {
            List<Tuple2<String, Tuple>> groupedByEdge = ((VariableVertex) insideVariable).getVerticesValues().entrySet().parallelStream()
                    .map(vertex -> new Tuple2<>(vertex.getKey(), vertex.getValue()))
                    .collect(Collectors.toList());

            return groupedByEdge.parallelStream().map(t2 -> {
                Tuple result = Tuple.newInstance(t2.f1.getArity() + 2);
                result.setField(new String[]{t2.f0}, 0);
                for (int i = 1; i < result.getArity(); i++) {
                    result.setField(t2.f1.getField(i - 2), i);
                }
                return result;
            }).map(tuple -> new Tuple2<>(partition, tuple));

        }
        else if (insideVariable instanceof VariableEdge) {
            List<Tuple3<String, String, Tuple>> groupedByEdge = ((VariableEdge) insideVariable).getEdgesValues().entrySet().parallelStream().map(vertex -> {
                //For each vertex -> list of edge, tuple
                return vertex.getValue().entrySet().stream().map(v -> new Tuple3<>(vertex.getKey(), v.getKey(), v.getValue())).collect(Collectors.toList());
            }).flatMap(list -> list.parallelStream()).collect(Collectors.toList());

            return groupedByEdge.parallelStream().map(t3 -> {
                Tuple result = Tuple.newInstance(t3.f2.getArity() + 2);
                result.setField(new String[]{t3.f0}, 0);
                result.setField(new String[]{t3.f1}, 1);
                for (int i = 2; i < result.getArity(); i++) {
                    result.setField(t3.f2.getField(i - 2), i);
                }
                return result;
            }).map(tuple -> new Tuple2<>(partition, tuple));
        }
        else if (insideVariable instanceof VariableAggregate) {
            return Arrays.stream(((VariableAggregate) insideVariable).getValue()).map(t -> new Tuple2<>(partition, t));
        }
        else {
            System.out.println("Variable can be AGGREGATE, NODE or EDGE");
            System.out.println("VariableSolver.getStream");
            throw new WrongTypeRuntimeException(Variable.class, insideVariable.getClass());

        }
    }

    /**
     *
     * @param variableName
     * @param partition
     * @param timeWindow
     * @param windowType
     * @throws VariableNotDefined if variable isn't defined
     * @return Can return empty set
     */
    private List<Variable> getSelectedVariable (String variableName, @Nullable Map<String, String> partition, String timeWindow, WindowType windowType) throws VariableNotDefined {
        Long timeWindowL = null;
        if (timeWindow != null) timeWindowL = Utils.solveTime(timeWindow);
        NavigableMap<Long, Variable> entireVarList = this.varablesNew.get(variableName);
        if (entireVarList == null) throw new VariableNotDefined(variableName);
        List<Variable> windowed = extractWindow(entireVarList, timeWindowL, windowType);

        if (!(windowed.isEmpty()) && partition!= null && windowed.get(0) instanceof VariablePartition){
            List<VariablePartition> toBeExtractFromPartition = windowed.stream().map(variable -> (VariablePartition) variable).collect(Collectors.toList());
            windowed = extractfromPartition(toBeExtractFromPartition, partition);
        }

        return windowed;

    }
    /**
     * @param selectedVars not null
     * @param timeWindow @Nullable ->take latest
     * @param windowType @NotNull
     * @return
     */
    private ArrayList<Variable>extractWindow(LinkedList<Variable> selectedVars, Long timeWindow, WindowType windowType){

        ArrayList<Variable> result = new ArrayList<>();
        if (timeWindow == null) timeWindow = 0L;
        if (windowType == null) windowType = WindowType.AGO;

        long timeSolved = this.currentTimestamp - timeWindow;

        Iterator<Variable> iterator = selectedVars.descendingIterator();
        Variable current = null;

        while (iterator.hasNext() && timeSolved < (current = iterator.next()).getTimestamp()){
            if (windowType == WindowType.WITHIN || windowType == WindowType.EVERYWITHIN)
                result.add(current);
        }
        if (windowType == WindowType.AGO && current != null && timeSolved >= (current = iterator.next()).getTimestamp()) {
            result.add(current);
        }
        return result;
    }

    private ArrayList<Variable>extractWindow(NavigableMap<Long, Variable> selectedVars, Long timeWindow, WindowType windowType){

        ArrayList<Variable> result = new ArrayList<>();
        if (timeWindow == null) timeWindow = 0L;
        if (windowType == null) windowType = WindowType.AGO;

        long timeSolved = this.currentTimestamp - timeWindow;

        if (windowType == WindowType.WITHIN || windowType == WindowType.EVERYWITHIN) {
            selectedVars.tailMap(selectedVars.floorKey(timeSolved), true).values().stream().forEach(variable -> {
                result.add(variable);
            });
        } else {
            result.add(selectedVars.floorEntry(timeSolved).getValue());
        }
        while(result.removeAll(null)){}

        return result;
    }

    /**
     * Extract variable list from partition list
     * @param partitions
     * @throws IllegalArgumentException wrong key format
     * @return
     */
    private List<Variable> extractfromPartition(List<VariablePartition> partitions, Map<String, String> partition) throws IllegalArgumentException{
        ArrayList<Variable> result = new ArrayList<>();
        for (VariablePartition varp: partitions) {
            result.add(varp.getInsideVariable(partition));
        }
        return result;
    }

    /**
     * It assums increasing timestamp order
     * @param variable
     */
    public void addVariable(Variable variable) {
        //this.variables.computeIfAbsent(variable.getName(), k -> new LinkedList<>());
        //this.variables.get(variable.getName()).add(variable);

        this.varablesNew.computeIfAbsent(variable.getName(), k -> new TreeMap<>());
        this.varablesNew.get(variable.getName()).put(variable.getTimestamp(), variable);
    }

    public void removeOldVersions() {
        //long keepTimestamp;
        //int versionsToDelete;
        /*
        for (LinkedList<Variable> list: this.variables.values()) {
            versionsToDelete = 0;
            keepTimestamp = this.currentTimestamp - list.peekLast().getPersistence();
            Variable current = null, next = null;
            Iterator<Variable> iterator = list.iterator();
            while (iterator.hasNext()){
                current = next;
                next = iterator.next();
                if (current!=null && next.getTimestamp() < keepTimestamp) {
                    versionsToDelete = versionsToDelete + 1;
                }
                if (next.getTimestamp() > keepTimestamp) break;
            }
            for (int i = 0; i < versionsToDelete; i++) {
                list.removeFirst();
            }
        }
         */
        this.varablesNew.entrySet().parallelStream().forEach(entry -> {
            NavigableMap<Long, Variable> versions = entry.getValue();
            long keepTimestamp = this.currentTimestamp - versions.get(versions.lastKey()).getPersistence();
            long lastToKeep = versions.floorKey(keepTimestamp);
            versions = versions.tailMap(lastToKeep, true);
            entry.setValue(versions);
        });
    }

    /**
     * Expressed in ms, it need to be set manually in every new timestamp
     */

    public void setCurrentTimestamp (long timestamp) {
        this.currentTimestamp = timestamp;
    }

    public long getCurrentTimestamp() { return this.currentTimestamp;}

    public Tuple2<String, String> solveFields(String variableName) {
        String[] elements = variableName.split(".");
        if (elements.length) // todo Complete field solving
    }
}

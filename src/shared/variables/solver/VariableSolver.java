package shared.variables.solver;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.jetbrains.annotations.Nullable;
import shared.Utils;
import shared.computation.Vertex;
import shared.data.CompositeKey;
import shared.data.MultiKeyMap;
import shared.exceptions.VariableNotDefined;
import shared.exceptions.WrongTypeRuntimeException;
import shared.selection.SelectionSolver.Operation.WindowType;
import shared.streamProcessing.ExtractedIf;
import shared.streamProcessing.ExtractedStream;
import shared.variables.*;

import javax.naming.OperationNotSupportedException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VariableSolver implements Serializable {


    //Done : use tail with navigableTreeMap (navigable can't be final)
  //  private final HashMap<String , LinkedList<Variable>> variables;
    private final HashMap<String, NavigableMap<Long, Variable>> varablesNew;
    private long currentTimestamp;

    public VariableSolver() {
        //this.variables = new HashMap<>();
        this.varablesNew = new HashMap<>();
    }


    public String[] getAggregate(String variableName, @Nullable Map<String, String> partition){

        List<String[]> result = this.getAggregate(variableName, partition, null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }
    public String[] getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName){
        List<String[]> result = this.getVertexVariable(variableName, partition, vertexName,  null, null);
        if (result.size() == 1) return result.get(0);
        else if (result.isEmpty()) return new String[0];
        else throw new RuntimeException("result size is expected to be 0 or 1");
    }
    public String[] getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName){
        List<String[]> result = this.getEdgeVariable(variableName, partition, vertexName, edgeName,  null, null);
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
    public List<String[]> getAggregate(String variableName, @Nullable Map<String, String> partition, @Nullable String timeWindow, @Nullable WindowType windowType){

        Tuple2<String, String> variableField = this.solveFields(variableName);
        String field = variableField.f1;
        String variableNameSolved = variableField.f0;


        List<Variable> selectedVars = getSelectedVariable(variableNameSolved, partition, timeWindow, windowType);
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
    public List<String[]> getVertexVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, @Nullable String timeWindow, @Nullable WindowType windowType){

        Tuple2<String, String> variableField = this.solveFields(variableName);
        String field = variableField.f1;
        String variableNameSolved = variableField.f0;

        List<Variable> selectedVars = getSelectedVariable(variableNameSolved, partition, timeWindow, windowType);
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
    public List<String[]> getEdgeVariable(String variableName, @Nullable Map<String, String> partition, String vertexName, String edgeName, @Nullable String timeWindow, @Nullable WindowType windowType){

        Tuple2<String, String> variableField = this.solveFields(variableName);
        String field = variableField.f1;
        String variableNameSolved = variableField.f0;

        List<Variable> selectedVars = getSelectedVariable(variableNameSolved, partition, timeWindow, windowType);
        List<String[]> result = new ArrayList<>();

        for (Variable variable: selectedVars) {
            if (! (variable instanceof VariableEdge)) throw new WrongTypeRuntimeException(VariableEdge.class, variable.getClass());

            if (field == null) result.add(((VariableEdge) variable).getSingleVarValue(vertexName, edgeName));
            else result.add(((VariableEdge) variable).getTupleField(vertexName, edgeName, field));

        }
        return result;
    }

    public MultiKeyMap<Map<String, Vertex>> getGraphs (String variableName, @Nullable String timeAgo){

        List<Variable> graph = this.getSelectedVariable(variableName, null, null, WindowType.AGO);

        if (!(graph.get(0) instanceof VariableGraph)) throw new WrongTypeRuntimeException(VariableGraph.class, graph.getClass());

        return ((VariableGraph)graph.get(0)).getSavedPartitions();
    }

    public enum VariableType { AGGREGATE, EDGE, GRAPH, PARTITION, VERTEX };

    /**
     * Add values to variable, create variable or partition if not present
     * @implSpec This method is only supporting VariableVertex right now
     * @param varNamePersistence
     * @param type
     * @param partition if null -> no variable partition
     * @param values vertex name, associated values
     */
    public void addToVariable (Tuple2<String, Long> varNamePersistence, VariableType type,  @Nullable Map<String, String> partition, Tuple2< String, List<String>> values) throws OperationNotSupportedException {

        Variable variable;
        String variableName = varNamePersistence.f0;

        //Get variable

            //Create variable/partition if not present
            this.varablesNew.putIfAbsent(variableName, new TreeMap<>());

            variable = this.varablesNew.get(variableName).get(this.currentTimestamp);

            if (variable == null){

                switch (type) {
                    case VERTEX:
                        variable = new VariableVertex(variableName, varNamePersistence.f1, this.currentTimestamp, new HashMap<>(), variableName);
                        break;
                    case AGGREGATE:
                        throw new OperationNotSupportedException("addToVariable method doesn't support aggregates right now");
                    case EDGE:
                        throw new OperationNotSupportedException("addToVariable method doesn't support edge variable right now");
                    case GRAPH:
                        throw new OperationNotSupportedException("addToVariable method doesn't support graphs right now");
                    default:
                        throw new OperationNotSupportedException("type not supported: " + type);
                }

                if (partition == null) {
                    this.varablesNew.get(variableName).put(this.currentTimestamp, variable);
                } else {
                    //Create Partition Variable and add Variable

                    MultiKeyMap<Variable> partitionVar = new MultiKeyMap<>(partition.keySet().toArray(String[]::new));
                    partitionVar.putValue(new HashMap<>(partition), variable);

                    this.varablesNew.get(variableName).put(
                            this.currentTimestamp,
                            new VariablePartition(variableName, varNamePersistence.f1, this.currentTimestamp, partitionVar)
                    );

                }
        }

        // vet variable inside if partition
        if (variable instanceof VariablePartition){
            VariablePartition varP = (VariablePartition) variable;
            variable = varP.getInsideVariable(partition);

            if (variable == null) {
                //Create partition for partition variable
                variable = new VariableVertex(variableName, varNamePersistence.f1, this.currentTimestamp, new HashMap<>(), variableName);
                varP.getAllInsideVariables().putValue(new HashMap<>(partition), variable);
            }
        }
        // variable has been created -> now put values
        if (variable instanceof VariableVertex) {

            VariableVertex varVertex = (VariableVertex) variable;
            varVertex.addValuesToOneField(values.f0, values.f1);

        } else {
            throw new OperationNotSupportedException("type not supported (VariableVertex only): " + type);
        }

    }

    /**
     *
     * @param variableName
     * @param windowType
     * @param timestamp
     * @return NULL if Empty
     */
    public MultiKeyMap<ExtractedIf>getStream(String variableName, WindowType windowType, String timestamp) {

        MultiKeyMap<Stream<Tuple>> result;

        Tuple2<String, String> variableField = this.solveFields(variableName);
        String selectedField = variableField.f1;
        String variableNameSolved = variableField.f0;

        ArrayList<Variable> variableVersions = this.extractWindow(this.varablesNew.get(variableNameSolved), Utils.solveTime(timestamp), windowType);
        if (variableVersions.isEmpty()) return null;

        if (variableVersions.get(0) instanceof VariablePartition) {
            result = new MultiKeyMap<>(((VariablePartition) variableVersions.get(0)).getAllInsideVariables().getKeys());

            List<Tuple2<Map<String, String>, Tuple>> collect = variableVersions.stream().map(variable -> { //FOR EACH VERSION
                VariablePartition variablePartition = (VariablePartition) variable;
                return variablePartition.getAllInsideVariables().getAllElements().entrySet().stream().map(entry -> {  //FOR EACH PARTITION
                    return getStreamFromVariable(entry.getValue(), entry.getKey().getKeysMapping(), selectedField);
                }); //RETURN THE LIST OF TUPLE2: partition, stream<Tuples>
            }).flatMap(stream->stream).flatMap(stream -> stream).collect(Collectors.toList());

            ConcurrentMap<Map<String, String>, List<Tuple2<Map<String, String>, Tuple>>> byPartition = collect.stream().collect(Collectors.groupingByConcurrent(tuple2 -> tuple2.f0, Collectors.toList()));
            byPartition.entrySet().stream().map(entry -> {
                if (variableVersions.get(0) instanceof VariableAggregate){
                    //Group all fields
                    return new Tuple2<>(entry.getKey(), groupFields(entry.getValue().stream().map(partitionTuple-> partitionTuple.f1), 0));
                } else if (variableVersions.get(0) instanceof VariableVertex){
                    //Group by 0 and group other fields
                    return new Tuple2<>(entry.getKey(), groupFields(entry.getValue().stream().map(partitionTuple-> partitionTuple.f1), 1));
                } else {
                    //Group by 0, 1 and group other fields
                    return new Tuple2<>(entry.getKey(), groupFields(entry.getValue().stream().map(partitionTuple-> partitionTuple.f1), 2));
                }
            }).forEach(tuple -> {
                result.putValue(new HashMap<>(tuple.f0), tuple.f1);
            });
        } else { //If not variable partition
            result = new MultiKeyMap<>(new String[]{"all"});
            HashMap<String, String> compositeKey = new HashMap<>();
            compositeKey.put("all", "all");

            Stream<Tuple> singlePartitionStream = variableVersions.stream().map(variable -> {
                HashMap<String, String> partition = new HashMap<>();
                partition.put("all", "all");
                Stream<Tuple2<Map<String, String>, Tuple>> streamFromVariable = getStreamFromVariable(variable, partition, selectedField);

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
            fields.addAll(((VariableVertex)variable).getTupleNames());
            streamType = ExtractedStream.StreamType.NODE;
        }
        else if (variable instanceof VariableEdge) {
            fields.add(ExtractedStream.NODELABEL);
            fields.add(ExtractedStream.EDGELABEL);
            fields.addAll(((VariableEdge)variable).getTupleNames());
            streamType = ExtractedStream.StreamType.EDGE;
        } else {
            throw new WrongTypeRuntimeException(Variable.class, variable.getClass());
        }

        //endregion

        MultiKeyMap<ExtractedIf> extractedStream = new MultiKeyMap<>(result.getKeys());

        result.getAllElements().entrySet().stream().forEach(element -> {
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

        return grouped.entrySet().stream().map(entryList ->
                entryList.getValue().stream().reduce((tuple1, tuple2)-> {
                    for (int i = 0; i < tuple1.getArity(); i++) {
                        tuple1.getField(i);
                        tuple2.getField(i);
                        String[] concatenated = Arrays.copyOf((String[]) tuple1.getField(i), ((String[]) tuple1.getField(i)).length + ((String[]) tuple2.getField(i)).length);
                        System.arraycopy(tuple2.getField(i), 0, concatenated, ((String[]) tuple1.getField(i)).length, ((String[]) tuple2.getField(i)).length);
                        tuple1.setField(concatenated, i);
                    }
                    return tuple1;
                }).get());
    }

    private Stream<Tuple2<Map<String, String>, Tuple>> getStreamFromVariable(Variable insideVariable, HashMap<String, String> partition, @Nullable String field){
        if (insideVariable instanceof VariableVertex) {
            List<Tuple2<String, Tuple>> groupedByVertex = ((VariableVertex) insideVariable).getVerticesValues().entrySet().stream()
                    .map(vertex -> new Tuple2<>(vertex.getKey(), vertex.getValue()))
                    .collect(Collectors.toList());

            return groupedByVertex.stream().map(t2 -> {
                Tuple result;
                if (field == null) { //Get all fields
                    result = Tuple.newInstance(t2.f1.getArity() + 1);
                    result.setField(new String[]{t2.f0}, 0);
                    for (int i = 1; i < result.getArity(); i++) {
                        result.setField(t2.f1.getField(i - 1), i);
                    }
                } else { //Get only one field
                    result = Tuple.newInstance(2);
                    result.setField(new String[]{t2.f0}, 0);
                    result.setField(((VariableVertex) insideVariable).getTupleField(t2.f0, field), 1);
                }
                return result;
            }).map(tuple -> new Tuple2<>(partition, tuple));

        }
        else if (insideVariable instanceof VariableEdge) {
            List<Tuple3<String, String, Tuple>> groupedByEdge = ((VariableEdge) insideVariable).getEdgesValues().entrySet().stream().map(vertex -> {
                //For each vertex -> list of edge, tuple
                return vertex.getValue().entrySet().stream().map(v -> new Tuple3<>(vertex.getKey(), v.getKey(), v.getValue())).collect(Collectors.toList());
            }).flatMap(list -> list.stream()).collect(Collectors.toList());

            return groupedByEdge.stream().map(t3 -> {
                Tuple result;
                if(field == null) {
                    result = Tuple.newInstance(t3.f2.getArity() + 2);
                    result.setField(new String[]{t3.f0}, 0);
                    result.setField(new String[]{t3.f1}, 1);
                    for (int i = 2; i < result.getArity(); i++) {
                        result.setField(t3.f2.getField(i - 2), i);
                    }
                } else {//Get only one field
                    result = Tuple.newInstance(3);
                    result.setField(new String[]{t3.f0}, 0);
                    result.setField(new String[]{t3.f1}, 1);
                    result.setField(((VariableVertex) insideVariable).getTupleField(t3.f0, field), 2);
                }
                return result;
            }).map(tuple -> new Tuple2<>(partition, tuple));
        }
        else if (insideVariable instanceof VariableAggregate) {
            if (field == null) {
                return Arrays.stream(((VariableAggregate) insideVariable).getValue()).map(t -> new Tuple2<>(partition, t));
            } else {
                return Arrays.stream(((VariableAggregate) insideVariable).getValue()).map(t -> new Tuple2<>(partition, new Tuple1<>(((VariableAggregate) insideVariable).getFieldValue(field))));
            }
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
    private List<Variable> getSelectedVariable (String variableName, @Nullable Map<String, String> partition, @Nullable String timeWindow, WindowType windowType) throws VariableNotDefined {
        Long timeWindowL;
        if (timeWindow != null) {
            timeWindowL = Utils.solveTime(timeWindow);
        } else {
            timeWindowL = 0L;
        }
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

    private ArrayList<Variable> extractWindow(NavigableMap<Long, Variable> selectedVars, Long timeWindow, WindowType windowType){

        ArrayList<Variable> result = new ArrayList<>();
        if (timeWindow == null) timeWindow = 0L;
        if (windowType == null) windowType = WindowType.AGO;

        long timeSolved = this.currentTimestamp - timeWindow;


        try {
            if (windowType == WindowType.WITHIN || windowType == WindowType.EVERYWITHIN) {
                result.addAll(selectedVars.tailMap(selectedVars.floorKey(timeSolved), true).values());
            } else {
                if  (timeSolved < 0) return result;
                result.add(selectedVars.floorEntry(timeSolved).getValue());
            }
        } catch (NullPointerException e){
            //result will be empty
        }


        result.removeAll(Collections.singletonList(null));

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
        this.varablesNew.entrySet().stream().forEach(entry -> {
            NavigableMap<Long, Variable> versions = entry.getValue();
            long keepTimestamp = this.currentTimestamp - versions.get(versions.lastKey()).getPersistence();
            long lastToKeep = versions.floorKey(keepTimestamp);
            Iterator<Long> iterator = versions.keySet().iterator();

            while (iterator.hasNext()){
                long current = iterator.next();

                if (current < lastToKeep){
                    iterator.remove();
                } else {
                    break;
                }
            }
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
        String[] elements = variableName.split("\\.", 2);
        if (elements.length == 1) return new Tuple2<>(elements[0], null);
        else return new Tuple2<>(elements[0], elements[1]);
    }

    public void printVariable(String name) {
        //System.out.println(this.varablesNew.get(name).lastEntry().getValue().toString());
    }

    public void printAllVariables() {
        for (String v: this.varablesNew.keySet()) {
            printVariable(v);
        }
    }
}

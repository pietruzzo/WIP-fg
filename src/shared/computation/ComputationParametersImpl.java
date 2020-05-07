package shared.computation;

import org.apache.flink.api.java.tuple.Tuple2;
import org.jetbrains.annotations.Nullable;
import shared.VertexM;
import shared.selection.SelectionSolver;
import shared.variables.solver.VariableSolver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComputationParametersImpl implements Serializable, ComputationParameters {

    private VariableSolver variableSolver;
    private Map<String, String> partition;
    private HashMap<String, Parameter> parameters;
    private List<Tuple2<String, Long>> returnVarNames;

    public ComputationParametersImpl() {
        this.variableSolver = null;
        this.partition = null;
        this.returnVarNames = null;
        this.parameters = new HashMap<>();
    }

    /**
     *
     * @param paramName
     * @return null if parameter is not registered
     * obs: parameters of type "value" are always assumed single value in the pattern grammar
     */
    @Override
    public String getParameter (String paramName) {

        try {
            return this.getParameter(paramName, null)[0];
        } catch (NullPointerException e) {
            return null;
        }

    }

    public void setReturnVarNames(List<Tuple2<String, Long>> returnVarNames) {
        this.returnVarNames = returnVarNames;
    }

    @Override
    public List<String> returnVarNames (){
        return returnVarNames.stream().map(var -> var.f0).collect(Collectors.toList());
    }

    public List<Tuple2<String, Long>> getReturnVarsTemporalWindow (){
        return returnVarNames;
    }

    @Override
    public String[] getParameter (String paramName, Vertex vertex){

        VertexM vertexM = (VertexM) vertex;

        Parameter parameter = this.get(paramName);

        //Type Value
        if (parameter.isValue()) {
            return parameter.getContent();
        }

        //Type Label
        if (parameter.isLabel()) {
            return vertexM.getLabelVertex(parameter.parameter[0]);

        }

        //Variable node
        return variableSolver
                .getVertexVariable(parameter.getContent()[0], partition, vertexM.getNodeId(), parameter.getTimeAgo(), parameter.getwType())
                .stream().flatMap(array -> Arrays.stream(array))
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }

    /**
     *
     * @param paramName
     * @param vertex
     * @return First field is edgeId, second field are associated values
     */
    @Override
    public Map<String, String[]> getEdgeParameter (String paramName, Vertex vertex){

        VertexM vertexM = (VertexM) vertex;

        Parameter parameter = this.get(paramName);

        //Type Value
        if (parameter.isValue()) {
            return Arrays.stream(vertexM.getEdges())
                    .map(edge -> new Tuple2<>(edge, parameter.getContent()))
                    .collect(Collectors.toMap(tuple2 -> tuple2.f0, tuple2 -> tuple2.f1));
        }

        //Type Label
        if (parameter.isLabel()) {
            return Arrays.stream(vertexM.getEdges())
                    .map(edge -> new Tuple2<>(edge, vertexM.getLabelEdge(edge, parameter.getContent()[0])))
                    .collect(Collectors.toMap(tuple2 -> tuple2.f0, tuple2 -> tuple2.f1));

        }

        //Variable Edge
        return
                Arrays.stream(vertexM.getEdges())
                        .map(edge -> {
                            String[] edgeVariable = variableSolver
                                    .getEdgeVariable(parameter.getContent()[0], partition, vertexM.getNodeId(), edge, parameter.getTimeAgo(), parameter.getwType())
                                    .stream().flatMap(array -> Arrays.stream(array))
                                    .collect(Collectors.toList()).toArray(String[]::new);
                            return new Tuple2<>(edge, edgeVariable);
                        })
                        .collect(Collectors.toMap(tuple2 -> tuple2.f0, tuple2 -> tuple2.f1));

    }

    public VariableSolver getVariableSolver() {
        return variableSolver;
    }

    public void setVariableSolver(VariableSolver variableSolver) {
        this.variableSolver = variableSolver;
    }

    public Map<String, String> getPartition() {
        return partition;
    }

    public void setPartition(Map<String, String> partition) {
        this.partition = partition;
    }

    public Parameter get(Object key) {
        return this.parameters.get(key);
    }

    public Parameter put(String key, Parameter value) {
        return this.parameters.put(key, value);
    }

    public static class Parameter implements Serializable{
        private enum ParameterType  {VALUE, LABEL, VARIABLE}
        private ParameterType type;
        private String[] parameter;

        private final String timeAgo;
        private final SelectionSolver.Operation.WindowType wType;


        public boolean isValue() {
            return type.equals(ParameterType.VALUE);
        }

        public boolean isLabel() {
            return type.equals(ParameterType.LABEL);
        }

        public boolean isVariable() {
            return type.equals(ParameterType.VARIABLE);
        }

        public String[] getContent() {
            return parameter;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public SelectionSolver.Operation.WindowType getwType() {
            return wType;
        }



        /**
         * Value Parameter constructor
         * @param parameter
         */
        public Parameter(String parameter) {
            this.type = ParameterType.VALUE;
            this.parameter = new String[]{parameter};
            this.timeAgo = null;
            this.wType = null;
        }

        /**
         *
         * @param parameter
         * @param type 0:value, 1:label, 2:variable
         * @param timeAgo
         * @param wType
         */
        public Parameter(String parameter, int type, @Nullable String timeAgo, @Nullable SelectionSolver.Operation.WindowType wType) {
            switch (type){
                case 0:
                    this.type = ParameterType.VALUE;
                    break;
                case 1:
                    this.type = ParameterType.LABEL;
                    break;
                case 2:
                    this.type = ParameterType.VARIABLE;
                    break;
            }
            this.parameter = new String[]{parameter};
            this.timeAgo = timeAgo;
            this.wType = wType;
        }
    }

}

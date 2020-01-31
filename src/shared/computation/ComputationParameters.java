package shared.computation;

import org.apache.flink.api.java.tuple.Tuple2;
import org.jetbrains.annotations.Nullable;
import shared.VertexM;
import shared.selection.SelectionSolver;
import shared.variables.solver.VariableSolver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ComputationParameters implements Serializable {

    private VariableSolver variableSolver;
    private Map<String, String> partition;
    private HashMap<String, Parameter> parameters;

    public ComputationParameters() {
        this.variableSolver = null;
        this.partition = null;
        this.parameters = new HashMap<>();
    }

    /**
     *
     * @param paramName
     * @return null if parameter is not registered
     */
    public String getParameter (String paramName) {

        try {
            return this.parameters.get(paramName).getParameter()[0];
        } catch (NullPointerException e) {
            return null;
        }

    }

    public String[] getNodeValues (String variableName, Vertex vertexId){

        VertexM vertexM = (VertexM) vertexId;

        Parameter parameter = this.get(variableName);

        //Type Value
        if (parameter.isValue()) {
            return parameter.getParameter();
        }

        //Type Label
        if (parameter.isLabel()) {
            return vertexM.getLabelVertex(parameter.parameter[0]);

        }

        //Variable node
        return variableSolver
                .getVertexVariable(parameter.getParameter()[0], partition, vertexM.getNodeId(), parameter.getTimeAgo(), parameter.getwType())
                .stream().flatMap(array -> Arrays.stream(array))
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }

    /**
     *
     * @param variableName
     * @param vertexId
     * @return First field is edgeId, second field associated values
     */
    public Map<String, String[]> getEdgeValues (String variableName, Vertex vertexId){

        VertexM vertexM = (VertexM) vertexId;

        Parameter parameter = this.get(variableName);

        //Type Label
        if (parameter.isLabel()) {
            return Arrays.stream(vertexM.getEdges())
                    .map(edge -> new Tuple2<>(edge, vertexM.getLabelEdge(edge, parameter.getParameter()[0])))
                    .collect(Collectors.toMap(tuple2 -> tuple2.f0, tuple2 -> tuple2.f1));

        }

        //Variable Edge
        return
                Arrays.stream(vertexM.getEdges())
                        .map(edge -> {
                            String[] edgeVariable = variableSolver
                                    .getEdgeVariable(parameter.getParameter()[0], partition, vertexM.getNodeId(), edge, parameter.getTimeAgo(), parameter.getwType())
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
        private boolean isValue;
        private boolean isLabel;
        private String[] parameter;

        private final String timeAgo;
        private final SelectionSolver.Operation.WindowType wType;


        public boolean isValue() {
            return isValue;
        }

        public boolean isLabel() {
            return isLabel;
        }

        public String[] getParameter() {
            return parameter;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public SelectionSolver.Operation.WindowType getwType() {
            return wType;
        }

        public void setParameter(String[] parameter) {
            this.parameter = parameter;
        }

        public void setValue(boolean value) {
            isValue = value;
        }

        public void setLabel(boolean label) {
            isLabel = label;
        }


        /**
         * Value Parameter constructor
         * @param parameter
         */
        public Parameter(String parameter) {
            this.isValue = true;
            this.parameter = new String[]{parameter};
            this.timeAgo = null;
            this.wType = null;
        }

        public Parameter(String parameter, boolean isValue, boolean isLabel, @Nullable String timeAgo, @Nullable SelectionSolver.Operation.WindowType wType) {
            this.isValue = isValue;
            this.parameter = new String[]{parameter};
            this.timeAgo = timeAgo;
            this.wType = wType;
        }
    }

}

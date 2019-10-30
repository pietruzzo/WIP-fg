package shared.selection;


import shared.VertexNew;
import shared.exceptions.WrongTypeRuntimeException;
import shared.variables.solver.VariableSolver;
import shared.selection.SelectionSolver.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PartitioningSolver implements Selection, Cloneable{ //Inside partition

    public final boolean partitionOnEdge;
    private final List<Element> operationalElements;

    public PartitioningSolver(boolean partitionOnEdge) {
        this.partitionOnEdge = partitionOnEdge;
        this.operationalElements = new ArrayList<>();
    }

    public void addElement(Element element){
        this.operationalElements.add(element);
    }


    /**
     *
     * @param vertex
     * @param variableSolver
     * @return HashMap label-values
     */
    public HashMap<String, String[]> getPartitionsVertex(VertexNew vertex, VariableSolver variableSolver){
        HashMap<String, String[]> returnValues = new HashMap<>();
       //Get values
        for (Element e: operationalElements) {
            String[] values = e.solveVariablesAndLabels(variableSolver, vertex);
            returnValues.put(e.name, values);
        }
        return returnValues;
    }

    public HashMap<String, String[]> getPartitionsEdge(VertexNew vertex, String edge, VariableSolver variableSolver){
        HashMap<String, String[]> returnValues = new HashMap<>();
        //Get values
        for (Element e: operationalElements) {
            String[] values = e.solveVariablesAndLabels(variableSolver, vertex);
            returnValues.put(e.name, values);
        }
        return returnValues;
    }

    @Override
    public PartitioningSolver clone() {
        PartitioningSolver ps = new PartitioningSolver(this.partitionOnEdge);
        for (Element e: operationalElements) {
            ps.addElement(e.clone());
        }
        return ps;
    }

    public static class Element implements Serializable, Cloneable {
        private String[] values;
        private String name;
        private Operation.Type type;
        private Operation.WindowType wType;
        private String withinTimeUnits;

        public Element(String name, Operation.Type type, String withinTimeUnits, Operation.WindowType windowType) {
            this.values = null;
            this.name = name;
            this.type = type;
            this.withinTimeUnits = withinTimeUnits;
            this.wType = windowType;
        }

        public String[] solveVariablesAndLabels(VariableSolver variableSolver, VertexNew vertex){
            //Solve Variables
            if (type.equals(Operation.Type.VARIABLE)) {
                List<String[]> variableValues = variableSolver.getVertexVariable(name, null, vertex.getNodeId(), withinTimeUnits, wType);
                List<String> newValues = variableValues.stream().flatMap(list -> Arrays.stream(list)).collect(Collectors.toList());
                this.values = (String[])newValues.toArray();
                this.type = Operation.Type.VALUE;
            }
            //Solve Labels
            if (type.equals(Operation.Type.LABEL)) {
                values = vertex.getLabelVertex(name);
                type = Operation.Type.VALUE;
            }
            return values;
        }
        public String[] solveVariablesAndLabels(VariableSolver variableSolver, VertexNew vertex, String edgeName){
            //Solve Variables
            if (type.equals(Operation.Type.VARIABLE)) {
                List<String[]> variableValues = variableSolver.getEdgeVariable(name, null, vertex.getNodeId(), edgeName, withinTimeUnits, wType);
                List<String> newValues = variableValues.stream().flatMap(list -> Arrays.stream(list)).collect(Collectors.toList());
                this.values = (String[])newValues.toArray();
                this.type = Operation.Type.VALUE;
            }
            //Solve Labels
            if (type.equals(Operation.Type.LABEL)) {
                values = vertex.getLabelEdge(name, edgeName);
                type = Operation.Type.VALUE;
            }
            return values;
        }

        public void solveAggregates(VariableSolver variableSolver){
            if (type.equals(Operation.Type.VARIABLE)) {
                try {
                    List<String[]> variableValues = variableSolver.getAggregate(name, null, withinTimeUnits, wType);
                    List<String> newValues = variableValues.stream().flatMap(list -> Arrays.stream(list)).collect(Collectors.toList());
                    this.values = (String[])newValues.toArray();
                    this.type = Operation.Type.VALUE;
                } catch (WrongTypeRuntimeException e){}
            }
        }

        @Override
        public Element clone() {
            return new Element(this.name, this.type, this.withinTimeUnits, this.wType);
        }
    }
}




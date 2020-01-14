package shared.selection;


import shared.VertexM;
import shared.variables.solver.VariableSolver;
import shared.selection.SelectionSolver.Operation;

import java.io.Serializable;
import java.util.*;
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
    public HashMap<String, String[]> getPartitionsVertex(VertexM vertex, VariableSolver variableSolver){
        HashMap<String, String[]> returnValues = new HashMap<>();
       //Get values
        for (Element e: operationalElements) {
            String[] values = e.solveVariablesAndLabels(variableSolver, vertex);
            returnValues.put(e.name, values);
        }
        return returnValues;
    }

    public HashMap<String, String[]> getPartitionsEdge(VertexM vertex, String edge, VariableSolver variableSolver){
        HashMap<String, String[]> returnValues = new HashMap<>();
        //Get values
        for (Element e: operationalElements) {
            String[] values = e.solveVariablesAndLabels(variableSolver, vertex, edge);
            returnValues.put(e.name, values);
        }
        return returnValues;
    }

    public ArrayList<String> getNames(){
        ArrayList<String> result = new ArrayList<>();
        for (Element element: this.operationalElements) {
            result.add(element.name);
        }
        return result;
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
        //private String[] values;
        private String name;
        private Operation.Type type;
        private Operation.WindowType wType;
        private String withinTimeUnits;

        public Element(String name, Operation.Type type, String withinTimeUnits, Operation.WindowType windowType) {
            //this.values = null;
            this.name = name;
            this.type = type;
            this.withinTimeUnits = withinTimeUnits;
            this.wType = windowType;
        }

        public String[] solveVariablesAndLabels(VariableSolver variableSolver, VertexM vertex){
            String[] values;
            //Solve Variables
            if (type.equals(Operation.Type.VARIABLE)) {
                List<String[]> variableValues = variableSolver.getVertexVariable(name, null, vertex.getNodeId(), withinTimeUnits, wType);
                List<String> newValues = variableValues.stream().flatMap(list -> Arrays.stream(list)).collect(Collectors.toList());
                values = newValues.toArray(String[]::new);
            }
            //Solve Labels
            else if (type.equals(Operation.Type.LABEL)) {
                values = vertex.getLabelVertex(name);
            }
            else {
                throw new RuntimeException("Operation type must be VARIABLE or LABEL, not value");
            }
            return deduplicateValues(values);
        }

        public String[] solveVariablesAndLabels(VariableSolver variableSolver, VertexM vertex, String edgeName){
            String[] values;
            //Solve Variables
            if (type.equals(Operation.Type.VARIABLE)) {
                List<String[]> variableValues = variableSolver.getEdgeVariable(name, null, vertex.getNodeId(), edgeName, withinTimeUnits, wType);
                List<String> newValues = variableValues.stream().flatMap(list -> Arrays.stream(list)).collect(Collectors.toList());
                values = newValues.toArray(String[]::new);
            }
            //Solve Labels
            if (type.equals(Operation.Type.LABEL)) {
                values = vertex.getLabelEdge(name, edgeName);
            } else {
                throw new RuntimeException("Operation type must be VARIABLE or LABEL, not value");
            }
            return deduplicateValues(values);
        }

        public String[] solveAggregates(VariableSolver variableSolver){
            String[] values;
            if (type.equals(Operation.Type.VARIABLE)) {
                    List<String[]> variableValues = variableSolver.getAggregate(name, null, withinTimeUnits, wType);
                    List<String> newValues = variableValues.stream().flatMap(list -> Arrays.stream(list)).collect(Collectors.toList());
                    values = newValues.toArray(String[]::new);
            } else {
                throw new RuntimeException("Operation type must be VARIABLE");
            }
            return deduplicateValues(values);
        }


        /**
         *
         * @param values
         * @return HashSet with null
         */
        public String[] deduplicateValues (String[] values) {
            if (values == null || values.length == 0) return new String[]{null};
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(Arrays.asList(values));
            return hashSet.toArray(String[]::new);
        }

        @Override
        public Element clone() {
            return new Element(this.name, this.type, this.withinTimeUnits, this.wType);
        }
    }
}




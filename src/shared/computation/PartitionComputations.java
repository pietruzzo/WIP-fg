package shared.computation;

import akka.japi.Pair;
import org.apache.commons.math3.exception.DimensionMismatchException;

import java.util.*;

public abstract class PartitionComputations {

    abstract public List<String> getNames();
    abstract public void addComputationRuntime(Map<String, String> partition, ComputationRuntime computation);
    abstract public ComputationRuntime get(Map<String, String> partition);
    abstract public List<ComputationRuntime> getAll();

    public static class Node extends PartitionComputations {

        private final String varName;
        private HashMap<String, PartitionComputations> nodes;

        public Node (List<Pair<String, List<String>>> varValue){

            nodes = new HashMap<>();

            this.varName = varValue.get(0).first();
            List<Pair<String, List<String>>> subList = varValue.subList(1, varValue.size());
            if (subList.isEmpty()){
                for (String value: varValue.get(0).second()) {
                    nodes.put(value, new Leaf ());
                }
            } else {
                for (String value : varValue.get(0).second()) {
                    nodes.put(value, new Node(subList));
                }
            }
        }



        public List<String> getNames(){
            ArrayList<String> names = new ArrayList<>();
            names.add(this.varName);
            if (nodes.size()>0) {
                names.addAll(nodes.values().iterator().next().getNames());
            }
            return names;
        }

        public List<ComputationRuntime> getAll(){
            ArrayList<ComputationRuntime> result = new ArrayList<>();
            for (PartitionComputations partition: nodes.values()) {
                result.addAll(partition.getAll());
            }
            return result;
        }

        public ComputationRuntime get(Map<String, String> partition){
            PartitionComputations child = nodes.get(partition.get(this.varName));
            return child.get(partition);
        }

        /*
        I don't like this, too illegible
         */
        public void addComputationRuntime(Map<String, String> partition, ComputationRuntime computation) throws IllegalArgumentException{
            List<Pair<String, String>> orderedPartition = reorder(partition);
            String currName = orderedPartition.get(0).first();
            if (!currName.equals(this.varName)) throw new IllegalArgumentException("Attribute name structure is different");
            if (partition.size() == 0) throw new IllegalArgumentException("No attribute values given");
            String currValue = orderedPartition.get(0).second();
            if (nodes.get(currValue) == null) { //Branch doesn't exist, create new branch
                List<Pair<String, List<String>>> matchingContructor = new ArrayList<>();
                for (Pair p: orderedPartition.subList(1, orderedPartition.size())) {
                    List<String> val = new ArrayList<>();
                    val.add((String)p.second());
                    matchingContructor.add(new Pair<>((String)p.first(), val));
                }
                if (partition.size() > 1) {
                    nodes.put(currValue, new Node(matchingContructor));
                } else {
                    nodes.put(currValue, new Leaf());
                }
            }
            partition.remove(currName);
            nodes.get(currValue).addComputationRuntime(partition, computation);
        }

        /**
         * Reorder map elements in a list according to varName ordering
         * @param list
         * @return ordered name-value list
         * @throws DimensionMismatchException
         */
        private List<Pair<String, String>> reorder(Map<String, String> list) throws DimensionMismatchException {
            List<Pair<String, String>> result = new ArrayList<>();
            List<String> ordered = getNames();
            for (String element: ordered) {
                if (list.get(element) == null) throw new DimensionMismatchException(ordered.indexOf(element), list.size());
                result.add(new Pair(element, list.get(element)));
            }
            return result;
        }


    }

    public static class Leaf extends PartitionComputations {

        private ComputationRuntime computationRuntime;

        public Leaf() {
            this.computationRuntime = null;
        }

        public Leaf(ComputationRuntime computationRuntime) {
            this.computationRuntime = computationRuntime;
        }

        @Override
        public List<String> getNames() {
            return new ArrayList<>();
        }

        @Override
        public void addComputationRuntime(Map<String, String> partition, ComputationRuntime computation) {
            this.computationRuntime = computation;
        }

        @Override
        public ComputationRuntime get(Map<String, String> partition) {
            return this.computationRuntime;
        }

        @Override
        public List<ComputationRuntime> getAll() {
            ArrayList<ComputationRuntime> result = new ArrayList<>();
            result.add(this.computationRuntime); //Un check se null?
            return result;
        }
    }
}

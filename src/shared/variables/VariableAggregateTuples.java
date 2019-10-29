package shared.variables;

import akka.japi.Pair;
import org.apache.flink.api.java.tuple.Tuple;

import java.util.ArrayList;

public class VariableAggregateTuples extends Variable {


    private static final long serialVersionUID = 200047L;

        /**
         * Valore Aggregato, solitamente sul master
         */
        private final Tuple[] value;

        /**
         * Set associated nodeIds in case of free variables
         */
        private ArrayList<Pair<String, String>> associatedEdges;

        public VariableAggregateTuples(String name, long persistence, long timestamp, Tuple[] values) {
            super(name, persistence, timestamp);
            this.value = values;
        }

        public Tuple[] getValue() {
            return value;
        }


        public ArrayList<Pair<String, String>> getAssociatedEdges() {
            return associatedEdges;
        }

        public void setAssociatedEdges(ArrayList<Pair<String, String>> associatedEdges) {
            this.associatedEdges = associatedEdges;
        }

}

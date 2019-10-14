package shared.computation;

import java.util.HashMap;

public class ComputationParameters extends HashMap<String, ComputationParameters.Parameter> {

    public static class Parameter {
        private final boolean isValue;
        private final String parameter;
        private final Long timestamp;

        public Parameter(String parameter) {
            this.isValue = true;
            this.parameter = parameter;
            this.timestamp = null;
        }

        public Parameter(String parameter, boolean isValue, long timestamp) {
            this.isValue = isValue;
            this.parameter = parameter;
            this.timestamp = timestamp;
        }
    }

}

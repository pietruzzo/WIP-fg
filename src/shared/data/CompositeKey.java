package shared.data;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CompositeKey implements Serializable {

    private final HashMap<String, String> keysMapping;

    public CompositeKey(HashMap<String, String> keysMapping) {
        this.keysMapping = (HashMap)keysMapping.clone();
    }

    public HashMap<String, String> getKeysMapping() {
        return keysMapping;
    }

    @Override
    public int hashCode() {
        StringBuilder toBeHashed = new StringBuilder();
        //Order and Hash
        String[] keys = keysMapping.keySet().toArray(String[]::new);
        Arrays.sort(keys);

        for (String key: keys) {
            toBeHashed.append(key).append(keysMapping.get(key));
        }
        return toBeHashed.toString().hashCode();
    }

    @Override
    public boolean equals(Object compositeKey) {
        if ( !(compositeKey instanceof CompositeKey) ) return false;
        CompositeKey otherKey = (CompositeKey) compositeKey;
        if (this.keysMapping.size() != otherKey.keysMapping.size()) return false;
        for (Map.Entry<String, String> entry: otherKey.keysMapping.entrySet()) {
            if ( entry.getValue() == null && this.keysMapping.get(entry.getKey()) != null ) return false;
            else if ( entry.getValue() != null && this.keysMapping.get(entry.getKey()) == null ) return false;
            else if ( entry.getValue() == null && this.keysMapping.get(entry.getKey()) == null ) {}
            else if (! (entry.getValue().equals(this.keysMapping.get(entry.getKey()))) ) return false;
        }
        return true;
    }



}

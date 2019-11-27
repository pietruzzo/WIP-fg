package shared.data;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CompositeKey {

    private final HashMap<String, String> keysMapping;

    public CompositeKey(HashMap<String, String> keysMapping) {
        this.keysMapping = (HashMap)keysMapping.clone();
    }

    public HashMap<String, String> getKeysMapping() {
        return keysMapping;
    }

    @Override
    public int hashCode() {
        String toBeHashed = "";
        //Order and Hash
        String[] keys = keysMapping.keySet().toArray(String[]::new);
        Arrays.sort(keys);

        for (String key: keys) {
            toBeHashed = toBeHashed+key+keysMapping.get(key);
        }
        return toBeHashed.hashCode();
    }

    @Override
    public boolean equals(Object compositeKey) {
        if ( !(compositeKey instanceof CompositeKey) ) return false;
        CompositeKey otherKey = (CompositeKey) compositeKey;
        if (this.keysMapping.size() != otherKey.keysMapping.size()) return false;
        for (Map.Entry<String, String> entry: otherKey.keysMapping.entrySet()) {
            if (! (entry.getValue().equals(this.keysMapping.get(entry.getKey()))) ) return false;
        }
        return true;
    }



}

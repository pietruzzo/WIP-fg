package shared;

import shared.computation.ComputationRuntime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiKeyMap<T> implements Serializable {

    private final String[] keys;
    private final Map<String, T>  map;

    public MultiKeyMap(String[] keys) {
        this.keys = keys;
        this.map = new HashMap<>();
    }

    public String[] getKeys(){
        return keys;
    }

    /**
     *
     * @param compositeKey
     * @return null if value isn't contained for the specified composite key
     * @throws IllegalArgumentException if there isn't a composite key for the specified key mapping
     */
    public T getValue (Map<String, String> compositeKey) throws IllegalArgumentException{
        return map.get(composeKey(compositeKey));
    }

    public void putValue(Map<String, String> compositeKey, T value) {
        map.put(composeKey(compositeKey), value);
    }

    /**
     *
     * @param keyValues Map<key,value>
     * @return
     * @throws IllegalArgumentException if there isn't mapping for the specified composite key
     *
     */
    private String composeKey(Map<String, String> keyValues) throws IllegalArgumentException{
        String outputKey = "", next;
        for (String key: keys) {
            next = keyValues.get(key);
            if (next == null) throw new IllegalArgumentException("No mapping for " + key + " inside " + Arrays.toString(keyValues.keySet().toArray()));
            outputKey = outputKey + key + next;
        }
        return outputKey;
    }


}

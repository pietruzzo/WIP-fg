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
     *
     */
    private String composeKey(Map<String, String> keyValues) throws IllegalArgumentException{
        String outputKey = "", next;
        for (String key: keys) {
            next = keyValues.get(key);
            if (next == null) throw new IllegalArgumentException("No mapping for " + key + " inside " + Arrays.toString(keyValues.keySet().toArray()));
            outputKey = outputKey + key + next;
        }
        return  outputKey;
    }


}

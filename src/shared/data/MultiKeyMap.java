package shared.data;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultiKeyMap<T> implements Serializable {

    private final String[] keys;
    private final Map<CompositeKey, T>  map;

    public MultiKeyMap(String[] keys) {
        this.keys = keys;
        Arrays.sort(this.keys);
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
    public T getValue (HashMap<String, String> compositeKey) throws IllegalArgumentException{
        return map.get(new CompositeKey(compositeKey));

    }

    public T getValue (Map<String, String> compositeKey) throws IllegalArgumentException{
        HashMap<String, String> key = new HashMap<>(compositeKey);
        return map.get(new CompositeKey(key));

    }

    public T getValue (CompositeKey compositeKey) throws IllegalArgumentException{
        return map.get(compositeKey);

    }

    public Map<CompositeKey, T> getAllElements(){
        return this.map;
    }

    public synchronized void putValue(HashMap<String, String> compositeKey, T value) throws IllegalArgumentException{
        if( !validateKey(compositeKey) ) throw new IllegalArgumentException("Composite key have different simple keys set: " + compositeKey + ", " + Arrays.toString(this.keys));
        map.put(new CompositeKey(compositeKey), value);
    }

    public synchronized void putIfAbsent(CompositeKey compositeKey, T value) {
        this.map.putIfAbsent(compositeKey, value);
    }

    public synchronized void putValue(CompositeKey compositeKey, T value) throws IllegalArgumentException{
        if( !validateKey(compositeKey.getKeysMapping()) ) throw new IllegalArgumentException("Composite key have different simple keys set: " + compositeKey + ", " + Arrays.toString(this.keys));
        map.put(compositeKey, value);
    }

    private boolean validateKey(HashMap<String, String> compositeKey){
        if (this.keys.length != compositeKey.size()) return false;
        String[] givenKeys = compositeKey.keySet().toArray(String[]::new);
        Arrays.sort(givenKeys);
        for (int i = 0; i < givenKeys.length; i++) {
            if( !(givenKeys[i].equals(this.keys[i])) ) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MultiKeyMap{" +
                "keys=" + Arrays.toString(keys) +
                ", map=" + map +
                '}';
    }
}


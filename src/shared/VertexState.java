package shared;

import akka.event.Logging;
import akka.japi.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VertexState implements Serializable {

    /*
        Assumption: State versions are in timestamp order
     */
    static public final String DELETE_VALUE = "DELETE";

    private static final long serialVersionUID = 200002L;

    final private Map<String, Versions> versionedAttributes;  //AttributeName - List of Pairs < Timestamp, Value >

    public VertexState(){
        versionedAttributes = new HashMap<>();
    }

    public void addToState(String name, String value, Long timestamp) {
        Versions versions;
        if (versionedAttributes.containsKey(name)) {
            versions = versionedAttributes.get(name);
        } else {
            versions = new Versions();
            versionedAttributes.put(name, versions);
        }
        versions.add(new Pair(timestamp, value));
    }

    public void addToState (VertexState otherState){

    }

    public void removeOldStates(String name, Long timestamp) { //But keeping latest one

        Versions values = versionedAttributes.get(name);

        if (values == null) { //No entry for name
            return;
        }

        Versions toBeRemoved = new Versions();

        for (Pair p: values) {
            if ((Long)p.second()< timestamp && p != values.get(values.size()-1)) toBeRemoved.add(p);
        }
        for (Pair rv: toBeRemoved) {
            values.remove(rv);
        }
        toBeRemoved.clear();

        //Remove entry if deleted
        if (values.isEmpty() || values.size() == 1 && values.get(0).second().equals(DELETE_VALUE)){
            versionedAttributes.remove(name);
        }
    }

    public void removeOldStates(Long timestamp){
        for (String name: versionedAttributes.keySet()) {
            removeOldStates(name, timestamp);
        }
    }

    /**
     * Get latest version
     */
    public String getValue (String key){
        Versions values = versionedAttributes.get(key);
        return values.get(values.size()-1).second();
    }

    public Versions getValues (String key, Long t1, Long t2){
        Versions values = versionedAttributes.get(key);
        if (values == null || values.isEmpty()) return new Versions();
        return values.subVersions(t1, t2);
    }


    @Override
    public VertexState clone(){
        VertexState newState = new VertexState();
        Iterator<Map.Entry<String, Versions>> it = versionedAttributes.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Versions> element = it.next();
            Iterator<Pair<Long, String>> version = element.getValue().iterator();
            while (version.hasNext()) {
                Pair<Long, String> entry = version.next();
                newState.addToState(element.getKey(), entry.second(), entry.first().longValue());
            }
        }
        return newState;
    }


    public static class Versions extends ArrayList<Pair<Long, String>> implements Serializable {
        private static final long serialVersionUID = 200003L;

        public Versions subVersions (long t1, long t2){
            Versions result = new Versions();
            for (Pair<Long, String> entry: this) {
                if (entry.first() > t2) return result;
                else if (entry.first() >= t1) result.add(new Pair<>(entry.first().longValue(), entry.second()));
            }
            return result;
        }
    }

}


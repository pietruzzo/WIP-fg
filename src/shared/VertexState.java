package shared;

import akka.japi.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VertexState implements Serializable {

    private static final long serialVersionUID = 200002L;

    private Map<String, Versions> versionedAttributes;  //AttributeName - List of Pairs < Timestamp, Value >

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
        for (:
             ) {

        }
    }

    public void removeOldStates(Long timestamp) { //But keeping latest one
        Versions toBeRemoved = new Versions();
        for (Versions v: versionedAttributes.values()) {
            for (Pair p: v) {
                if ((Long)p.second()< timestamp && p != v.get(v.size()-1)) toBeRemoved.add(p);
            }
            for (Pair rv: toBeRemoved) {
                v.remove(rv);
            }
            toBeRemoved.clear();
        }
        Object[] toRemove= versionedAttributes.entrySet().parallelStream().filter(stringVersionsEntry -> stringVersionsEntry.getValue().isEmpty()).toArray();
        for (Object o: toRemove) {
            versionedAttributes.remove(((Map.Entry<String, Versions>) o).getKey());
        }

    }

    /**
     * Get latest version
     * @param key
     * @return
     */
    public String getValue (String key){
        Versions values = versionedAttributes.get(key);
        return values.get(values.size()-1).second();
    }

    public ArrayList<Pair<Long, String>>

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

}

class Versions extends ArrayList<Pair<Long, String>> {
    private static final long serialVersionUID = 200003L;
}
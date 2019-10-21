package shared.variables.solver;

import shared.variables.Variable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public abstract class VariableSolver {

    /**
     * Expressed in ms, it need to be set manually in every new timestamp
     */
    protected long currentTimestamp;

    public void setCurrentTimestamp (long timestamp) {
        this.currentTimestamp = timestamp;
    }

    /**
     * Keep only 1 version older than currentTimestamp
     * @param listMapping
     * @param <V>
     */
    protected <V extends Variable> void removeOldVariables(Map<String, LinkedList<V>> listMapping){
        long keepTimestamp;
        int versionsToDelete;
        for (LinkedList<V> list: listMapping.values()) {
            versionsToDelete = 0;
            keepTimestamp = this.currentTimestamp - list.peekLast().getPersistence();
            Variable current = null, next = null;
            Iterator<V> iterator = list.iterator();
            while (iterator.hasNext()){
                current = next;
                next = iterator.next();
                if (current!=null && next.getTimestamp() < keepTimestamp) {
                    versionsToDelete = versionsToDelete + 1;
                }
                if (next.getTimestamp() > keepTimestamp) break;
            }
            for (int i = 0; i < versionsToDelete; i++) {
                list.removeFirst();
            }
        }
    }
    protected <Var extends Variable> void addToMap(Map<String, LinkedList<Var>> vertexMap, Var variable){
        vertexMap.computeIfAbsent(variable.getName(), k -> new LinkedList<>());
        vertexMap.get(variable.getName()).add(variable);
    }

    public abstract void addVariable(Variable variable);

    public abstract void removeOldVersions();
}

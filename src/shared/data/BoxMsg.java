package shared.data;

import java.io.Serializable;
import java.util.*;


public class BoxMsg<TMsg> implements Serializable {
    private static final long serialVersionUID = 200015L;

    /**
     * valid for step number (es: if built from step 0, this will be 1)
     */
    private final long stepNumber;

    /**
     * Partition
     */
    private Map<String, String> partition;

    /**
     * Destination, ListOfMessages
     */
    private final HashMap<String, ArrayList<TMsg>> data;

    public BoxMsg(long stepNumber) {
        this.stepNumber = stepNumber;
        data = new HashMap<>();
    }

    public long getStepNumber(){
        return stepNumber;
    }

    public void setPartition(Map<String, String> partition){
        this.partition = partition;
    }

    public Map<String, String> getPartition(){
        return this.partition;
    }

    public synchronized void put(String destination, TMsg message){
        ArrayList<TMsg> messages = data.computeIfAbsent(destination, k -> new ArrayList<>());

        messages.add(message);
    }

    /**
     * @return null if element is not present
     */
    public synchronized ArrayList<TMsg> get (String destination){
        return data.get(destination);
    }

    public synchronized boolean isEmpty(){
        return this.data.isEmpty();
    }

    public synchronized Set<String> keySet (){ return data.keySet();}

    public SynchronizedIterator<Map.Entry<String, ArrayList<TMsg>>> getSyncIterator(){
        return new SynchronizedIterator<>(this.data.entrySet().iterator());
    }

    @Override
    public String toString() {
        return "BoxMsg{" +
                "stepNumber=" + stepNumber +
                ", partition=" + partition +
                ", data=" + data +
                '}';
    }
}

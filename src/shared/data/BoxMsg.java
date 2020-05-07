package shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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
     * True: no more messages to transmit from Actors A to B
     */
    private boolean lastBox;

    /**
     * number of inserted msgs
     */
    private int insertedMsgs;

    /**
     * Destination, ListOfMessages
     */
    private final HashMap<String, ArrayList<TMsg>> data;

    public BoxMsg(long stepNumber) {
        this.stepNumber = stepNumber;
        this.data = new HashMap<>();
        this.lastBox = false;
        this.insertedMsgs = 0;
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

    public void addToValue(String destination, TMsg message){
        data.computeIfAbsent(destination, k-> new ArrayList<>());
        data.get(destination).add(message);
        insertedMsgs++;
    }

    public ArrayList<TMsg> put(String destination, ArrayList<TMsg> messageList){
        insertedMsgs = insertedMsgs + messageList.size();
        ArrayList<TMsg> oldValue = data.put(destination, messageList);
        if (oldValue != null) {
            insertedMsgs = insertedMsgs - oldValue.size();
        }
        return oldValue;
    }

    /**
     * @return null if element is not present
     */
    public ArrayList<TMsg> get (String destination){
        return data.get(destination);
    }

    public boolean isEmpty(){
        return this.data.isEmpty();
    }

    public Set<Map.Entry<String, ArrayList<TMsg>>> entrySet (){
        return data.entrySet();
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public int numMessages () {
        return this.insertedMsgs;
    }

    public HashMap<String, ArrayList<TMsg>> getData(){
        return this.data;
    }

    public void setLastFlag(boolean lastBox) {
        this.lastBox = lastBox;
    }

    public boolean isLast() {
        return this.lastBox;
    }

    @Override
    public String toString() {
        return "BoxMsg{" +
                "stepNumber=" + stepNumber +
                ", partition=" + partition +
                ", lastBox=" + lastBox +
                ", data=" + data +
                '}';
    }


}

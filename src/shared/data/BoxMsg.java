package shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class BoxMsg<TMsg> implements Serializable {
    private static final long serialVersionUID = 200015L;

    private final int stepNumber;
    private final HashMap<String, ArrayList<TMsg>> data; //Destination - List of Messages

    public BoxMsg(int stepNumber) {
        this.stepNumber = stepNumber;
        data = new HashMap<>();
    }

    public int getStepNumber(){
        return stepNumber;
    }

    public synchronized void put(String destination, TMsg message){
        ArrayList<TMsg> messages = data.get(destination);

        if (messages == null) {
            messages = new ArrayList<>();
            data.put(destination, messages);
        }
        messages.add(message);
    }

    public synchronized ArrayList<TMsg> get (String destination){
        return (ArrayList<TMsg>) data.get(destination);
    }

    public SynchronizedIterator<Map.Entry<String, ArrayList<TMsg>>> getSyncIterator(){
        return new SynchronizedIterator<>(this.data.entrySet().iterator());
    }


}

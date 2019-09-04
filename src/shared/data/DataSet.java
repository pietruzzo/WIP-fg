package shared.data;

import shared.Vertex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class DataSet extends HashMap<String, Vertex> {

    public SynchronizedIterator<Vertex> getSyncronizedIterator(){
        return new SynchronizedIterator<Vertex>(super.values().iterator());
    }


}

package shared.data;

import shared.Vertex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class DataSet extends HashMap<String, Vertex> {

    public SynchronizedIterator<Vertex> getSyncronizedIterator(){
        return new SynchronizedIterator<Vertex>(super.values().iterator());
    }

    class SynchronizedIterator<Vertex> implements Iterator{

        private Iterator<Vertex> it;

        public SynchronizedIterator(Iterator<Vertex> iterator) {
            this.it = iterator;
        }

        @Override
        public synchronized boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public synchronized Object next() {
            return it.next();
        }

        @Override
        public synchronized void remove() {
            it.remove();
        }

    }
}

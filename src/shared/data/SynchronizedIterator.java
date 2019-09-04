package shared.data;

import java.util.Iterator;


public class SynchronizedIterator<T> implements Iterator<T> {

    private Iterator<T> it;

    public SynchronizedIterator(Iterator<T> iterator) {
        this.it = iterator;
    }

    @Override
    public synchronized boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public synchronized T next() {
        return it.next();
    }

    @Override
    public synchronized void remove() {
        it.remove();
    }

}


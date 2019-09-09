package shared.data;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;



public class SynchronizedIterator<T> implements Iterator<T> {

    private Iterator<T> it;

    public SynchronizedIterator(Iterator<T> iterator) {
        this.it = iterator;
    }

    @Override
    public synchronized boolean hasNext() {
        return it.hasNext();
    }

    /**
     * @return next element
     * @exception java.util.NoSuchElementException if End of List has been reached
     */
    @Override
    public synchronized T next() throws NoSuchElementException {
        return it.next();
    }

    @Override
    public synchronized void remove() {
        it.remove();
    }

}


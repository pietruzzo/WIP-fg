package shared.data;

import java.util.Iterator;
import java.util.NoSuchElementException;



public class SynchronizedIterator<T>  {

    private Iterator<T> it;

    public SynchronizedIterator(Iterator<T> iterator) {
        this.it = iterator;
    }


    /**
     * @return next element
     * @exception java.util.NoSuchElementException if End of List has been reached
     */
    public synchronized T next() throws NoSuchElementException {
        return it.next();
    }

    public synchronized void remove() {
        it.remove();
    }

}


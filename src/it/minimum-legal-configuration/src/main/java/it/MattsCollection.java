package it;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MattsCollection<E> extends AbstractCollection<E> {
    private final Object[] items;
    private int index;

    public MattsCollection(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("negative capacity: " + capacity);

        this.items = new Object[capacity];
        this.index = 0;
    }

    @Override
    public Iterator<E> iterator() {
        final int initialSize = size();

        return new Iterator<E>() {
            private int currentIndex = 0;

            public boolean hasNext() {
                return currentIndex < initialSize;
            }

            @SuppressWarnings("unchecked")
            public E next() {
                if (size() != initialSize)
                    throw new ConcurrentModificationException();
                if (!hasNext())
                    throw new NoSuchElementException();
                return (E) items[currentIndex++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size() {
        return index;
    }

    @Override
    public boolean add(E newItem) {
        if (index == items.length)
            throw new IllegalArgumentException("capacity reached");

        for (int i = 0; i < size(); ++i) {
            if (items[i] == newItem)
                return false;
        }

        items[index++] = newItem;
        return true;
    }

    @Override
    public boolean remove(Object oldItem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> oldItems) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> keepers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "egg";
    }
}

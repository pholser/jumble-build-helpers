package it;

import static java.util.Collections.*;
import org.junit.Test;

public class MattsCollectionExceptionsTest {
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeCapacity() {
        new MattsCollection<Object>(-1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSupportRemove() {
        new MattsCollection<Object>(1).remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSupportRemoveAll() {
        new MattsCollection<Object>(2).removeAll(singleton("baz"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSupportRetainAll() {
        new MattsCollection<Object>(3).retainAll(singleton("quxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectAddWhenCapacityReached() {
        new MattsCollection<Object>(0).add(new Object());
    }
}

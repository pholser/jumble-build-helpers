package it;

import java.util.Collection;
import java.util.NoSuchElementException;

import static java.util.Collections.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MattsEmptyCollectionTest {
    private Collection<String> empty;

    @Before
    public void setUp() {
        empty = new MattsCollection<String>(5);
    }

    @Test
    public void shouldReportEmpty() {
        assertTrue(empty.isEmpty());
    }

    @Test
    public void shouldReportCorrectSize() {
        assertEquals(0, empty.size());
    }

    @Test
    public void shouldNotContainAnything() {
        assertFalse(empty.contains("boo"));
        assertFalse(empty.contains(null));
    }

    @Test
    public void shouldContainAllOfTheEmptyCollection() {
        assertTrue(empty.containsAll(emptySet()));
    }

    @Test
    public void shouldNotContainAllOfANonEmptyCollection() {
        assertFalse(empty.containsAll(singleton("bar")));
    }

    @Test
    public void shouldGiveIteratorWhichDoesNotHaveNext() {
        assertFalse(empty.iterator().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGiveIteratorWhichRaisesExceptionOnNext() {
        empty.iterator().next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldGiveIteratorWhichForbidsRemoval() {
        empty.iterator().remove();
    }

    @Test
    public void shouldGiveAZeroLengthArray() {
        Object[] asArray = empty.toArray();
        assertEquals(0, asArray.length);
    }

    @Test
    public void shouldGiveAZeroLengthTypedArray() {
        String[] asArray = empty.toArray(new String[0]);
        assertEquals(0, asArray.length);
    }
}

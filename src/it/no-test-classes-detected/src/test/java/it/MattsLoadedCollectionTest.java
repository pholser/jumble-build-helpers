package it;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MattsLoadedCollectionTest {
    private Collection<String> loaded;

    @Before
    public void setUp() {
        loaded = new MattsCollection<String>(10);

        assertTrue(loaded.add("a"));
        assertTrue(loaded.addAll(asList("b", "c", "d")));
        assertTrue(loaded.add("e"));
    }

    @Test
    public void shouldNotReportEmpty() {
        assertFalse(loaded.isEmpty());
    }

    @Test
    public void shouldReportCorrectSize() {
        assertEquals(5, loaded.size());
    }

    @Test
    public void shouldIterateInInsertionOrder() {
        List<String> items = new ArrayList<String>();
        Iterator<String> iter = loaded.iterator();
        while (iter.hasNext())
            items.add(iter.next());

        assertEquals(asList("a", "b", "c", "d", "e"), items);

        assertFalse(iter.hasNext());
        try {
            iter.next();
            fail();
        }
        catch (NoSuchElementException expected) {
            assertTrue(expected.getMessage(), true);
        }
    }

    @Test(expected = ConcurrentModificationException.class)
    public void shouldPreventConcurrentModification() {
        Iterator<String> iter = loaded.iterator();
        loaded.add("f");
        iter.next();
    }

    @Test
    public void shouldGiveArray() {
        Object[] asArray = loaded.toArray();

        assertEquals(asList("a", "b", "c", "d", "e"), asList(asArray));
    }

    @Test
    public void shouldGiveTypedArray() {
        String[] asArray = loaded.toArray(new String[loaded.size()]);

        assertEquals(asList("a", "b", "c", "d", "e"), asList(asArray));
    }
}

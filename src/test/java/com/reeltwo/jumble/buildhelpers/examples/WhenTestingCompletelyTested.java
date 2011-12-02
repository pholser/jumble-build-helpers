package com.reeltwo.jumble.buildhelpers.examples;

import org.junit.Test;

import static org.junit.Assert.*;

public class WhenTestingCompletelyTested {
    @Test
    public void shouldAddTwoIntegers() {
        assertEquals(7, new CompletelyTested().add(3, 4));
    }
}

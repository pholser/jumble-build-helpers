package com.reeltwo.jumble.buildhelpers.util;

import org.infinitest.toolkit.EqualsHashCodeTestSupport;
import org.junit.Test;

import static org.junit.Assert.*;

public class PercentageTest extends EqualsHashCodeTestSupport {
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegative() {
        new Percentage("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectGreaterThan100() {
        new Percentage("101");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectDecimal() {
        new Percentage("99.44");
    }

    @Test
    public void shouldCompare() {
        Percentage lessThan = new Percentage(50);
        Percentage greaterThan = new Percentage(51);

        assertEquals(0, lessThan.compareTo(lessThan));
        assertTrue(lessThan.compareTo(greaterThan) < 0);

        assertEquals(0, greaterThan.compareTo(greaterThan));
        assertTrue(greaterThan.compareTo(lessThan) > 0);
    }

    @Override
    protected Percentage equal() {
        return new Percentage("40");
    }

    @Override
    protected Percentage notEqual() {
        return new Percentage(39);
    }
}

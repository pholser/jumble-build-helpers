package com.reeltwo.jumble.buildhelpers.examples;

import org.junit.runner.Description;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;

import static org.junit.Assert.*;

public class WhenTestingWellTested {
    @Rule
    public final TestRule rule = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            super.starting(description);
            System.out.println(description.getMethodName());
        }
    };

    private final WellTested myWellTested = new WellTested();

    @Test
    public void aBoolean() {
        assertTrue(myWellTested.mustReturnTrue());
    }

    @Test
    public void conditionFalseBranch() {
        assertEquals(0, myWellTested.mustTakeCorrectBranchFor(false));
    }

    @Test
    public void conditionTrueBranch() {
        assertEquals(1, myWellTested.mustTakeCorrectBranchFor(true));
    }

    @Test
    public void decrement() {
        assertEquals(0, myWellTested.mustDecrement(1));
    }

    @Test
    public void equal() {
        assertTrue(myWellTested.mustBeEqual());
    }

    @Test
    public void increment() {
        assertEquals(1, myWellTested.mustIncrement(0));
    }

    @Test
    public void infiniteLoop() {
        myWellTested.turnIntoInfiniteLoop();
    }

    @Test
    public void integer() {
        assertEquals(1, myWellTested.mustReturnOne());
    }

    @Test
    public void notEqual() {
        assertTrue(myWellTested.mustNotBeEqual());
    }
}

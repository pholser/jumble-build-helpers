package com.reeltwo.jumble.buildhelpers.examples;

public class WellTested {
    public boolean mustBeEqual() {
        String a = "hi";
        return a == a;
    }

    public int mustDecrement(int aNum) {
        return --aNum;
    }

    public int mustIncrement(int aNum) {
        return ++aNum;
    }

    public boolean mustNotBeEqual() {
        return new Object() != new Object();
    }

    public int mustReturnOne() {
        return 1;
    }

    public boolean mustReturnTrue() {
        return true;
    }

    public int mustTakeCorrectBranchFor(boolean condition) {
        if (condition)
            return 1;

        return 0;
    }

    public void turnIntoInfiniteLoop() {
        for (int i = 1; i < 120; i++)
            System.out.println("" + i);
    }
}

package com.reeltwo.jumble.buildhelpers.util;

import java.io.Serializable;

public class Percentage implements Comparable<Percentage>, Serializable {
    private static final long serialVersionUID = 1L;

    private final int percentage;

    public Percentage(String raw) {
        this(Integer.parseInt(raw));
    }

    public Percentage(int raw) {
        if (raw < 0 || raw > 100)
            throw new IllegalArgumentException("need between 0 and 100, was " + raw);

        this.percentage = raw;
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Percentage && percentage == ((Percentage) that).percentage;
    }

    @Override
    public int hashCode() {
        return percentage;
    }

    @Override
    public String toString() {
        return percentage + "%";
    }

    public int compareTo(Percentage other) {
        if (percentage == other.percentage)
            return 0;

        return percentage < other.percentage ? -1 : 1;
    }

    public boolean lessThan(Percentage other) {
        return compareTo(other) < 0;
    }
}

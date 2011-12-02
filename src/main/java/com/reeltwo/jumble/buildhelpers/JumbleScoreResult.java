package com.reeltwo.jumble.buildhelpers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.reeltwo.jumble.buildhelpers.util.Percentage;

public class JumbleScoreResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String targetClass;
    private final Percentage threshold;
    private final Percentage score;

    public JumbleScoreResult(String targetClass, Percentage threshold, Percentage score) {
        this.targetClass = targetClass;
        this.threshold = threshold;
        this.score = score;
    }

    public boolean succeeded() {
        return !score.lessThan(threshold);
    }

    @Override
    public String toString() {
        return String.format("score for %1$s: %2$s, threshold: %3$s", targetClass, score, threshold);
    }

    public static String toMessage(List<JumbleScoreResult> failures) {
        StringBuilder message = new StringBuilder();

        for (Iterator<JumbleScoreResult> iter = failures.iterator(); iter.hasNext();) {
            JumbleScoreResult each = iter.next();

            message.append(each.toString());
            if (iter.hasNext())
                message.append(System.getProperty("line.separator"));
        }

        return message.toString();
    }
}

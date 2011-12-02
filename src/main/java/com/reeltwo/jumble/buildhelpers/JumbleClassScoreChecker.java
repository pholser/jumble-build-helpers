package com.reeltwo.jumble.buildhelpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.reeltwo.jumble.buildhelpers.util.Percentage;

public class JumbleClassScoreChecker {
    private final JumbleClassScoreScraper scraper;
    private final Percentage threshold;
    private final ByteArrayOutputStream jumbleOutput;

    public JumbleClassScoreChecker(Percentage threshold, ByteArrayOutputStream jumbleOutput) {
        this.scraper = new JumbleClassScoreScraper();
        this.threshold = threshold;
        this.jumbleOutput = jumbleOutput;
    }

    public JumbleScoreResult checkScore(String targetClass) throws IOException {
        return new JumbleScoreResult(targetClass, threshold, scraper.jumbleScoreFrom(jumbleOutput));
    }
}

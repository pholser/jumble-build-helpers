package com.reeltwo.jumble.buildhelpers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.reeltwo.jumble.buildhelpers.util.Percentage;

class JumbleClassScoreScraper {
    private static final Pattern SCORE_PATTERN = Pattern.compile("^Score: (\\d+)%");

    Percentage jumbleScoreFrom(ByteArrayOutputStream jumbleOutput) throws IOException {
        BufferedReader scoreReader =
            new BufferedReader(new InputStreamReader(new ByteArrayInputStream(jumbleOutput.toByteArray())));
        for (String line = scoreReader.readLine(); line != null; line = scoreReader.readLine()) {
            Matcher matcher = SCORE_PATTERN.matcher(line);
            if (matcher.find())
                return new Percentage(matcher.group(1));
        }

        return new Percentage("100");
    }
}

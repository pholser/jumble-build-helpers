package com.reeltwo.jumble.buildhelpers.maven;

import java.util.List;

import com.reeltwo.jumble.buildhelpers.JumbleScoreResult;
import org.apache.maven.plugin.MojoFailureException;

import static com.reeltwo.jumble.buildhelpers.JumbleScoreResult.*;

class JumbleMojoFailure extends MojoFailureException {
    private static final long serialVersionUID = 1L;

    JumbleMojoFailure(List<JumbleScoreResult> failures) {
        super(toMessage(failures));
    }
}

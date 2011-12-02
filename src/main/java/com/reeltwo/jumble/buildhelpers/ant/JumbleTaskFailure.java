package com.reeltwo.jumble.buildhelpers.ant;

import java.util.List;

import com.reeltwo.jumble.buildhelpers.JumbleScoreResult;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;

import static com.reeltwo.jumble.buildhelpers.JumbleScoreResult.*;

class JumbleTaskFailure extends BuildException {
    private static final long serialVersionUID = 1L;

    JumbleTaskFailure(List<JumbleScoreResult> failures, Location location) {
        super(toMessage(failures), location);
    }
}

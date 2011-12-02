package com.reeltwo.jumble.buildhelpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.reeltwo.jumble.buildhelpers.util.Percentage;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.util.TeeOutputStream;

import static org.apache.tools.ant.Project.*;

public class JumbleExecutor {
    private final ProjectComponent component;
    private final List<JumbleCommandLineRunner> runners;
    private final Percentage threshold;
    private final File outputDir;
    private ByteArrayOutputStream outputCapture = new ByteArrayOutputStream(8192);

    public JumbleExecutor(ProjectComponent component, List<JumbleCommandLineRunner> runners, Percentage threshold,
        File outputDir) {

        this.component = component;
        this.runners = runners;
        this.threshold = threshold;
        this.outputDir = outputDir;
    }

    public List<JumbleScoreResult> execute() throws IOException {
        List<JumbleScoreResult> failures = new LinkedList<JumbleScoreResult>();

        for (JumbleCommandLineRunner each : runners) {
            Execute executor = new Execute(newStreamHandler(each.getTargetClass()));
            executor.setCommandline(each.getCommandLine());

            executor.execute();

            JumbleClassScoreChecker scoreChecker = new JumbleClassScoreChecker(threshold, outputCapture);
            JumbleScoreResult result = scoreChecker.checkScore(each.getTargetClass());
            if (!result.succeeded())
                failures.add(result);
        }

        return failures;
    }

    private ExecuteStreamHandler newStreamHandler(String targetClassName) throws FileNotFoundException {
        OutputStream out = new LogOutputStream(component, MSG_INFO);
        OutputStream err = new LogOutputStream(component, MSG_ERR);

        if (outputDir != null) {
            outputDir.mkdirs();
            out = new TeeOutputStream(out,
                new FileOutputStream(new File(outputDir, "JUMBLE-" + targetClassName + ".txt")));
        }

        outputCapture = new ByteArrayOutputStream(8192);
        return new PumpStreamHandler(new TeeOutputStream(out, outputCapture), err);
    }
}

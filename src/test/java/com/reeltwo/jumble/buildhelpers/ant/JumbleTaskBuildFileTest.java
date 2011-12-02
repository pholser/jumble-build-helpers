package com.reeltwo.jumble.buildhelpers.ant;

import java.io.File;

import org.apache.tools.ant.BuildFileTest;

import static com.reeltwo.jumble.buildhelpers.ant.JumbleTask.*;
import static com.reeltwo.jumble.buildhelpers.ant.JumbleTaskTest.*;

public class JumbleTaskBuildFileTest extends BuildFileTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        configureProject(getClass().getResource("/ant/jumble-task-test-build.xml").getFile());
    }

    public void testShouldFailIfJumbleJarLocationNotProvided() {
        expectBuildException("no-jumble-jar-set", MISSING_JUMBLE_JAR);
    }

    public void testShouldFailIfNoTargetClassesSpecified() {
        expectBuildException("no-target-classes-specified", MISSING_TARGET_CLASSES);
    }

    public void testShouldFailIfNoTargetClassesDetected() {
        expectBuildException("no-target-classes-detected", MISSING_TARGET_CLASSES);
    }

    public void testShouldSucceedWithMinimumLegalConfiguration() {
        executeTarget("minimum-legal-configuration");

        assertLogContaining("Mutating com.reeltwo.jumble.buildhelpers.examples.WellTested");
        assertLogContaining("Score: 0%");
    }

    public void testShouldSucceedWithMinimumLegalConfigurationWithTests() {
        executeTarget("minimum-legal-configuration-with-tests");

        assertLogContaining("Mutating com.reeltwo.jumble.buildhelpers.examples.WellTested");
        assertLogContaining("Score: 75%");
    }

    public void testShouldFailWhenGivenANonExistentOutputDirectory() {
        expectBuildException("non-existent-output-directory", OUTPUTDIR_DOES_NOT_EXIST);
    }

    public void testShouldFailWhenGivenANonWritableOutputDirectory() {
        expectBuildException("non-writable-output-directory", OUTPUTDIR_IS_NOT_WRITABLE);
    }

    public void testShouldFailWhenGivenANonDirectoryAsOutputDirectory() {
        expectBuildException("output-directory-not-a-directory", OUTPUTDIR_IS_NOT_A_DIRECTORY);
    }

    public void testShouldWriteOutputFilesWhenOutputDirectoryGiven() {
        executeTarget("output-directory-specified");

        File jumbleReport = new File(TEMP_PATH, "JUMBLE-com.reeltwo.jumble.buildhelpers.examples.WellTested.txt");
        assertTrue(jumbleReport.exists());
    }

    public void testShouldSucceedWithMethodExclusion() {
        executeTarget("exclude-methods-attribute");

        assertLogContaining("Mutating com.reeltwo.jumble.buildhelpers.examples.WellTested");
        assertLogContaining("Score: 75%");
    }

    public void testShouldSucceedWithScoreGreaterThanThreshold() {
        executeTarget("class-threshold-met-single");

        assertLogContaining("Mutating com.reeltwo.jumble.buildhelpers.examples.WellTested");
        assertLogContaining("Score: 75%");
    }

    public void testShouldFailWithScoreLessThanThreshold() {
        expectBuildExceptionContaining("class-threshold-not-met-single", "threshold not met",
            "score for com.reeltwo.jumble.buildhelpers.examples.WellTested: 75%," + " threshold: 100%");
    }

    public void testShouldSucceedWhenAllJumblesGreaterThanThreshold() {
        executeTarget("class-threshold-met-multiple");

        assertLogContaining("Mutating com.reeltwo.jumble.buildhelpers.examples.WellTested");
        assertLogContaining("Score: 75%");
        assertLogContaining("Mutating com.reeltwo.jumble.buildhelpers.examples.CompletelyTested");
        assertLogContaining("Score: 100%");
    }

    public void testShouldFailWithSomeScoresLessThanThreshold() {
        expectBuildExceptionContaining("class-threshold-not-met-most", "threshold not met",
            "score for com.reeltwo.jumble.buildhelpers.examples.WellTested: 75%," + " threshold: 90%");
        expectBuildExceptionContaining("class-threshold-not-met-most", "threshold not met",
            "score for com.reeltwo.jumble.buildhelpers.examples.NotTested: 0%," + " threshold: 90%");
    }
}

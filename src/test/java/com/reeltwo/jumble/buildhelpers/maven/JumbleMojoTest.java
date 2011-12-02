package com.reeltwo.jumble.buildhelpers.maven;

import java.io.File;
import java.util.List;
import java.util.Properties;

import static java.io.File.*;
import static java.lang.System.*;
import static java.util.Arrays.*;

import com.reeltwo.jumble.buildhelpers.JumbleCommandLineRunner;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JumbleMojoTest {
    @Test
    public void maximallySpecifiedCommandLine() throws Exception {
        MavenProject maven = mock(MavenProject.class);
        when(maven.getTestClasspathElements()).thenReturn(asList("target/test-classes", "target/classes"));

        JumbleMojo jumble = new JumbleMojo();
        jumble.mavenProjectIs(maven);
        jumble.jumbleJarIs(new File("/Users/pholser/jumble_1_1_0/jumble/jumble.jar"));
        jumble.targetClassesDirIs(new File("target/classes"));
        jumble.testClassesDirIs(new File("target/test-classes"));
        jumble.targetIncludesAre("**/Percentage.class");
        jumble.targetExcludesAre("**/Percentage$$*.class");
        jumble.testIncludesAre("**/*Test.class");
        jumble.testExcludesAre("**/*$$*.class");
        jumble.mutateConstantPool(true);
        jumble.firstMutationIndex(1);
        jumble.mutateIncrements(true);
        jumble.mutateInlineConstants(true);
        jumble.maxMutations(2);
        jumble.loadCache(false);
        jumble.orderTests(false);
        jumble.saveCache(false);
        jumble.useCache(false);
        jumble.mutateReturnValues(true);
        jumble.mutateSwitchCases(true);
        jumble.outputDirIs(new File("target/jumble-reports"));
        jumble.excludedMethodsAre("wait", "notify", "notifyAll");
        jumble.classThresholdIs(90);
        jumble.mutateAssignments(true);
        jumble.emitVerboseOutput(true);
        Properties properties = new Properties();
        properties.setProperty("aProperty", "aValue");
        properties.setProperty("anotherProperty", "anotherValue");
        jumble.additionalSystemPropertiesAre(properties);

        List<JumbleCommandLineRunner> runners = jumble.createRunners();
        assertEquals(1, runners.size());
        JumbleCommandLineRunner runner = runners.get(0);
        assertEquals(
            asList(getProperty("java.home") + separator + "bin" + separator + "java",
                "-jar",
                "/Users/pholser/jumble_1_1_0/jumble/jumble.jar",
                "com.reeltwo.jumble.buildhelpers.util.Percentage",
                "--classpath=target/test-classes" + System.getProperty("path.separator") + "target/classes",
                "--define-property=anotherProperty=anotherValue",
                "--define-property=aProperty=aValue",
                "--cpool",
                "--first-mutation=1",
                "--increments",
                "--inline-consts",
                "--max-external-mutations=2",
                "--no-load-cache",
                "--no-order",
                "--no-save-cache",
                "--no-use-cache",
                "--return-vals",
                "--switch",
                "--stores",
                "--verbose",
                "--exclude=wait,notify,notifyAll",
                "com.reeltwo.jumble.buildhelpers.ant.JumbleTaskBuildFileTest",
                "com.reeltwo.jumble.buildhelpers.ant.JumbleTaskTest",
                "com.reeltwo.jumble.buildhelpers.maven.JumbleMojoTest",
                "com.reeltwo.jumble.buildhelpers.util.PercentageTest"),
            asList(runner.getCommandLine()));
    }
}

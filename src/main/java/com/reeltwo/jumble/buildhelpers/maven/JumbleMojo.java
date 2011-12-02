package com.reeltwo.jumble.buildhelpers.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.*;

import com.reeltwo.jumble.buildhelpers.JumbleCommandLineRunner;
import com.reeltwo.jumble.buildhelpers.JumbleExecutor;
import com.reeltwo.jumble.buildhelpers.JumbleRunnerBuilder;
import com.reeltwo.jumble.buildhelpers.JumbleScoreResult;
import com.reeltwo.jumble.buildhelpers.util.Percentage;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.ProjectComponent;
import org.codehaus.plexus.util.FileUtils;

import static org.codehaus.plexus.util.StringUtils.*;

/**
 * @goal jumble
 * @phase test
 * @requiresDependencyResolution test
 */
public class JumbleMojo extends AbstractMojo {
    /**
     * Reference to the current Maven project.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    void mavenProjectIs(MavenProject newProject) {
        project = newProject;
    }

    /**
     * The location of Jumble's JAR file.
     *
     * @parameter expression="${jumble.jar}"
     * @required
     */
    private File jumbleJar;

    void jumbleJarIs(File jar) {
        jumbleJar = jar;
    }

    /**
     * Where the classes to be jumbled are rooted.
     *
     * @parameter default-value="${project.build.outputDirectory}"
     */
    private File targetClassesDir;

    void targetClassesDirIs(File dir) {
        targetClassesDir = dir;
    }

    /**
     * Where the test classes that exercise the jumbling are rooted.
     *
     * @parameter default-value="${project.build.testOutputDirectory}"
     */
    private File testClassesDir;

    void testClassesDirIs(File dir) {
        testClassesDir = dir;
    }

    /**
     * Patterns matching classes to be jumbled.
     *
     * @parameter expression="${jumble.target.includes}"
     * @required
     */
    private ArrayList<String> targetIncludes;

    void targetIncludesAre(String... patterns) {
        targetIncludes = new ArrayList<String>(asList(patterns));
    }

    /**
     * Patterns matching classes to be excluded from jumbling.
     *
     * @parameter expression="${jumble.target.excludes}"
     */
    private ArrayList<String> targetExcludes;

    void targetExcludesAre(String... patterns) {
        targetExcludes = new ArrayList<String>(asList(patterns));
    }

    /**
     * Patterns matching test classes to be used to trigger the jumbling.
     *
     * @parameter expression="${jumble.test.includes}"
     * @required
     */
    private ArrayList<String> testIncludes;

    void testIncludesAre(String... patterns) {
        testIncludes = new ArrayList<String>(asList(patterns));
    }

    /**
     * Patterns matching test classes to be excluded from triggering the jumbling process.
     *
     * @parameter expression="${jumble.test.excludes}"
     */
    private ArrayList<String> testExcludes;

    void testExcludesAre(String... patterns) {
        testExcludes = new ArrayList<String>(asList(patterns));
    }

    /**
     * Whether to mutate the constant pools of target classes.
     *
     * @parameter expression="${jumble.constant.pool}" default-value="false"
     */
    private boolean constantPool;

    void mutateConstantPool(boolean setting) {
        constantPool = setting;
    }

    /**
     * Index of first mutation to attempt (for testing).
     *
     * @parameter expression="${jumble.first.mutation.index}" default-value="0"
     */
    private int firstMutation;

    void firstMutationIndex(int index) {
        firstMutation = index;
    }

    /**
     * Whether to mutate increments in target classes.
     *
     * @parameter expression="${jumble.increments}" default-value="false"
     */
    private boolean increments;

    void mutateIncrements(boolean setting) {
        increments = setting;
    }

    /**
     * Whether to mutate inline constants in target classes.
     *
     * @parameter expression="${jumble.inline.constants}" default-value="false"
     */
    private boolean inlineConstants;

    void mutateInlineConstants(boolean setting) {
        inlineConstants = setting;
    }

    /**
     * Maximum number of mutations to attempt in external JVM.
     *
     * @parameter expression="${jumble.max.mutations}" default-value="0"
     */
    private int maxMutations;

    void maxMutations(int count) {
        maxMutations = count;
    }

    /**
     * Whether to load Jumble's cache of previous run.
     *
     * @parameter expression="${jumble.load.cache}" default-value="true"
     */
    private boolean loadCache;

    void loadCache(boolean setting) {
        loadCache = setting;
    }

    /**
     * Whether to order test by run time.
     *
     * @parameter expression="${jumble.order}" default-value="true"
     */
    private boolean order;

    void orderTests(boolean setting) {
        order = setting;
    }

    /**
     * Whether to save Jumble's cache of previous run.
     *
     * @parameter expression="${jumble.save.cache}" default-value="true"
     */
    private boolean saveCache;

    void saveCache(boolean setting) {
        saveCache = setting;
    }

    /**
     * Whether to use Jumble's cache of previous run.
     *
     * @parameter expression="${jumble.use.cache}" default-value="true"
     */
    private boolean useCache;

    void useCache(boolean setting) {
        useCache = setting;
    }

    /**
     * Whether to mutate return values in target classes.
     *
     * @parameter expression="${jumble.return.values}" default-value="false"
     */
    private boolean returnValues;

    void mutateReturnValues(boolean setting) {
        returnValues = setting;
    }

    /**
     * Whether to mutate switch cases in target classes.
     *
     * @parameter expression="${jumble.switch.cases}" default-value="false"
     */
    private boolean switchCases;

    void mutateSwitchCases(boolean setting) {
        switchCases = setting;
    }

    /**
     * Where to write Jumble's output.
     *
     * @parameter default-value="${project.build.directory}/jumble-reports"
     */
    private File outputDir;

    void outputDirIs(File dir) {
        outputDir = dir;
    }

    /**
     * Names of methods that are not to be jumbled.
     *
     * @parameter expression="${jumble.excludedMethods}"
     */
    private ArrayList<String> excludedMethods;

    void excludedMethodsAre(String... names) {
        excludedMethods = new ArrayList<String>(asList(names));
    }

    /**
     * If any class's Jumble score dips below this threshold, fail the build.
     *
     * @parameter expression="${jumble.class.threshold}" default-value="0"
     */
    private int classThreshold;

    void classThresholdIs(int percentage) {
        classThreshold = percentage;
    }

    /**
     * Whether to mutate assignments in target classes.
     *
     * @parameter expression="${jumble.assignments}" default-value="false"
     */
    private boolean assignments;

    void mutateAssignments(boolean setting) {
        assignments = setting;
    }

    /**
     * Whether to have Jumble emit verbose output.
     *
     * @parameter expression="${jumble.verbose}" default-value="false"
     */
    private boolean verbose;

    void emitVerboseOutput(boolean setting) {
        verbose = setting;
    }

    /**
     * Additional system properties to define in the JVM used to run unit tests.
     *
     * @parameter expression="${jumble.sysproperties}"
     */
    private Properties sysProperties = new Properties();

    void additionalSystemPropertiesAre(Properties properties) {
        sysProperties = properties;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        List<JumbleCommandLineRunner> runners = createRunners();

        try {
            ProjectComponent fake = new ProjectComponent() {
                // fakery
            };
            List<JumbleScoreResult> failures =
                new JumbleExecutor(fake, runners, new Percentage(classThreshold), outputDir).execute();
            if (!failures.isEmpty())
                throw new JumbleMojoFailure(failures);
        } catch (IOException ex) {
            throw new MojoExecutionException("Jumble failed", ex);
        }
    }

    List<JumbleCommandLineRunner> createRunners() throws MojoExecutionException {
        JumbleRunnerBuilder builder = new JumbleRunnerBuilder();
        builder.jumbleJar(jumbleJar);
        builder.addClasspathElements(testClasspath());
        for (File each : files(testClassesDir, testIncludes, testExcludes))
            builder.addTestClass(each.getPath());
        builder.mutateConstantPool(constantPool);
        builder.firstMutationIndex(firstMutation);
        builder.mutateIncrements(increments);
        builder.mutateInlineConstants(inlineConstants);
        builder.maxMutations(maxMutations);
        builder.loadCache(loadCache);
        builder.orderTests(order);
        builder.saveCache(saveCache);
        builder.useCache(useCache);
        builder.mutateReturnValues(returnValues);
        builder.mutateSwitchCases(switchCases);
        builder.excludeMethods(excludedMethods);
        builder.mutateAssignments(assignments);
        builder.emitVerboseOutput(verbose);
        for (Enumeration<?> en = sysProperties.propertyNames(); en.hasMoreElements();) {
            String key = (String) en.nextElement();
            builder.addSystemProperty(key, sysProperties.getProperty(key));
        }
        List<JumbleCommandLineRunner> runners = new ArrayList<JumbleCommandLineRunner>();
        for (File each : files(targetClassesDir, targetIncludes, targetExcludes))
            runners.add(builder.getRunnerForTargetClass(each));
        return runners;
    }

    private List<String> testClasspath() throws MojoExecutionException {
        try {
            return project.getTestClasspathElements();
        } catch (DependencyResolutionRequiredException ex) {
            throw new MojoExecutionException("Problems resolving test classpath", ex);
        }
    }

    private List<File> files(File dir, List<String> includes, List<String> excludes) throws MojoExecutionException {
        try {
            return FileUtils.getFiles(dir, includeSpec(includes), includeSpec(excludes), false);
        } catch (IOException ex) {
            throw new MojoExecutionException("Problems processing files", ex);
        }
    }

    private static String includeSpec(List<String> items) {
        return join(items.iterator(), ",");
    }
}

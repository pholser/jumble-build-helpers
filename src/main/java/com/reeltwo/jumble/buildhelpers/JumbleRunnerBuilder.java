package com.reeltwo.jumble.buildhelpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.*;
import static java.util.regex.Pattern.*;

import org.codehaus.plexus.util.StringUtils;

import org.apache.tools.ant.BuildException;

public class JumbleRunnerBuilder {
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    private File jumbleJar;
    private final List<Object> classpathElements = new ArrayList<Object>();
    private final List<String> testClasses = new ArrayList<String>();
    private final List<String> sysProperties = new ArrayList<String>();
    private boolean mutateConstantPool;
    private int firstMutationIndex;
    private boolean mutateIncrements;
    private boolean mutateInlineConstants;
    private int maxMutations;
    private boolean loadCache = true;
    private boolean orderTests = true;
    private boolean saveCache = true;
    private boolean useCache = true;
    private boolean mutateReturnValues;
    private boolean mutateSwitchCases;
    private String excludedMethodSpec;
    private File excludedMethodsFile;
    private boolean mutateAssignments;
    private boolean verbose;

    public void jumbleJar(File jar) {
        jumbleJar = jar;
    }

    public boolean jumbleJarSpecified() {
        return jumbleJar != null;
    }

    public void addClasspathElements(List<?> elements) {
        classpathElements.addAll(elements);
    }

    public void addTestClass(String path) {
        testClasses.add(convertClassFilePathToClassName(path));
    }

    public void addSystemProperty(String key, String value) {
        sysProperties.add(key + '=' + value);
    }

    public void mutateConstantPool(boolean mutate) {
        this.mutateConstantPool = mutate;
    }

    public void firstMutationIndex(int index) {
        this.firstMutationIndex = index;
    }

    public void mutateIncrements(boolean mutate) {
        this.mutateIncrements = mutate;
    }

    public void mutateInlineConstants(boolean mutate) {
        this.mutateInlineConstants = mutate;
    }

    public void maxMutations(int max) {
        this.maxMutations = max;
    }

    public void loadCache(boolean load) {
        this.loadCache = load;
    }

    public void orderTests(boolean order) {
        this.orderTests = order;
    }

    public void saveCache(boolean save) {
        this.saveCache = save;
    }

    public void useCache(boolean use) {
        this.useCache = use;
    }

    public void mutateReturnValues(boolean mutate) {
        this.mutateReturnValues = mutate;
    }

    public void mutateSwitchCases(boolean mutate) {
        this.mutateSwitchCases = mutate;
    }

    public void excludeMethods(String methodSpec) {
        this.excludedMethodSpec = methodSpec;
    }

    public void excludeMethods(List<String> methodSpecs) {
        this.excludedMethodSpec = methodSpec(methodSpecs);
    }

    private String methodSpec(List<String> methodSpecs) {
        return StringUtils.join(methodSpecs.iterator(), ",");
    }

    public void mutateAssignments(boolean mutate) {
        this.mutateAssignments = mutate;
    }

    public void emitVerboseOutput(boolean verboseOutput) {
        this.verbose = verboseOutput;
    }

    public JumbleCommandLineRunner getRunnerForTargetClass(File file) {
        return getRunnerForTargetClass(convertClassFilePathToClassName(file.getPath()));
    }

    private JumbleCommandLineRunner getRunnerForTargetClass(String className) {
        JumbleCommandLineRunner runner = new JumbleCommandLineRunner(jumbleJar, className);
        addOptions(runner);
        addTestClasses(runner);
        return runner;
    }

    void addOptions(JumbleCommandLineRunner runner) {
        if (!classpathElements.isEmpty())
            runner.addClasspath(createClasspath());

        runner.addSystemProperties(sysProperties);

        if (mutateConstantPool)
            runner.mutateConstantPool();
        if (firstMutationIndex != 0)
            runner.firstMutationIndex(firstMutationIndex);
        if (mutateIncrements)
            runner.mutateIncrements();
        if (mutateInlineConstants)
            runner.mutateInlineConstants();
        if (maxMutations != 0)
            runner.maxMutations(maxMutations);
        if (!loadCache)
            runner.doNotLoadCache();
        if (!orderTests)
            runner.doNotOrderTests();
        if (!saveCache)
            runner.doNotSaveCache();
        if (!useCache)
            runner.doNotUseCache();
        if (mutateReturnValues)
            runner.mutateReturnValues();
        if (mutateSwitchCases)
            runner.mutateSwitchCases();
        if (mutateAssignments)
            runner.mutateAssignments();
        if (verbose)
            runner.emitVerboseOutput();
        List<String> excludedMethods = excludedMethodsFromFile();
        runner.excludeMethods(excludedMethodSpec, excludedMethods);
    }

    private String createClasspath() {
        StringBuilder classpathArg = new StringBuilder();
        for (Iterator<?> iter = classpathElements.iterator(); iter.hasNext();) {
            classpathArg.append(iter.next());
            if (iter.hasNext())
                classpathArg.append(PATH_SEPARATOR);
        }

        return classpathArg.toString();
    }

    private void addTestClasses(JumbleCommandLineRunner runner) {
        for (String each : testClasses)
            runner.addTestClass(each);
    }

    private List<String> excludedMethodsFromFile() {
        if (excludedMethodsFile == null)
            return emptyList();

        List<String> excludedMethods = new ArrayList<String>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(excludedMethodsFile));
            for (String line = reader.readLine(); line != null; line = reader.readLine())
                excludedMethods.add(line);

            return excludedMethods;
        } catch (IOException ex) {
            throw new BuildException(ex);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ignored) {
                // on purpose
            }
        }
    }

    private String convertClassFilePathToClassName(String filePath) {
        return filePath.replaceAll(quote(File.separator), ".").replaceAll("\\.class$", "");
    }
}

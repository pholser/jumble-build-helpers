package com.reeltwo.jumble.buildhelpers.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.reeltwo.jumble.buildhelpers.JumbleCommandLineRunner;
import com.reeltwo.jumble.buildhelpers.JumbleExecutor;
import com.reeltwo.jumble.buildhelpers.JumbleRunnerBuilder;
import com.reeltwo.jumble.buildhelpers.JumbleScoreResult;
import com.reeltwo.jumble.buildhelpers.util.Percentage;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.PropertyResource;

public class JumbleTask extends Task {
    public static final String MISSING_JUMBLE_JAR = "Need to specify location of Jumble jar";
    public static final String MISSING_TARGET_CLASSES = "Either no target classes were specified, or none were detected";
    public static final String OUTPUTDIR_DOES_NOT_EXIST = "Specified outputdir does not exist";
    public static final String OUTPUTDIR_IS_NOT_WRITABLE = "Specified outputdir is not writable";
    public static final String OUTPUTDIR_IS_NOT_A_DIRECTORY = "Specified outputdir is not a directory";

    private final JumbleRunnerBuilder builder = new JumbleRunnerBuilder();
    private final List<FileSet> targetClasses = new ArrayList<FileSet>();
    private File outputDirectory;
    private Percentage classThreshold = new Percentage(0);

    @Override
    public void execute() {
        checkJumbleJar();
        checkTargetClasses();
        checkOutputDirectory();

        executeJumble();
    }

    public void setJar(File jar) {
        builder.jumbleJar(jar);
    }

    public void addConfiguredTargetclasses(FileSet classes) {
        targetClasses.add(classes);
    }

    public void addConfiguredClasspath(Path path) {
        builder.addClasspathElements(Collections.<Object> singletonList(path));
    }

    public void addConfiguredTestclasses(FileSet classes) {
        for (Iterator<?> iter = classes.iterator(); iter.hasNext();)
            builder.addTestClass(((FileResource) iter.next()).getName());
    }

    public void addSysproperty(Environment.Variable property) {
        builder.addSystemProperty(property.getKey(), property.getValue());
    }

    public void addSyspropertyset(PropertySet properties) {
        for (Iterator<?> iter = properties.iterator(); iter.hasNext();) {
            PropertyResource next = (PropertyResource) iter.next();
            builder.addSystemProperty(next.getName(), next.getValue());
        }
    }

    public void setConstantpool(boolean mutate) {
        builder.mutateConstantPool(mutate);
    }

    public void setFirstmutation(int index) {
        builder.firstMutationIndex(index);
    }

    public void setIncrements(boolean mutate) {
        builder.mutateIncrements(mutate);
    }

    public void setInlineconstants(boolean mutate) {
        builder.mutateInlineConstants(mutate);
    }

    public void setMaxmutations(int max) {
        builder.maxMutations(max);
    }

    public void setLoadcache(boolean load) {
        builder.loadCache(load);
    }

    public void setOrder(boolean order) {
        builder.orderTests(order);
    }

    public void setSavecache(boolean save) {
        builder.saveCache(save);
    }

    public void setUsecache(boolean use) {
        builder.useCache(use);
    }

    public void setReturnvalues(boolean mutate) {
        builder.mutateReturnValues(mutate);
    }

    public void setSwitch(boolean mutate) {
        builder.mutateSwitchCases(mutate);
    }

    public void setOutputdir(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setExcludemethods(String methodSpec) {
        builder.excludeMethods(methodSpec);
    }

    public void setClassthreshold(Percentage threshold) {
        this.classThreshold = threshold;
    }

    public void setAssignments(boolean mutate) {
        builder.mutateAssignments(mutate);
    }

    public void setVerbose(boolean verbose) {
        builder.emitVerboseOutput(verbose);
    }

    List<JumbleCommandLineRunner> getRunners() {
        List<JumbleCommandLineRunner> runners = new ArrayList<JumbleCommandLineRunner>();
        for (FileSet each : targetClasses)
            runners.addAll(getRunnersForTargetClasses(each));
        return runners;
    }

    private List<JumbleCommandLineRunner> getRunnersForTargetClasses(FileSet files) {
        List<JumbleCommandLineRunner> runners = new ArrayList<JumbleCommandLineRunner>();
        for (Iterator<?> iter = files.iterator(); iter.hasNext();) {
            FileResource next = (FileResource) iter.next();
            runners.add(builder.getRunnerForTargetClass(new File(next.getName())));
        }
        return runners;
    }

    private void checkJumbleJar() {
        if (!builder.jumbleJarSpecified())
            throw new BuildException(MISSING_JUMBLE_JAR, getLocation());
    }

    private void checkTargetClasses() {
        for (FileSet each : targetClasses) {
            if (each.size() > 0)
                return;
        }

        throw new BuildException(MISSING_TARGET_CLASSES, getLocation());
    }

    private void checkOutputDirectory() {
        if (outputDirectory == null)
            return;

        if (!outputDirectory.exists())
            throw new BuildException(OUTPUTDIR_DOES_NOT_EXIST, getLocation());

        if (!outputDirectory.isDirectory())
            throw new BuildException(OUTPUTDIR_IS_NOT_A_DIRECTORY, getLocation());

        if (!outputDirectory.canWrite())
            throw new BuildException(OUTPUTDIR_IS_NOT_WRITABLE, getLocation());
    }

    private void executeJumble() {
        try {
            List<JumbleScoreResult> failures =
                new JumbleExecutor(this, getRunners(), classThreshold, outputDirectory).execute();
            if (!failures.isEmpty())
                throw new JumbleTaskFailure(failures, getLocation());
        } catch (IOException ex) {
            throw new BuildException("Problem executing Jumble", ex);
        }
    }
}

package com.reeltwo.jumble.buildhelpers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.types.CommandlineJava;

public class JumbleCommandLineRunner {
    static final String PATH_SEPARATOR = System.getProperty("path.separator");

    private final CommandlineJava java = new CommandlineJava();
    private final String targetClass;

    JumbleCommandLineRunner(File jumbleJar, String targetClass) {
        this.targetClass = targetClass;

        java.createVmArgument().setValue("-jar");
        java.createVmArgument().setValue(jumbleJar.getAbsolutePath());
        java.createArgument().setValue(targetClass);
    }

    void addClasspath(String classpath) {
        java.createArgument().setValue("--classpath=" + classpath);
    }

    void addSystemProperties(List<String> properties) {
        for (String each : properties)
            java.createArgument().setValue("--define-property=" + each);
    }

    void addJVMArguments(List<String> jvmArgs) {
        for (String each : jvmArgs)
            java.createArgument().setValue("--jvm-arg=" + each);
    }

    void mutateConstantPool() {
        java.createArgument().setValue("--cpool");
    }

    void firstMutationIndex(int firstMutationIndex) {
        java.createArgument().setValue("--first-mutation=" + firstMutationIndex);
    }

    void mutateIncrements() {
        java.createArgument().setValue("--increments");
    }

    void mutateInlineConstants() {
        java.createArgument().setValue("--inline-consts");
    }

    void maxMutations(int maxMutations) {
        java.createArgument().setValue("--max-external-mutations=" + maxMutations);
    }

    void doNotLoadCache() {
        java.createArgument().setValue("--no-load-cache");
    }

    void doNotOrderTests() {
        java.createArgument().setValue("--no-order");
    }

    void doNotSaveCache() {
        java.createArgument().setValue("--no-save-cache");
    }

    void doNotUseCache() {
        java.createArgument().setValue("--no-use-cache");
    }

    void mutateReturnValues() {
        java.createArgument().setValue("--return-vals");
    }

    void mutateSwitchCases() {
        java.createArgument().setValue("--switch");
    }

    void mutateAssignments() {
        java.createArgument().setValue("--stores");
    }

    void emitVerboseOutput() {
        java.createArgument().setValue("--verbose");
    }

    void excludeMethods(String methodSpec, List<String> methods) {
        if (methodSpec == null && methods.isEmpty())
            return;

        StringBuilder value = new StringBuilder("--exclude=");
        if (methodSpec != null) {
            value.append(methodSpec);

            if (!methods.isEmpty())
                value.append(',');
        }

        for (String each : methods)
            value.append(each).append(',');

        java.createArgument().setValue(value.toString().replaceAll("[,]$", ""));
    }

    void addTestClass(String testClassName) {
        java.createArgument().setValue(testClassName);
    }

    public String getTargetClass() {
        return targetClass;
    }

    public String[] getCommandLine() {
        String[] command = java.getCommandline();
        System.out.println(Arrays.toString(command));
        return command;
    }

    public String describeCommand() {
        return java.describeCommand();
    }
}

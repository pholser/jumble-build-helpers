package com.reeltwo.jumble.buildhelpers.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.io.File.*;
import static java.lang.System.*;
import static java.util.regex.Pattern.*;

import static com.reeltwo.jumble.buildhelpers.JumbleRunnerBuilder.*;

import com.reeltwo.jumble.buildhelpers.JumbleCommandLineRunner;

import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JumbleTaskTest {
    static final String TEMP_PATH = getProperty("java.io.tmpdir").replaceAll("/$", "");

    private JumbleTask task;
    private File classesDir;
    private List<List<String>> expectedCommandLines;
    private File jumbleJar;

    @Before
    public void setUp() throws Exception {
        task = makeNewTask();
        classesDir = makeNewDirectory(new File(TEMP_PATH), "classes");
        jumbleJar = makeNewFile("jumble", ".jar", new File(TEMP_PATH));
        task.setJar(jumbleJar);

        expectedCommandLines = new ArrayList<List<String>>();
        setTargetClasses("com" + separator + "foo", "AAA", "BBB");
    }

    @Test
    public void shouldReflectTargetClassesOnCommandLine() {
        assertCommandLine();
    }

    @Test
    public void shouldReflectClasspathOnCommandLine() {
        setClasspath(TEMP_PATH, getProperty("user.home"), getProperty("user.dir"));

        assertCommandLine();
    }

    @Test
    public void shouldReflectSystemPropertiesOnCommandLine() {
        addSysProperty("1stKey", "1stValue");
        addSysProperty("2ndKey", "2ndValue");
        addSysPropertySetReferringTo("aKey", "aValue", "anotherKey", "anotherValue");

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToMutateConstantPoolOnCommandLine() {
        mutateConstantPool();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToSpecifyFirstMutationIndexOnCommandLine() {
        setMutationIndex(2);

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToMutateIncrementsOnCommandLine() {
        mutateIncrements();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToMutateInlineConstantsOnCommandLine() {
        mutateInlineConstants();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToSpecifyMaximumNumberOfMutationsOnCommandLine() {
        setMaxMutations(100);

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestNotToLoadCacheOnCommandLine() {
        doNotLoadCache();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestNotToOrderTestsOnCommandLine() {
        doNotOrderTests();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestNotToSaveCacheOnCommandLine() {
        doNotSaveCache();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestNotToUseCacheOnCommandLine() {
        doNotUseCache();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToMutateReturnValuesOnCommandLine() {
        mutateReturnValues();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToMutateSwitchCasesOnCommandLine() {
        mutateSwitchCases();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToMutateAssignmentsOnCommandLine() {
        mutateAssignments();

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToEmitVerboseOutputOnCommandLine() {
        emitVerboseOutput();

        assertCommandLine();
    }

    @Test
    public void shouldReflectTestClassesOnCommandLine() throws Exception {
        setTestClasses("com" + separator + "bar", "AAATest", "BBBTest");

        assertCommandLine();
    }

    @Test
    public void shouldOrderTargetClassOptionsAndTestClassesCorrectlyOnCommandLine() throws Exception {
        setTargetClasses("com" + separator + "foo", "AAA", "BBB");
        setClasspath(TEMP_PATH, getProperty("user.home"), getProperty("user.dir"));
        setTestClasses("com" + separator + "bar", "AAATest", "BBBTest");

        assertCommandLine();
    }

    @Test
    public void shouldReflectRequestToExcludeMethodsOnCommandLine() {
        excludeMethods("toString", "hashCode");

        assertCommandLine();
    }

    @Test
    public void shouldAllowExcludedMethodsInAttributeAndInFile() {
        task.setExcludemethods("wait,notify");

        addToEachCommandLine("--exclude=" + methodSpec("wait", "notify"));

        assertCommandLine();
    }

    private void assertCommandLine() {
        List<List<String>> actualCommandLines = new ArrayList<List<String>>();
        for (JumbleCommandLineRunner each : task.getRunners())
            actualCommandLines.add(Arrays.asList(each.getCommandLine()));

        assertEquals(expectedCommandLines, actualCommandLines);
    }

    private void setClasspath(String firstPath, String secondPath, String thirdPath) {
        Path firstClasspath = makeNewPath();
        firstClasspath.add(makeNewPath(firstPath));
        firstClasspath.add(makeNewPath(secondPath));
        task.addConfiguredClasspath(firstClasspath);

        Path secondClasspath = makeNewPath();
        secondClasspath.add(makeNewPath(thirdPath));
        task.addConfiguredClasspath(secondClasspath);

        addToEachCommandLine("--classpath=" + firstPath + PATH_SEPARATOR + secondPath + PATH_SEPARATOR + thirdPath);
    }

    private void addSysProperty(String key, String value) {
        task.addSysproperty(makeNewProperty(key, value));

        addToEachCommandLine("--define-property=" + key + '=' + value);
    }

    private void addSysPropertySetReferringTo(String firstKey, String firstValue, String secondKey,
        String secondValue) {

        setProjectProperty(firstKey, firstValue);
        setProjectProperty(secondKey, secondValue);

        PropertySet properties = makeNewPropertySet();
        properties.addPropertyref(makeNewPropertyReference(firstKey));
        properties.addPropertyref(makeNewPropertyReference(secondKey));
        task.addSyspropertyset(properties);

        addToEachCommandLine("--define-property=" + secondKey + '=' + secondValue);
        addToEachCommandLine("--define-property=" + firstKey + '=' + firstValue);
    }

    private void mutateConstantPool() {
        task.setConstantpool(true);

        addToEachCommandLine("--cpool");
    }

    private void setMutationIndex(int index) {
        task.setFirstmutation(index);

        addToEachCommandLine("--first-mutation=" + index);
    }

    private void mutateIncrements() {
        task.setIncrements(true);

        addToEachCommandLine("--increments");
    }

    private void mutateInlineConstants() {
        task.setInlineconstants(true);

        addToEachCommandLine("--inline-consts");
    }

    private void setMaxMutations(int max) {
        task.setMaxmutations(max);

        addToEachCommandLine("--max-external-mutations=" + max);
    }

    private void doNotLoadCache() {
        task.setLoadcache(false);

        addToEachCommandLine("--no-load-cache");
    }

    private void doNotOrderTests() {
        task.setOrder(false);

        addToEachCommandLine("--no-order");
    }

    private void doNotSaveCache() {
        task.setSavecache(false);

        addToEachCommandLine("--no-save-cache");
    }

    private void doNotUseCache() {
        task.setUsecache(false);

        addToEachCommandLine("--no-use-cache");
    }

    private void mutateReturnValues() {
        task.setReturnvalues(true);

        addToEachCommandLine("--return-vals");
    }

    private void mutateSwitchCases() {
        task.setSwitch(true);

        addToEachCommandLine("--switch");
    }

    private void mutateAssignments() {
        task.setAssignments(true);

        addToEachCommandLine("--stores");
    }

    private void emitVerboseOutput() {
        task.setVerbose(true);

        addToEachCommandLine("--verbose");
    }

    private void excludeMethods(String firstMethod, String secondMethod) {
        String methodSpec = methodSpec(firstMethod, secondMethod);
        task.setExcludemethods(methodSpec);

        addToEachCommandLine("--exclude=" + methodSpec);
    }

    private String methodSpec(String firstMethod, String secondMethod) {
        return firstMethod + ',' + secondMethod;
    }

    private void setTargetClasses(String packageDirPath, String firstClassName, String secondClassName)
        throws Exception {

        File packageDir = makeNewDirectory(classesDir, packageDirPath);
        File firstClass = makeNewFile(firstClassName, ".class", packageDir);
        File secondClass = makeNewFile(secondClassName, ".class", packageDir);

        FileSet classes = makeNewFileSet();
        classes.setDir(classesDir);
        classes.setIncludes("**/" + firstClass.getName() + ',' + "**/" + secondClass.getName());

        task.addConfiguredTargetclasses(classes);

        String packageName = convertPackageDirPathToPackageName(packageDirPath);
        File[] targetClasses = new File[] { firstClass, secondClass };

        for (File each : targetClasses) {
            List<String> newCommandLine = new ArrayList<String>();
            newCommandLine.add(getProperty("java.home") + separator + "bin" + separator + "java");
            newCommandLine.add("-jar");
            newCommandLine.add(jumbleJar.getAbsolutePath());
            newCommandLine.add(packageName + '.' + stripClassSuffix(each.getName()));
            expectedCommandLines.add(newCommandLine);
        }
    }

    private void setTestClasses(String packageDirPath, String firstClassName, String secondClassName) throws Exception {
        File packageDir = makeNewDirectory(classesDir, packageDirPath);
        File firstClass = makeNewFile(firstClassName, ".class", packageDir);
        File secondClass = makeNewFile(secondClassName, ".class", packageDir);

        FileSet classes = makeNewFileSet();
        classes.setDir(classesDir);
        classes.setIncludes("**/" + firstClass.getName() + ',' + "**/" + secondClass.getName());

        task.addConfiguredTestclasses(classes);

        String packageName = convertPackageDirPathToPackageName(packageDirPath);
        addToEachCommandLine(packageName + '.' + stripClassSuffix(firstClass.getName()));
        addToEachCommandLine(packageName + '.' + stripClassSuffix(secondClass.getName()));
    }

    private JumbleTask makeNewTask() {
        JumbleTask newTask = new JumbleTask();

        Project project = new Project();
        project.setBasedir(getProperty("user.home"));
        newTask.setProject(project);
        newTask.setLocation(new Location(getClass().getName()));

        return newTask;
    }

    private File makeNewDirectory(File parent, String name) {
        File newDirectory = new File(parent, name);
        newDirectory.mkdirs();
        assertTrue(newDirectory.exists());
        return newDirectory;
    }

    private File makeNewFile(String prefix, String suffix, File directory) throws Exception {
        File newFile = createTempFile(prefix, suffix, directory);
        assertTrue(newFile.exists());
        return newFile;
    }

    private Path makeNewPath() {
        Path newPath = new Path(task.getProject());
        newPath.setLocation(task.getLocation());
        return newPath;
    }

    private Path makeNewPath(String path) {
        Path newPath = makeNewPath();
        newPath.setPath(path);
        return newPath;
    }

    private FileSet makeNewFileSet() {
        FileSet newFileSet = new FileSet();
        newFileSet.setProject(task.getProject());
        newFileSet.setLocation(task.getLocation());
        return newFileSet;
    }

    private Environment.Variable makeNewProperty(String key, String value) {
        Environment.Variable newProperty = new Environment.Variable();
        newProperty.setKey(key);
        newProperty.setValue(value);
        return newProperty;
    }

    private PropertySet makeNewPropertySet() {
        PropertySet properties = new PropertySet();
        properties.setProject(task.getProject());
        properties.setLocation(task.getLocation());
        return properties;
    }

    private PropertySet.PropertyRef makeNewPropertyReference(String key) {
        PropertySet.PropertyRef reference = new PropertySet.PropertyRef();
        reference.setName(key);
        return reference;
    }

    private void setProjectProperty(String key, String value) {
        task.getProject().setProperty(key, value);
    }

    private String convertPackageDirPathToPackageName(String packageDirPath) {
        return packageDirPath.replaceAll(quote(separator), ".");
    }

    private String stripClassSuffix(String fileName) {
        return fileName.replaceAll("\\.class$", "");
    }

    private void addToEachCommandLine(String argument) {
        for (List<String> each : expectedCommandLines)
            each.add(argument);
    }
}

package com.umayrh.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.gradle.testkit.runner.TaskOutcome.*;

/**
 * Class that tests RScriptTask
 * See also https://docs.gradle.org/current/userguide/test_kit.html
 */
class RScriptTaskTest {
    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File buildFile;
    String buildFileHeader = "plugins { id 'com.umayrh.rplugin' }\n\n";

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
    }

    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('rScriptTask', type: RScriptTask)
        assertTrue(task instanceof RScriptTask)
    }

    @Ignore("Not ready - somehow fails to find RScriptTask even after loading plugin")
    public void rScriptTaskExecutesSuccessfully() throws IOException {
        String buildFileContent = buildFileHeader +
                                  "project.task ('rScriptTask', type: RScriptTask) {" +
                                  "    expression = 'version'" +
                                  "}";
        writeFile(buildFile, buildFileContent);

        BuildResult result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("rScriptTask")
            .withPluginClasspath()
            .build();

        assertTrue(result.getOutput().contains("platform"));
        assertEquals(result.task(":rScriptTask").getOutcome(), SUCCESS);
    }

    /*
     * Utility method for safely writing a string to file
     */
    private void writeFile(File file, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}


package com.umayrh.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import static org.gradle.testkit.runner.TaskOutcome.*;

import spock.lang.Ignore
import spock.lang.Specification

/**
 * Class that tests RScriptTask
 * See also https://docs.gradle.org/current/userguide/test_kit.html
 */
class RScriptTaskTest extends Specification {
    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File buildFile;

    def setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
        buildFile << """
            plugins {
                id 'com.umayrh.rplugin'
            }
        """
    }

    def "Can successfully add a task of type RScriptTask to a project"() {
        Project project = ProjectBuilder.builder().build()

        when:
        def task = project.task('rScriptTask', type: RScriptTask)

        then:
        task instanceof RScriptTask
    }

    @Ignore("Not ready - somehow fails to find RScriptTask even after loading plugin")
    def "Can successfully execute a task of type RScriptTask"() {
        buildFile << """
            project.task ('rScriptTask', type: RScriptTask) {
                expression = 'version'
            }
        """

        when:
        def result = GradleRunner.create()
            .withDebug(true)
            .withProjectDir(testProjectDir.root)
            .withArguments("rScriptTask")
            .withPluginClasspath()
            .build()

        then:
        result.getOutput().contains("platform")
        result.task(":rScriptTask").getOutcome() == SUCCESS
    }
}


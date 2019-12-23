package com.umayrh.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder
import spock.lang.Shared;

import static org.gradle.testkit.runner.TaskOutcome.*;

import spock.lang.Specification

/**
 * Class that tests RScriptTask
 * See also https://docs.gradle.org/current/userguide/test_kit.html
 */
class RScriptTaskTest extends Specification {
    @Rule TemporaryFolder testProjectDir = new TemporaryFolder();
    File buildFile;

    @Before
    def beforeMethod() throws Exception {
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

    def "Can successfully execute a task of type RScriptTask"() {
        // Custom tasks class names must be fully qualified for detection
        buildFile << """
            project.task ('rScriptTask', type: com.umayrh.gradle.RScriptTask) {
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
        result.getOutput().contains("language       R")
        result.task(":rScriptTask").getOutcome() == SUCCESS
    }

    def "Can successfully execute RScriptPackratCleanTask"() {
        buildFile << """
            project.task ('rScriptCleanTask', type: com.umayrh.gradle.RScriptPackratCleanTask) {
            }
        """

        String packratBaseDir = "packrat"
        def packratDirs = [ "lib", "lib-R", "lib-ext", "src" ]
        def packratFiles = []

        packratDirs.each {
            packratFiles.add(testProjectDir.newFolder(packratBaseDir, "${it}"))
        }

        when:
        def result = GradleRunner.create()
            .withDebug(true)
            .withProjectDir(testProjectDir.root)
            .withArguments("rScriptCleanTask")
            .withPluginClasspath()
            .build()

        then:
        result.task(":rScriptCleanTask").getOutcome() == SUCCESS
    }
}


package com.umayrh.gradle

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradle.api.DefaultTask
import static org.junit.Assert.*

class RPluginTest {
    @Test
    public void rPluginAddsTasksToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.umayrh.rplugin'

        assertTrue(project.tasks.installDeps instanceof RScriptInstallTask)
        assertTrue(project.tasks.setupCreate instanceof RScriptSetupTask)
        assertTrue(project.tasks.setupPackrat instanceof RScriptSetupPackratTask)
        assertTrue(project.tasks.setup instanceof DefaultTask)
        assertTrue(project.tasks.document instanceof RScriptDocumentTask)
        assertTrue(project.tasks.test instanceof RScriptTestTask)
        assertTrue(project.tasks.packratRestore instanceof RScriptPackratRestoreTask)
        assertTrue(project.tasks.packratClean instanceof RScriptPackratCleanTask)
    }
}

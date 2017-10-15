package com.umayrh.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskAction

/**
 * Main plugin class containing various tasks for setting up, documenting, and testing R packages
 */
class RPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.apply(plugin: BasePlugin.class)

        /*
         * Note that Gradle expects an evaluation order: while tasks can be created
         * during its configuration phase, they can't be referenced till after creation
         */

        // Tasks for setting up R project
        target.task('installDeps', type: RScriptInstallTask)
        target.task('setupCreate', type: RScriptSetupTask, dependsOn: target.tasks.installDeps)
        target.task('setupPackrat', type: RScriptSetupPackratTask, dependsOn: target.tasks.setupCreate)
        target.task('setup', dependsOn: target.tasks.setupPackrat) { 
            group = "R packaging"
            description = "Sets up a skeleton R package (warning: non-idempotent task)"
        }

        // Task for adding docs
        target.task('document', type: RScriptDocumentTask) { group = "R packaging" }

        // Task for running tests
        target.task('test', type: RScriptTestTask, dependsOn: target.tasks.document) { group = "R packaging" }

        // Task for restoring packages managed by Packrat
        target.task('packratRestore', type: RScriptPackratRestoreTask) { group = "R packaging" }

        // Task for cleaning packages (compiled and source) managed by Packrat
        target.task('packratClean', type: RScriptPackratCleanTask) { group = "R packaging" }

        // TODO: add packaging task
    }
}

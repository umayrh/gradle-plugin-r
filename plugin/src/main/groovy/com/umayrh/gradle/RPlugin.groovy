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
        target.task('installDeps', type: RScriptInstallTask)  {
            group = "R setup, build and packaging"
            description = "Installs common packaging dependencies"
        }

        target.task('setupCreate', type: RScriptSetupTask, dependsOn: target.tasks.installDeps)  {
            group = "R setup, build and packaging"
            description = "Ensures that pre-conditions for package setup are satisfied."
        }

        target.task('setupPackrat', type: RScriptSetupPackratTask, dependsOn: target.tasks.setupCreate)  {
            group = "R setup, build and packaging"
            description = "Sets up a skeleton R package using Renv (warning: non-idempotent task)."
        }

        target.task('setupRenv', type: RScriptSetupRenvTask, dependsOn: target.tasks.setupCreate)  {
            group = "R setup, build and packaging"
            description = "Sets up a skeleton R package using Renv (warning: non-idempotent task)"
        }

        target.task('setup', dependsOn: target.tasks.setupRenv) {
            group = "R setup, build and packaging"
            description = "Sets up a skeleton R package (warning: non-idempotent task)."
        }

        // Task for restoring packages managed by Packrat
        target.task('packratRestore', type: RScriptPackratRestoreTask, dependsOn: target.tasks.installDeps) {
            group = "R setup, build and packaging"
        }

        // Task for restoring packages managed by Renv
        target.task('renvRestore', type: RScriptRenvRestoreTask, dependsOn: target.tasks.installDeps) {
            group = "R setup, build and packaging"
        }

        target.task('restore', dependsOn: target.tasks.renvRestore) {
            group = "R setup, build and packaging"
            description = "Restores all dependencies for a package."
        }

        // Task for cleaning packages (compiled and source) managed by Packrat
        target.task('packratClean', type: RScriptPackratCleanTask)  {
            group = "R setup, build and packaging"
        }

        // Task for cleaning packages (compiled and source) managed by Renv
        target.task('renvClean', type: RScriptRenvCleanTask)  {
            group = "R setup, build and packaging"
        }

        // Task for cleaning packages
        target.task('packageClean', dependsOn: target.tasks.renvClean)  {
            group = "R setup, build and packaging"
        }

        // Task for adding docs
        target.task('document', type: RScriptDocumentTask, dependsOn: target.tasks.renvRestore)  {
            group = "R setup, build and packaging"
        }

        // Task for running tests
        target.task('test', type: RScriptTestTask, dependsOn: target.tasks.document)  {
            group = "R setup, build and packaging"
        }

        // Task for check package
        target.task('packageCheck', type: RScriptCheckTask, dependsOn: target.tasks.restore)  {
            group = "R setup, build and packaging"
        }

        // Task for running tests
        target.task('packageBuild', type: RScriptBuildTask, dependsOn: target.tasks.test)  {
            group = "R setup, build and packaging"
        }
    }
}

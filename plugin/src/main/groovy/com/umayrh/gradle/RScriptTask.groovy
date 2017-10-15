package com.umayrh.gradle

import org.gradle.api.tasks.Exec
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * A DefaultTask that executes Rscript for given expressions
 * TODO: All classes in here should be referencing a specific project dir
 *      instead of the vague current dir. Maybe expect settings.gradle?
 */
class RScriptTask extends DefaultTask {
    String description = "Runs Rscript against given expression"
    @Input
    String expression = null

    @TaskAction
    def exec() {
        project.exec {
            if (expression == null) {
                 throw new GradleException("Must specify an Rscript expression")
            }
            // commandLine is part of Exec task
            commandLine 'Rscript', '-e', expression
        }
    }
}

/*
 * TODO: allow configuring repo
 * TODO: consider an install task per package, connected using GradleBuild task
 */
class RScriptInstallTask extends RScriptTask {
    String description = "Installs R packaging dependencies: devtools, roxygen, testthat, packrat"
    String expression = "if (!require('devtools')) install.packages('devtools', repo='http://cran.rstudio.com'); " +
                        "if (!require('roxygen')) devtools::install_github('klutometis/roxygen'); " +
                        "if (!require('testthat')) install.packages('testthat', repo='http://cran.rstudio.com'); " +
                        "if (!require('packrat')) install.packages('packrat', repo='http://cran.rstudio.com');"
}

class RScriptCreateTask extends RScriptTask {
    String description = "Creates a basic R package"
    String expression = "devtools::create('.')"
}

class RScriptSetupTestTask extends RScriptTask {
    String description = "Sets up testing for R package"
    String expression = "devtools::use_testthat()"
}

class RScriptSetupPackratTask extends RScriptTask {
    String description = "Sets up package management for R package"
    String expression = "packrat::init('.')"
}

class SetupGitignoreTask extends DefaultTask {
    String description = "Creates a .gitignore for R package"

    @TaskAction
    def exec() {
        new File(".gitignore").withWriter { out ->
            out.writeLine(".Rproj.user\n.Rhistory\n.RData\npackrat/lib*/\npackrat/src/")
        }
    }
}

class RScriptDocumentTask extends RScriptTask {
    String description = "Creates documentation for R package"
    String expression = "devtools::document()"
}

class RScriptTestTask extends RScriptTask {
    String description = "Runs test for an R package"
    String expression = "devtools::test()"
}

class RScriptPackratRestoreTask extends RScriptTask {
    String description = "Restores packages managed by Packrat for R package"
    String expression = "packrat::restore(overwrite.dirty=TRUE)"
}

class RScriptPackratCleanTask extends Delete {
    String description = "Removes packages (compiled and sources) managed by Packrat for R package"
    Set<Object> delete = [ 'packrat/src', 'packrat/lib', 'packrat/lib-R', 'packrat/lib-ext' ]
}

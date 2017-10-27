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
    @Input
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
    @Input
    String description = "Installs R packaging dependencies: devtools, usethis, roxygen, testthat, packrat"
    String expression = [
                        "if (!'devtools' %in% installed.packages()) install.packages('devtools', repo='http://cran.rstudio.com')",
                        "if (!'usethis' %in% installed.packages()) devtools::install_github('r-lib/usethis')",
                        "if (!'roxygen' %in% installed.packages()) devtools::install_github('klutometis/roxygen')",
                        "if (!'testthat' %in% installed.packages()) install.packages('testthat', repo='http://cran.rstudio.com')",
                        "if (!'packrat' %in% installed.packages()) install.packages('packrat', repo='http://cran.rstudio.com')"
                        ].join("; ")
}

/*
 * Setups up R package in the current directory.
 * It also adds .gitignore and README.md files, and sets up roxygen and testthat
 * See https://github.com/r-lib/usethis for more options
 * TODO: make this an idempotent task
 */
class RScriptSetupTask extends RScriptTask {
    @Input
    String description = "Creates a basic R package"
    @Input
    String pkgName = System.getProperty("user.dir")
    String expression = [
                        "usethis::create_package('${pkgName}')",
                        "usethis::use_roxygen_md()",
                        "usethis::use_readme_md()",
                        "usethis::use_test('todo')",
                        "usethis::use_git_ignore(c('.Rproj.user','.Rhistory','.RData','packrat/lib*','packrat/src')); "
                        ].join("; ")
}

/*
 * A task that sets up Packrat for an R package
 */
class RScriptSetupPackratTask extends RScriptTask {
    @Input
    String description = "Sets up package management for R package"
    String expression = "packrat::init('.')"
}

/*
 * A task that create roxygen documents
 */
class RScriptDocumentTask extends RScriptTask {
    @Input
    String description = "Creates documentation for R package"
    String expression = "devtools::document()"
}

/*
 * A task that runs testthat tests
 */
class RScriptTestTask extends RScriptTask {
    @Input
    String description = "Runs test for an R package"
    String expression = "devtools::test()"
}

/*
 * A task that retores a package's stated dependencies using Packrat
 */
class RScriptPackratRestoreTask extends RScriptTask {
    @Input
    String description = "Restores packages managed by Packrat for R package"
    String expression = "packrat::restore(overwrite.dirty=TRUE)"
}

/*
 * A task that removes all of a package's dependencies
 */
class RScriptPackratCleanTask extends Delete {
    @Input
    String description = "Removes packages (compiled and sources) managed by Packrat for R package"
    Set<Object> delete = [ 'packrat/src', 'packrat/lib', 'packrat/lib-R', 'packrat/lib-ext' ]
}

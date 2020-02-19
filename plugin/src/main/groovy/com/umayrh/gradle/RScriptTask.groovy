package com.umayrh.gradle

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
            def env = System.getenv()['R_HOME']
            if (env == null) {
                // commandLine is part of Exec task
                commandLine 'Rscript', '-e', expression
            }else{
            // commandLine is part of Exec task
                commandLine env +'/bin/Rscript', '-e', expression
            }
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
                        "if (!'git2r' %in% installed.packages()) install.packages('git2r', repo='http://cran.rstudio.com')",
                        "if (!'devtools' %in% installed.packages()) install.packages('devtools', repo='http://cran.rstudio.com')",
                        "if (!'usethis' %in% installed.packages()) install.packages('usethis', repo='http://cran.rstudio.com')",
                        "if (!'roxygen2' %in% installed.packages()) install.packages('roxygen2', repo='http://cran.rstudio.com')",
                        "if (!'testthat' %in% installed.packages()) install.packages('testthat', repo='http://cran.rstudio.com')",
                        "if (!'packrat' %in% installed.packages()) install.packages('packrat', repo='http://cran.rstudio.com')",
                        "if (!'renv' %in% installed.packages()) install.packages('renv', repo='http://cran.rstudio.com')"
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
                        "usethis::use_git_ignore(c('.Rproj.user','.Rhistory','.RData','packrat/lib*','packrat/src','renv/library')); "
                        ].join("; ")
}

/*
 * A task that sets up Packrat for an R package
 */
class RScriptSetupPackratTask extends RScriptTask {
    @Input
    String description = "Sets up packrat-based package management for R package"
    String expression = "packrat::init('.')"
}

/*
 * A task that sets up Renv for an R package
 */
class RScriptSetupRenvTask extends RScriptTask {
    @Input
    String description = "Sets up Renv-based package management for R package"
    String expression = "renv::init(bare = TRUE)"
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
 * A task that runs devtools package check
 */
class RScriptCheckTask extends RScriptTask {
    @Input
    String description = "Runs checks for an R package"
    String expression = "devtools::check()"
}

/*
 * A task that builds an R package into a tarball
 */
class RScriptBuildTask extends RScriptTask {
    @Input
    String description = "Builds an R package into a tarball"
    String expression = "devtools::build()"
}


/*
 * A task that restores a package's stated dependencies using Packrat
 */
class RScriptPackratRestoreTask extends RScriptTask {
    @Input
    String description = "Restores packages managed by Packrat for R package"
    String expression = "packrat::restore(overwrite.dirty=TRUE)"
}

/*
 * A task that restores a package's stated dependencies using Renv
 */
class RScriptRenvRestoreTask extends RScriptTask {
    @Input
    String description = "Restores packages managed by Renv for R package"
    String expression = "renv::restore()"
}

/*
 * A task that removes all of a package's Packrat dependencies
 */
class RScriptPackratCleanTask extends Delete {
    @Input
    String description = "Removes packages (compiled and sources) managed by Packrat for an R package"
    Set<Object> delete = ["packrat/src", "packrat/lib", "packrat/lib-R", "packrat/lib-ext"]

    @TaskAction
    def exec() {
        project.delete(delete)
    }
}

/*
 * A task that removes all of a package's Renv dependencies
 */
class RScriptRenvCleanTask extends Delete {
    @Input
    String description = "Removes packages (compiled and sources) managed by Renv for an R package"
    Set<Object> delete = ["renv/library"]

    @TaskAction
    def exec() {
        project.delete(delete)
    }
}

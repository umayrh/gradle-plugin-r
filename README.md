# gradle-plugin-r

[![Build Status](https://travis-ci.org/umayrh/gradle-plugin-r.svg?branch=master)](https://travis-ci.org/umayrh/gradle-plugin-r)

A Gradle plugin for R projects.

This plugin helps create packages that are structured using [devtools](https://github.com/hadley/devtools) and 
[usethis](https://github.com/r-lib/usethis), documented using [roxygen2](https://github.com/klutometis/roxygen), 
with dependencies managed using [Renv](https://rstudio.github.io/renv/) (or optionally [Packrat](https://rstudio.github.io/packrat/)), 
and unit-tested using [testthat](https://github.com/hadley/testthat).

J. Olson's [R plugin](https://github.com/jamiefolson/gradle-plugin-r) inspired this one. One important difference is the use of Packrat,
the other is, perhaps, a terser implementation.

## Overview

The development workflows for R project are (`gradle tasks`):

```
R setup, build and packaging tasks
----------------------------------
document - Creates documentation for R package
installDeps - Installs common packaging dependencies
packageBuild - Builds an R package into a tarball
packageCheck - Runs checks for an R package
packageClean
packratClean - Removes packages (compiled and sources) managed by Packrat for an R package
packratRestore - Restores packages managed by Packrat for R package
renvClean - Removes packages (compiled and sources) managed by Renv for an R package
renvRestore - Restores packages managed by Renv for R package
restore - Restores all dependencies for a package.
setup - Sets up a skeleton R package (warning: non-idempotent task).
setupCreate - Ensures that pre-conditions for package setup are satisfied.
setupPackrat - Sets up a skeleton R package using Renv (warning: non-idempotent task).
setupRenv - Sets up a skeleton R package using Renv (warning: non-idempotent task)
test - Runs test for an R package
```

This project contains two directories:

* `plugin`, which contain all the Groovy code, properties and build files
* `testRProject`, a sample project with build file that uses this plugin

## How To

* Create a directory (this will be your R package)
* Create a build.gradle file, which applies this plugin

```
buildscript {
  repositories {
      maven {
          url "https://plugins.gradle.org/m2/"
      }
  }
  dependencies {
      classpath "gradle.plugin.com.umayrh:gradle-plugin-r:0.3.0"
  }
}

apply plugin: "com.umayrh.rplugin"
```

It should also be possible to use Gradle's new plugin mechanism:

````
plugins {
    id "com.umayrh.rplugin" version "0.3.0"
}
````

## Plugin Development Notes

If you add to or modify the code under `plugin` directory, then you can
recompile, re-test, and re-package by running (inside `plugin` dir)

`gradle build uploadArchives`

To test the plugin itself, you can run gradle inside `testRProject` dir e.g.

`gradle document`

## References

* [Implementing Gradle plugins](https://guides.gradle.org/implementing-gradle-plugins/)
* [Custom plugins](https://docs.gradle.org/current/userguide/custom_plugins.html)
* [Custom tasks](https://docs.gradle.org/current/userguide/custom_tasks.html)
* [Testing Gradle plugins](https://guides.gradle.org/testing-gradle-plugins/)
* [Gradle TestKit](https://docs.gradle.org/current/userguide/test_kit.html)
* [Publish Plugin](https://plugins.gradle.org/docs/publish-plugin)

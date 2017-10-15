# gradle-plugin-r

A Gradle plugin for R projects. 

This plugin helps create packages that are structured using [devtools](https://github.com/hadley/devtools), documented using
[roxygen2](https://github.com/klutometis/roxygen), with dependencies managed using [Packrat](https://rstudio.github.io/packrat/),
and unit-tested using [testthat](https://github.com/hadley/testthat).

J. Olson's [R plugin](https://github.com/jamiefolson/gradle-plugin-r) inspired this one. One important difference is the use of Packrat,
the other that this plugin achieves the same ends with a terser implementation.

## Overview

The development workflows for R project are (`gradle tasks`):

```
R packaging tasks
-----------------
document - Creates documentation for R package
packratClean - Removes packages (compiled and sources) managed by Packrat for R package
packratRestore - Restores packages managed by Packrat for R package
setup - Sets up a skeleton R package (warning: non-idempotent task)
test - Runs test for an R package
```

This project contains two directories:

* `plugin`, which contain all the Groovy code, properties and build files
* `testRProject`, a sample project with build file that uses this plugin

## How To

* Create a directory (this will be your R package)
* Create a build.gradle file, which applies this plugin

## Plugin Development Notes

If you add to or modify the code under `plugin` dirctory, then should can
recompile and re-package by running (inside `plugin` dir)

`gradle build uploadArchives`

To test the plugin itself, you can run gradle inside `testRProject` dir

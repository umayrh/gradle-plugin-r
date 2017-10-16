# gradle-plugin-r

A Gradle plugin for R projects. See also its Github [repo](https://github.com/umayrh/gradle-plugin-r).

This plugin helps create packages that are structured using [devtools](https://github.com/hadley/devtools) and 
[usethis](https://github.com/r-lib/usethis), documented using [roxygen2](https://github.com/klutometis/roxygen), 
with dependencies managed using [Packrat](https://rstudio.github.io/packrat/), and unit-tested using 
[testthat](https://github.com/hadley/testthat).

J. Olson's [R plugin](https://github.com/jamiefolson/gradle-plugin-r) inspired this one. One important difference is the use of Packrat,
the other is, perhaps, a terser implementation.

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

```
buildscript {
  repositories {
      maven {
          url "https://plugins.gradle.org/m2/"
      }
  }
  dependencies {
      classpath "gradle.plugin.com.umayrh:gradle-plugin-r:0.2"
  }
}

apply plugin: "com.umayrh.rplugin"
```

It should also be possible to use Gradle's new plugin mechanism:

````
plugins {
    id "com.umayrh.rplugin" version "0.2"
}
````

## Plugin Development Notes

If you add to or modify the code under `plugin` dirctory, then you can
recompile, re-test, and re-package by running (inside `plugin` dir)

`gradle build uploadArchives`

To test the plugin itself, you can run gradle inside `testRProject` dir e.g.

`gradle document`

## References

* [Implementing Gradle plugins](https://guides.gradle.org/implementing-gradle-plugins/)
* [Custom plugins](https://docs.gradle.org/current/userguide/custom_plugins.html)
* [Custom tasks](https://docs.gradle.org/current/userguide/custom_tasks.html)
* [Gradle TestKit](https://docs.gradle.org/current/userguide/test_kit.html)
* [Publish Plugin](https://plugins.gradle.org/docs/publish-plugin)

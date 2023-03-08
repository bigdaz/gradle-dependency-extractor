pluginManagement {
    includeBuild("build-logic")
    includeBuild("../..")
}

plugins {
    id("org.gradle.dependencyextractor.settings")
}

rootProject.name = "java-app"

includeBuild("list")
include("app", "utilities")

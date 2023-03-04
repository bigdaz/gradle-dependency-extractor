pluginManagement {
    includeBuild("../..")
}

plugins {
    id("org.gradle.dependencyextractor.settings")
}

rootProject.name = "java-app"

include("app", "list", "utilities")

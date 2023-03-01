plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

gradlePlugin {
    val projectExtractor by plugins.creating {
        id = "org.gradle.dependencyextractor.project"
        implementationClass = "org.gradle.dependencyextractor.DependencyExtractorProjectPlugin"
    }
    val settingsExtractor by plugins.creating {
        id = "org.gradle.dependencyextractor.settings"
        implementationClass = "org.gradle.dependencyextractor.DependencyExtractorSettingsPlugin"
    }
}

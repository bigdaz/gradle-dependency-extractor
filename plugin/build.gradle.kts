plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")
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

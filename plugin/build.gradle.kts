plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
}

gradlePlugin {
    val settingsExtractor by plugins.creating {
        id = "org.gradle.dependencyextractor.settings"
        implementationClass = "org.gradle.dependencyextractor.DependencyExtractorSettingsPlugin"
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test framework
            useKotlinTest()
        }
    }
}

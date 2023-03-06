package org.gradle.dependencyextractor

import io.kotest.matchers.file.shouldBeAFile
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.string.shouldNotBeEmpty
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test

class DependencyExtractorTest {
    @Test
    fun `java-app sample`() {
        val sampleCopy = File("/Users/daz/dev/github/dependency-extractor-plugin/build/java-app")
        sampleCopy.deleteRecursively()

        val sampleBuild = File("/Users/daz/dev/github/dependency-extractor-plugin/sample-projects/java-app")
        sampleBuild.copyRecursively(sampleCopy)

        val dependencyGraphFile = File("${sampleCopy}/.gradle/dependency-graph.json")
        dependencyGraphFile.shouldNotExist()

        // Run the build
        val runner = GradleRunner.create()
            .forwardOutput()
            .withArguments("help")
            .withProjectDir(sampleCopy)
        val result = runner.build()

        // Verify the output
        result.output.shouldNotBeEmpty()

        // Verify the generated graph
        dependencyGraphFile.shouldBeAFile()
        val graph = dependencyGraphFile.readText()
        graph.shouldNotBeEmpty()
    }
}

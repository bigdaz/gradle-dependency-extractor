package org.gradle.dependencyextractor

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DependencyExtractorTest {
    @Test
    fun `java-app sample`() {
        val sampleCopy = File("/Users/daz/dev/github/dependency-extractor-plugin/build/java-app")
        sampleCopy.deleteRecursively()

        val sampleBuild = File("/Users/daz/dev/github/dependency-extractor-plugin/sample-projects/java-app")
        sampleBuild.copyRecursively(sampleCopy)

        val outputFile = File("${sampleCopy}/.gradle/dependency-graph.json")

        assertFalse(outputFile.exists())

        // Run the build
        val runner = GradleRunner.create()
            .forwardOutput()
            .withArguments("help")
            .withProjectDir(sampleCopy)
        val result = runner.build()

        // Verify the result
        assertTrue(result.output.isNotEmpty())
        assertTrue(outputFile.exists())
    }
}

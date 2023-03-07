package org.gradle.dependencyextractor

import io.kotest.matchers.file.shouldBeAFile
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.*

class DependencyExtractorSamplesTest {
    companion object {
        @JvmStatic
        fun sampleExecutions(): Stream<Arguments> {
            val args = mutableListOf<Arguments>()
            val samplesDir = Path.of("../sample-projects")
            samplesDir.forEachDirectoryEntry() { sampleDir ->
                sampleDir.resolve("dependency-graphs")
                    .takeIf { it.isDirectory() }
                    ?.forEachDirectoryEntry("*.json") { graphJson ->
                        args.add(Arguments.of(sampleDir.toFile(), graphJson.nameWithoutExtension))
                    }
            }
            return args.stream()
        }
    }
    @ParameterizedTest
    @MethodSource("sampleExecutions")
    fun `check sample execution`(sampleDir: File, taskName: String) {
        val expectedDependencyGraphFile = File("${sampleDir}/dependency-graphs/${taskName}.json")
        expectedDependencyGraphFile.shouldExist()

        val sampleCopy = File("../build/${sampleDir.name}")
        sampleDir.copyRecursively(sampleCopy, overwrite = true)

        // Run the build
        val runner = GradleRunner.create()
            .forwardOutput()
            .withArguments(taskName)
            .withProjectDir(sampleCopy)
        val result = runner.build()
        // Verify the output
        result.output.shouldNotBeEmpty()

        // Verify the generated graph
        val dependencyGraphFile = File("${sampleCopy}/.gradle/dependency-graph.json")
        dependencyGraphFile.shouldBeAFile()
        val graph = dependencyGraphFile.readText()
        val expectedGraph = expectedDependencyGraphFile.readText()

        graph.shouldBe(expectedGraph)
    }
}

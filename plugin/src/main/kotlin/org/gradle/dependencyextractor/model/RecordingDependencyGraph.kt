package org.gradle.dependencyextractor.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

class RecordingDependencyGraph(val outputFile: Provider<RegularFile>) : AutoCloseable {
    private val mapper = jacksonObjectMapper().writerWithDefaultPrettyPrinter()

    private val resolvedConfigurations: MutableList<ResolvedConfiguration> = mutableListOf()

    fun recordResolvedConfiguration(resolvedConfiguration: ResolvedConfiguration) {
        resolvedConfigurations.add(resolvedConfiguration)
    }

    override fun close() {
        val out = outputFile.get().asFile
        mapper.writeValue(out, resolvedConfigurations)
    }
}

package org.gradle.dependencyextractor.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class RecordingDependencyGraph: AutoCloseable {
    private val mapper = jacksonObjectMapper().writerWithDefaultPrettyPrinter()

    private val resolvedConfigurations: MutableList<ResolvedConfiguration> = mutableListOf()

    fun recordResolvedConfiguration(resolvedConfiguration: ResolvedConfiguration) {
        resolvedConfigurations.add(resolvedConfiguration)
    }

    override fun close() {
        mapper.writeValue(System.out, resolvedConfigurations)
    }
}

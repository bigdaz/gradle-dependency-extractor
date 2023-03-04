package org.gradle.dependencyextractor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedComponentResult
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.internal.artifacts.configurations.ResolveConfigurationDependenciesBuildOperationType
import org.gradle.dependencyextractor.model.Dependency
import org.gradle.internal.operations.*
import java.net.URI

class DependencyExtractorService :
    BuildOperationListener,
    AutoCloseable {

    init {
        println("Creating: DependencyExtractorService")
    }

    override fun started(buildOperation: BuildOperationDescriptor, startEvent: OperationStartEvent) {
        // This method will never be called when registered in a `BuildServiceRegistry` (ie. Gradle 6.1 & higher)
        // No-op
        println("started: ${buildOperation.id}")
    }

    override fun progress(operationIdentifier: OperationIdentifier, progressEvent: OperationProgressEvent) {
        // This method will never be called when registered in a `BuildServiceRegistry` (ie. Gradle 6.1 & higher)
        // No-op
        println("progress: $operationIdentifier")
    }

    override fun finished(buildOperation: BuildOperationDescriptor, finishEvent: OperationFinishEvent) {
        handleBuildOperationType<
            ResolveConfigurationDependenciesBuildOperationType.Details,
            ResolveConfigurationDependenciesBuildOperationType.Result,
            ResolveConfigurationDependenciesBuildOperationType
            >(buildOperation, finishEvent) { details, result -> extractDependencies(details, result) }
    }

    private fun extractDependencies(
        details: ResolveConfigurationDependenciesBuildOperationType.Details,
        result: ResolveConfigurationDependenciesBuildOperationType.Result
    ) {
        val repositoryLookup = RepositoryUrlLookup(details, result)
        val rootComponent = result.rootComponent

        println()
        println("RESOLVED: ${details.buildPath} | ${rootComponent.id} | ${details.configurationName}")
        val resolvedComponents = linkedMapOf<ComponentIdentifier, Dependency>()
        walkResolvedComponentResult(rootComponent, repositoryLookup, "", resolvedComponents)

        val mapper = jacksonObjectMapper()
        resolvedComponents.values.forEach {
            println(mapper.writeValueAsString(it))
        }
    }

    private fun walkResolvedComponentResult(
        component: ResolvedComponentResult,
        repositoryLookup: RepositoryUrlLookup,
        prefix: String,
        seenComponents: MutableMap<ComponentIdentifier, Dependency>
    ) {
        if (seenComponents.containsKey(component.id)) {
            return
        }
        val repositoryUrl = repositoryLookup.doLookup(component)
        val resolvedDependencies = component.dependencies.filterIsInstance<ResolvedDependencyResult>().map { it.selected }

        seenComponents[component.id] = Dependency(componentId(component), repositoryUrl, resolvedDependencies.map { componentId(it) })

        resolvedDependencies
            .forEach {
                walkResolvedComponentResult(it, repositoryLookup, "$prefix-", seenComponents)
            }
    }

    private fun componentId(component: ResolvedComponentResult): String {
        val componentId = component.id
        if (componentId is ProjectComponentIdentifier) {
            return componentId.projectPath
        }
        return componentId.displayName
    }

    private class RepositoryUrlLookup(
        private val details: ResolveConfigurationDependenciesBuildOperationType.Details,
        private val result: ResolveConfigurationDependenciesBuildOperationType.Result
    ) {

        private fun getRepositoryUrlForId(id: String): String? {
            return details
                .repositories
                ?.find { it.id == id }
                ?.properties
                ?.let { it["URL"] as? URI }
                ?.toURL()
                ?.toString()
        }

        fun doLookup(component: ResolvedComponentResult): String? {
            // Get the repository id from the result
            val repositoryId = result.getRepositoryId(component)
            return repositoryId?.let { getRepositoryUrlForId(it) }
        }
    }

    override fun close() {
    }
}

private inline fun <reified D, reified R, BT : BuildOperationType<D, R>> handleBuildOperationType(
    buildOperation: BuildOperationDescriptor,
    finishEvent: OperationFinishEvent,
    handler: (details: D, result: R) -> Unit
) {
    val details: D? = buildOperation.details.let {
        if (it is D) it else null
    }
    val result: R? = finishEvent.result.let {
        if (it is R) it else null
    }
    if (details == null && result == null) {
        return
    } else if (details == null || result == null) {
        throw IllegalStateException("buildOperation.details & finishedEvent.result were unexpected types")
    }
    handler(details, result)
}

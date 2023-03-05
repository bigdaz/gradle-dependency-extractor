package org.gradle.dependencyextractor

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.internal.build.event.BuildEventListenerRegistryInternal

class DependencyExtractorSettingsPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {
        println("Applied dependency extractor plugin to Settings '${settings.rootProject.name}'")
        val gradle = settings.gradle as GradleInternal

        val extractorServiceProvider = gradle.sharedServices.registerIfAbsent(
            "dependencyExtractorService",
            DependencyExtractorService::class.java,
            {})
        gradle.services.get(BuildEventListenerRegistryInternal::class.java).onOperationCompletion(extractorServiceProvider)
    }
}

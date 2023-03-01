package org.gradle.dependencyextractor

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class DependencyExtractorSettingsPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {
        println("Applied dependency extractor plugin to Settings '${settings.rootProject.name}'")
        settings.gradle.beforeProject {
            it.plugins.apply(DependencyExtractorProjectPlugin::class.java)
        }
    }
}

package org.gradle.dependencyextractor

import org.gradle.api.Project
import org.gradle.api.Plugin

class DependencyExtractorProjectPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        println("Applied dependency extractor plugin to Project '${project.name}'")
    }
}

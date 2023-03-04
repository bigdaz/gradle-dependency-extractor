package org.gradle.dependencyextractor

import org.gradle.api.artifacts.component.ComponentIdentifier

data class ComponentDependency(val id: ComponentIdentifier, val repositoryUrl: String?, val dependencies: List<ComponentIdentifier>)

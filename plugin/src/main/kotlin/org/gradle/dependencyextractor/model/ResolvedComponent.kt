package org.gradle.dependencyextractor.model

data class ResolvedComponent(val id: String, val repositoryUrl: String?, val dependencies: List<String>)

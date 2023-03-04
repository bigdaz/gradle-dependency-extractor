package org.gradle.dependencyextractor.model

data class Dependency(val id: String, val repositoryUrl: String?, val dependencies: List<String>)

package org.gradle.dependencyextractor.model

data class ResolvedConfiguration(val rootId: String, val name: String, val components: MutableList<ResolvedComponent> = mutableListOf()) {
    fun hasComponent(componentId: String): Boolean {
        return components.map { it.id }.contains(componentId)
    }
}


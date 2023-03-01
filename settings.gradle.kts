plugins {
    id("com.gradle.enterprise") version("3.12.4")
}

gradleEnterprise {
    server = "https://ge.solutions-team.gradle.com"
    buildScan {
        publishAlways()
    }
}

rootProject.name = "dependency-extractor-plugin"
include("plugin")

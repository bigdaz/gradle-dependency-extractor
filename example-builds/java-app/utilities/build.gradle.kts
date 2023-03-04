plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":list"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

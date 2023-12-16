plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.13.0"
}

group = "com.coderzf1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.simpleframework:simple-xml:2.7.1")
}


// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1.1.26")
    type.set("AI") // Target IDE Platform

    plugins.set(listOf("android"))
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }


    runIde { ideDir.set( file("C:\\Program Files\\Android\\Android Studio2") )}


}

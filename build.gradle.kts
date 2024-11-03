plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.1.0"
    id("org.jetbrains.kotlin.jvm") version "2.1.0-Beta2"
    //id("org.jetbrains.intellij") version "1.17.4"
}



group = "com.coderzf1"
version = "4.0-SNAPSHOT" //Reminder, Increase Version Number Here for each update

repositories {
    mavenCentral()
    intellijPlatform{
        defaultRepositories()
    }
}
dependencies {
    implementation("org.simpleframework:simple-xml:2.7.1")
    intellijPlatform{
        androidStudio("2024.2.2.9")
        bundledPlugin("org.jetbrains.android")
        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }
}


// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
// Android Studio Releases for IntelliJ Plugins: https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html
//
//intellij {
//    version.set("2024.2.2.9")
//    type.set("AI") // Target IDE Platform
//    plugins.set(listOf("android","org.jetbrains.android"))
//}



tasks {
    buildSearchableOptions {
        enabled = false
    }
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }

    patchPluginXml{
        sinceBuild.set("231")
        untilBuild.set("243.*")
    }

    //runIde { ideDir.set( file("C:\\Program Files\\Android\\Android Studio2") )}

    publishPlugin {
        token.set("perm:d2lsbGlhbXMuY2FybC44Mw==.OTItOTI3MQ==.Lw5wKUtuT0VsbWH36uW7h7GS5bP92l")
    }
}

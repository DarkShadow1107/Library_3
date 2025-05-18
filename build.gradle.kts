import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") // Standard Kotlin JVM plugin
    id("org.jetbrains.compose") // Jetpack Compose plugin
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // Explicit Compose Multiplatform plugin
    kotlin("plugin.serialization") version "2.0.0" // Kotlin serialization plugin
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // Compose Desktop repository
    google() // Repository for other dependencies (might not be strictly needed for pure Desktop)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
}

compose.desktop {
    application {
        mainClass = "project.AppKt" // Entry point for the application
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Library_3"
            packageVersion = "1.0.0"
        }
    }
}
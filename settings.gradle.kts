rootProject.name = "deforged"

pluginManagement {
    repositories {
        gradlePluginPortal()

        maven("https://maven.minecraftforge.net/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}
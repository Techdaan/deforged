import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.UserDevPlugin
import org.gradle.kotlin.dsl.accessors.runtime.extensionOf
import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.MixinGradlePlugin
import org.spongepowered.asm.gradle.plugins.struct.DynamicProperties

buildscript {
    repositories {
        maven("https://files.minecraftforge.net/maven")
    }

    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.77") {
            isChanging = true
        }

        classpath("org.spongepowered:mixingradle:0.7.+")
    }
}

plugins {
    id("java")
}

apply<UserDevPlugin>()
apply<MixinGradlePlugin>()

group = "org.pokecentral"
version = "1.0.0"

configure<UserDevExtension> {
    mappingChannel.set((properties["mcpMappingChannel"] ?: error("mcpMappingsChannel not found")).toString())
    mappingVersion.set((properties["mcpMappingVersion"] ?: error("mcpMappingVersion not found")).toString())
}

repositories {
    mavenCentral()

    ivy {
        name = "Pixelmon"
        url = uri("https://download.nodecdn.net/containers")
        patternLayout {
            artifact("[organisation]/server/release/[revision]/[module]-[revision]-server.jar")
        }
        metadataSources {
            artifact()
        }
    }
}

val mcVersion = properties["modMcVersion"]?.toString() ?: error("modMcVersion not found")
val forgeVersion = properties["forgeVersion"]?.toString() ?: error("forgeVersion not found")

val fg = extensions["fg"] as DependencyManagementExtension

dependencies {
    "minecraft"("net.minecraftforge:forge:$mcVersion-$forgeVersion")

    implementation(fg.deobf("reforged:Pixelmon-1.16.5:9.1.5"))

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.test {
    useJUnitPlatform()
}

configure<MixinExtension> {
    add(sourceSets.main.get(), "mixins.deforged.refmap.json")
    config("mixins.deforged.json")

    val debugProperties = debug as DynamicProperties
    debugProperties.setProperty("verbose", true)
    debugProperties.setProperty("export", true)
}

import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val generatedApiConfigDir = layout.buildDirectory.dir("generated/apiConfig/kotlin")

val localPropertiesProvider = providers.fileContents(
    rootProject.layout.projectDirectory.file("local.properties")
).asText

val baseUrlProvider = localPropertiesProvider.map { content ->
    val props = Properties()
    props.load(content.reader())
    props.getProperty("uniprot.baseUrl").orEmpty()
}.orElse(
    providers.gradleProperty("uniprot.baseUrl")
).orElse(
    providers.environmentVariable("UNIPROT_BASE_URL")
).orElse("")

val generateApiConfig by tasks.registering {
    val outputDir = generatedApiConfigDir
    val baseUrl = baseUrlProvider
    inputs.property("baseUrl", baseUrl)
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile.resolve("org/protein/project/config")
        dir.mkdirs()
        val escapedUrl = baseUrl.get().replace("\"", "\\\"")
        dir.resolve("ApiConfig.kt").writeText(
            """
            package org.protein.project.config

            object ApiConfig {
                const val BASE_URL: String = "$escapedUrl"
            }
            """.trimIndent() + "\n"
        )
    }
}

kotlin {
    jvm()
    js {
        browser()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    android {
       namespace = "org.protein.project.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain {
            kotlin.srcDir(generateApiConfig.map { generatedApiConfigDir })
            dependencies {
                implementation(compose.materialIconsExtended)
                implementation(libs.navigation3.runtime)
                implementation(libs.navigation3.ui)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
            }
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
            implementation(libs.ktor.client.js)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.testRunner)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "org.protein.project.resources"
}

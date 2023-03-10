import Deps.Ktor.clientIos
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin(Plugins.multiplatform)
    id(Plugins.android)
    id(Plugins.kover) version Versions.kover
    id(Plugins.mokoKSwift) version Versions.mokoKSwift
    id(Plugins.mokoResources)
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
    id(Plugins.kermitGradlePlugin) // Kermit "Chisel" plugin for stripping logs
    id(Plugins.sqlDelight)
    id("io.realm.kotlin") version Versions.realm
    kotlin(Plugins.serialization) version Versions.kotlin
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
}

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "ProvbitShared"
            }
        }
    }

    targets.withType<KotlinNativeTarget> {
        compilations.get("main").kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
        compilations.get("main").kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("$buildDir/generated-apis/src/commonMain/kotlin")
            kotlin.srcDir("$buildDir/generated/ksp/commonMain/kotlin")
            dependencies {
                with(Deps.Kotlinx) {
                    implementation(coroutinesCore)
                }
                with(Deps.KermitTheLog) {
                    implementation(kotlin(moduleName))
                    implementation(kermit)
                }
                api(Deps.MokoResources.runtime)
                with(Deps.Ktor) {
                    implementation(clientCore)
                    implementation(clientJson)
                    implementation(clientSerialization)
                }
                with(Deps.Kotlinx) {
                    implementation(coroutinesCore)
                    implementation(coroutinesSerialization)
                }
                implementation(Deps.KotlinInject.runtime)
                with(Deps.Komma) {
                    implementation(fs)
                }
                implementation(Deps.SqlDelight.runtime)
                implementation("io.realm.kotlin:library-base:${Versions.realm}")
                api(Deps.MokoResources.runtime)
                implementation(Deps.SqlDelight.coroutinesExtension)
            }
        }
        val commonTest by getting {
            dependencies {
                with(Deps.TestCommon) {
                    implementation(kotlin(testCommon))
                    implementation(kotlin(testAnnotationsCommon))
                }
                with(Deps.Mockk) {
                    implementation(mockkCommon)
                }
                with(Deps.Kotlinx) {
                    implementation(coroutinesTest)
                }
                with(Deps.Turbine) {
                    implementation(turbine)
                }
            }
        }
        val androidMain by getting {
            kotlin.srcDir("$buildDir/generated-apis/src/androidMain/kotlin")
            dependencies {
                implementation(Deps.SqlDelight.android)
                implementation(Deps.Ktor.clientCio)
            }
        }
        val androidTest by getting {
            dependencies {
                with(Deps.Junit) {
                    implementation(kotlin(testJunit))
                    implementation(junit)
                }
                with(Deps.Mockk) {
                    implementation(mockk)
                }
                with(Deps.Kotlinx) {
                    implementation(coroutinesTest)
                }
            }
        }
        val iosMain by getting {
            dependencies {
                kotlin.srcDir("$buildDir/generated-apis/src/iosMain/kotlin")
                implementation(Deps.SqlDelight.ios)
                implementation(Deps.Ktor.clientIos)
            }
        }
        val iosTest by getting
    }
}

createAndRegisterGenerateTask("conch")

fun createAndRegisterGenerateTask(yml: String) {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
        dependsOn("openApiGenerate_$yml")
    }

    tasks.register<GenerateTask>("openApiGenerate_$yml") {
        generatorName.set("bright")
        inputSpec.set("$rootDir/api/$yml.json")
        outputDir.set("$buildDir/generated-apis/")
        additionalProperties.put("packageName", "ai.bright.provbit.client.$yml")
    }
}

android {
    compileSdk = AndroidSdk.compile
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ai.bright.provbit"
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
    arg("me.tatarka.inject.dumpGraph", "true")
}

dependencies {
    add("kspMetadata", "me.tatarka.inject:kotlin-inject-compiler-ksp:0.4.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspKotlinMetadata") dependsOn("kspKotlinMetadata")
    dependsOn("openApiGenerate_conch")
}

sqldelight {
    database("AppDatabase") {
        packageName = "ai.bright.provbit.demo.adapters.sqlite"
    }
}


/*
The Kermit log-stripping plugin (Chisel) allows for the disabling of certain logs in specific situations.
"Any log call below the configured severity will be removed. So, if you pass Warn, warn, error, and assert
calls remain but info and below are removed. There are some special values: None and All. None is default
(removes nothing). All removes all logging calls."
 Read more: https://github.com/yangblocker/Kermit#kermit-chisel
 */
kermit {
    val releaseBuildKermit: String by project

    if(releaseBuildKermit.toBoolean()) {
        stripBelow = co.touchlab.kermit.gradle.StripSeverity.Info
    }
}

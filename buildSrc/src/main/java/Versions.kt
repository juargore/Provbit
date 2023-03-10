import org.gradle.api.JavaVersion

object Versions {

    const val kotlin = "1.6.10"

    const val kotlinCoroutines = "1.6.0-native-mt"

    const val kotlinSerialization = "1.3.1"

    const val kotlinInject = "0.4.1"

    const val jetpackCompose = "1.2.0-alpha04"

    const val jetpackComposeUiTooling = "1.1.0-beta04"

    const val jetpackComposeActivities = "1.4.0"

    const val navigation = "2.3.5"

    const val jetpackAccompanist = "0.24.2-alpha"

    const val mlKit = "17.0.2"

    const val navigationComposeIntegration = "2.4.0-rc01"

    const val mockk = "1.12.1"

    const val mokoResources = "0.18.0"

    const val junit = "4.13.2"

    const val androidGradleTools = "7.0.3"

    const val kover = "0.4.4"

    const val mokoKSwift = "0.4.0"

    const val androidXCore = "1.7.0"

    const val hiltAndroid = "2.38.1"

    const val androidXAppCompat = "1.4.0"

    const val androidXLifecycle = "2.4.0"

    const val androidMaterial = "1.4.0"

    const val androidXJUnit = "1.1.3"

    const val androidXEspresso = "3.4.0"

    const val androidXCamera = "1.1.0-beta01"
    
    const val turbine = "0.7.0"

    const val kermit = "1.0.0"

    const val fs = "0.1.1"

    const val sqlDelight = "1.5.3"

    const val realm = "0.10.0"

    const val auth0 = "2.6.0"

    const val brightGenerator = "1.0.3"

    const val ktor = "1.6.5"

    const val glide = "4.13.0"
}

object Plugins {
    const val multiplatform = "multiplatform"
    const val android = "com.android.library"
    const val kover = "org.jetbrains.kotlinx.kover"
    const val mokoKSwift = "dev.icerock.moko.kswift"
    const val mokoResources = "dev.icerock.mobile.multiplatform-resources"
    const val kermitGradlePlugin = "co.touchlab.kermit"
    const val sqlDelight = "com.squareup.sqldelight"
    const val serialization = "plugin.serialization"
}

object AndroidSdk {
    const val min = 24
    const val compile = 31
    const val target = compile
}

object Java {
    val version = JavaVersion.VERSION_1_8
}

object Deps {

    object RootBuild {
        const val generatorGradle = "ai.bright.openapi:bright-kmm-generator:${Versions.brightGenerator}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradleTools}"
        const val mokoResources = "dev.icerock.moko:resources-generator:${Versions.mokoResources}"
        const val hiltGradle =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroid}"
        const val kermitGradle =
            "co.touchlab:kermit-gradle-plugin:${Versions.kermit}" // Kermit "Chisel" plugin for stripping logs
        const val sqlDelGradle = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
    }

    object Kotlinx {
        const val coroutinesSerialization =
            "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinSerialization}"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
    }

    object Dagger {
        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hiltAndroid}"
        const val hiltAndroidCompiler =
            "com.google.dagger:hilt-android-compiler:${Versions.hiltAndroid}"
    }

    object Junit {
        const val testJunit = "test-junit"
        const val junit = "junit:junit:${Versions.junit}"
    }

    object Mockk {
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val mockkCommon = "io.mockk:mockk-common:${Versions.mockk}"
    }

    object TestCommon {
        const val testCommon = "test-common"
        const val testAnnotationsCommon = "test-annotations-common"
    }

    object AndroidAuth0 {
        const val auth0 = "com.auth0.android:auth0:${Versions.auth0}"
    }

    object JetpackCompose {
        const val activity =
            "androidx.activity:activity-compose:${Versions.jetpackComposeActivities}"
        const val ui = "androidx.compose.ui:ui:${Versions.jetpackCompose}"
        const val material = "androidx.compose.material:material:${Versions.jetpackCompose}"
        const val preview = "androidx.compose.ui:ui-tooling-preview:${Versions.jetpackComposeUiTooling}"
        const val junit = "androidx.compose.ui:ui-test-junit4:${Versions.jetpackComposeUiTooling}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.jetpackComposeUiTooling}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.androidXLifecycle}"
    }

    object JetpackNavigation {
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val navigationDynamicFeatures = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
        const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigationComposeIntegration}"
    }

    object Accompanist {
        const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:${Versions.jetpackAccompanist}"
        const val permissions ="com.google.accompanist:accompanist-permissions:${Versions.jetpackAccompanist}"
    }

    object MLKit {
        const val barcodeScanning = "com.google.mlkit:barcode-scanning:${Versions.mlKit}"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.androidXCore}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.androidXAppCompat}"
        const val material = "com.google.android.material:material:${Versions.androidMaterial}"
        const val camera = "androidx.camera:camera-camera2:${Versions.androidXCamera}"
        const val cameraView ="androidx.camera:camera-view:${Versions.androidXCamera}"
        const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.androidXCamera}"
    }

    object AndroidXTesting {
        const val junit = "androidx.test.ext:junit:${Versions.androidXJUnit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.androidXEspresso}"
    }

    object MokoResources {
        const val runtime = "dev.icerock.moko:resources:${Versions.mokoResources}"
    }

    object KotlinInject {
        const val runtime = "me.tatarka.inject:kotlin-inject-runtime:${Versions.kotlinInject}"
    }

    object Turbine {
        const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    }

    object KermitTheLog {
        const val moduleName = "stdlib-common"
        const val kermit = "co.touchlab:kermit:${Versions.kermit}"

    }

    object Komma {
        const val fs = "ai.bright.core:fs:${Versions.fs}"
    }

    object SqlDelight {
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val android = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
        const val ios = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
        const val coroutinesExtension = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    }

    object Ktor {
        const val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
        const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
        const val clientCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
        const val clientIos = "io.ktor:ktor-client-ios:${Versions.ktor}"
    }

    object Glide{
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }
}

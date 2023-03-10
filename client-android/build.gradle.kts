plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = AndroidSdk.compile
    defaultConfig {
        applicationId = "ai.bright.provbit.android"
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = 1
        versionName = "0.0.1"
        setManifestPlaceholders(
            mapOf(
                "auth0Domain" to "brightai-core-dev.us.auth0.com",
                "auth0Scheme" to "brightai"
            )
        )
    }

    buildTypes {

        forEach {
            it.buildConfigField("String", "AUTH_CLIENT_ID", "\"${Secrets.Auth0.AUTH_CLIENT_ID}\"")
            it.buildConfigField("String", "AUTH_DOMAIN", "\"${Secrets.Auth0.AUTH_DOMAIN}\"")
            it.buildConfigField("String", "AUTH_CALLBACK_SCHEME", "\"${Secrets.Auth0.AUTH_CALLBACK_SCHEME}\"")

        }
        getByName("release") {
            isMinifyEnabled = false
        }

    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.jetpackCompose
    }

    compileOptions {
        sourceCompatibility = Java.version
        targetCompatibility = Java.version
    }

    kotlinOptions {
        jvmTarget = Java.version.toString()
    }

    packagingOptions {
        jniLibs {
            pickFirsts += mutableSetOf(
                "**/libc++_shared.so"
            )
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    compileOnly("io.realm.kotlin:library-base:0.10.0")
    implementation("org.pytorch:pytorch_android_lite:1.12")
    implementation("org.pytorch:pytorch_android_torchvision_lite:1.12")

    with(Deps.AndroidX) {
        implementation(core)
        implementation(appcompat)
        implementation(material)
        implementation(camera)
        implementation(cameraLifecycle)
        implementation(cameraView)
    }
    with(Deps.Accompanist) {
        implementation(navigationAnimation)
        implementation(permissions)
    }
    with(Deps.MLKit) {
        implementation(barcodeScanning)
    }
    with(Deps.JetpackCompose) {
        implementation(activity)
        implementation(ui)
        implementation(material)
        implementation(preview)
        implementation(viewModel)
        androidTestImplementation(junit)
        debugImplementation(tooling)
    }

    with(Deps.AndroidAuth0) {
        implementation(auth0)
    }

    with(Deps.Dagger) {
        implementation(hiltAndroid)
        kapt(hiltAndroidCompiler)
        implementation("androidx.hilt:hilt-navigation-compose:1.0.0-beta01")
    }

    with(Deps.JetpackNavigation) {
        implementation(navigationFragment)
        implementation(navigationUi)
        implementation(navigationDynamicFeatures)
        implementation(navigationCompose)
    }

    with(Deps.AndroidXTesting) {
        androidTestImplementation(junit)
        androidTestImplementation(espresso)
    }

    with(Deps.Junit) {
        testImplementation(junit)
    }

    with(Deps.KermitTheLog) {
        implementation(kotlin(moduleName))
        implementation(kermit)
    }

    with(Deps.Glide) {
        implementation(glide)
        implementation(glideCompiler)
    }
}

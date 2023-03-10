
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
        maven {
            val localProperties = java.util.Properties()
            localProperties.load(java.io.FileInputStream(rootProject.file("local.properties")))

            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/brightdotai/openapi-kmm-generator/")
            credentials {
                username = System.getenv("GITHUB_USER") ?: localProperties.getProperty("github.user") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("github.token") ?: ""
            }
        }
    }
    dependencies {
        classpath(Deps.RootBuild.kotlinGradle)
        classpath(Deps.RootBuild.androidGradle)
        classpath(Deps.RootBuild.mokoResources)
        classpath(Deps.RootBuild.hiltGradle)
        classpath(Deps.RootBuild.kermitGradle) // Kermit "Chisel" plugin for stripping logs
        classpath(Deps.RootBuild.sqlDelGradle)
        classpath(Deps.RootBuild.generatorGradle)
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven {
            val localProperties = java.util.Properties()
            localProperties.load(java.io.FileInputStream(rootProject.file("local.properties")))

            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/brightdotai/komma/")
            credentials {
                username = System.getenv("GITHUB_USER") ?: localProperties.getProperty("github.user") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("github.token") ?: ""
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

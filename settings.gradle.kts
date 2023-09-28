pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        maven {
            url = java.net.URI("https://jitpack.io")
        }
        maven {
            url = java.net.URI("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
        maven {
            url = java.net.URI("https://artifact.bytedance.com/repository/pangle/")
        }
        maven {
            url = java.net.URI("https://sdk.tapjoy.com/")
        }
    }
}

rootProject.name = "AdTest"
include(":app")
 
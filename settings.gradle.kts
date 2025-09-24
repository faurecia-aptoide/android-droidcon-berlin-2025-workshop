pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DeveloperWorkshop"
include(":app")
include(":common")
include(":android-automotive-poi-sample")
include(":android-automotive-covesa-push-notifications-sample")
include(":android-automotive-vehicle-properties-sample")
include(":android-automotive-audio-while-driving-sample")
include(":android-automotive-vehicle-properties-sample-app")
include(":android-automotive-covesa-push-notifications-sample-app")
include(":android-automotive-audio-while-driving-sample-app")

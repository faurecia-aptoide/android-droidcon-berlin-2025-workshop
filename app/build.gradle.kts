plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.forvia.droidcon.developerworkshop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.forvia.droidcon.developerworkshop"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

tasks.register("installAllApps") {
    dependsOn(
        ":android-automotive-audio-while-driving-sample-app:installDebug",
        ":android-automotive-vehicle-properties-sample-app:installDebug",
        ":android-automotive-covesa-push-notifications-sample-app:installDebug",
        ":android-automotive-poi-sample:installDebug",
    )
}

tasks.register("installPoiSampleApp") {
    dependsOn(
        ":android-automotive-poi-sample:installDebug",
    )
}

tasks.register("installPreviewApp") {
    dependsOn(
        ":app:installDebug",
        ":app:installPoiSampleApp",
    )
}

dependencies {
    implementation(project(":common"))
    implementation(project(":android-automotive-audio-while-driving-sample"))
    implementation(project(":android-automotive-vehicle-properties-sample"))
    implementation(project(":android-automotive-covesa-push-notifications-sample"))
    implementation(libs.bundles.car)
}
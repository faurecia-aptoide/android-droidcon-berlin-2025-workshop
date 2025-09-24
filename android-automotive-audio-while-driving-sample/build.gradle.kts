plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace =
        "com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample"
    compileSdk = 36

    defaultConfig {
        //applicationId =
        //  "com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample"
        minSdk = 29
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("Boolean", "FEATURE_BACKGROUND_AUDIO_WHILE_DRIVING", "true")
    }

    buildTypes {
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
        buildConfig = true
    }

    useLibrary("android.car")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.bundles.car)
}
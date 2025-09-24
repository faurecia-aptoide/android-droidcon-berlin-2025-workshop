plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.forvia.droidcon.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }
}

dependencies {
    // Compose BOM
    api(platform(libs.androidx.compose.bom))
    androidTestApi(platform(libs.androidx.compose.bom))

    // Main app libraries
    api(libs.bundles.androidx)
    api(libs.bundles.compose)
    api(libs.bundles.koin)
    // Testing
    testApi(libs.bundles.testing)
    androidTestApi(libs.bundles.testing)
    androidTestApi(libs.bundles.compose.testing)

    // Debug tools (only for debug builds)
    debugApi(libs.bundles.debug.compose)

    // Car APP
    api(libs.bundles.car)
    api(libs.androidx.datastore.preferences)
    api(libs.kotlinx.coroutines.android)
}
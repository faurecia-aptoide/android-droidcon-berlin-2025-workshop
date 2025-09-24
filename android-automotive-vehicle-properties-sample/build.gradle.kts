import groovy.json.JsonBuilder
import java.net.URL
import java.util.Base64
import java.util.regex.Pattern

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample"
    compileSdk = 36

    defaultConfig {
        //applicationId =
        //  "com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample"
        minSdk = 29
        targetSdk = 36
        //versionCode = 1
        //versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    useLibrary("android.car")
}

// Canonical AOSP source (Base64 with ?format=TEXT)
val aospUrl = providers.gradleProperty("vhalSrcUrl").orElse(
    "https://android.googlesource.com/platform/packages/services/Car/+/refs/heads/master/car-lib/src/android/car/VehiclePropertyIds.java?format=TEXT"
)

tasks.register("extractVehicleProperties") {
    description =
        "Extracts vehicle properties from VehiclePropertyIds.java to a JSON file in assets"
    group = "Custom"

    doLast {
        // Define input and output paths
        val src = aospUrl.get()
        println("Downloading (Base64): $src")

        // googlesource returns Base64 when ?format=TEXT
        val base64Bytes = URL(src).readBytes()
        val inputFile = Base64.getDecoder().decode(base64Bytes).toString(Charsets.UTF_8)

        val outputDir = file("src/main/assets")
        val outputFile = file("$outputDir/vehicle_properties.json")

        // Ensure output directory exists
        outputDir.mkdirs()

        // Lists to store property data
        val properties = mutableListOf<Map<String, Any>>()

        // Regex patterns to match property declarations and comments
        val propertyPattern = Pattern.compile("public static final int (\\w+)\\s*=\\s*(\\d+);")
        val commentPattern = Pattern.compile("/\\*\\*(.*?)\\*/", Pattern.DOTALL)
        val namePattern =
            Pattern.compile("^\\s*\\*\\s*([^@\\n].*?)(?=(?:\\s*\\*\\s*@|\\s*\\*/))", Pattern.DOTALL)

        // Read the input file
        val lines = inputFile
        val matcher = propertyPattern.matcher(lines)
        val commentMatcher = commentPattern.matcher(lines)

        // Store comments for processing
        val comments = mutableListOf<String>()
        while (commentMatcher.find()) {
            comments.add(
                commentMatcher.group(1).lines()
                    .joinToString("\n") { it.replace(Regex("^\\s*\\*\\s?"), "") }
                    .replace(Regex("\\{@code ([^}]+)}")) { match ->
                        "<a>${match.groupValues[1]}</a>"
                    }
                    .replace(Regex("\\{@link ([^}]+)}")) { match ->
                        val ref = match.groupValues[1].replace('#', '.')
                        "<a>$ref</a>"
                    }
                    .replace("<li>", "<li> ")
                    .replace("Property Config:", "<strong>Property Config:</strong>")
                    .replace("Required Permission:", "<strong>Required Permission</strong>")
            )
        }
        comments.removeAt(0)
        // Process each property
        var commentIndex = 0
        while (matcher.find()) {
            val propertyName = matcher.group(1)
            val propertyId = matcher.group(2)

            // Get the corresponding comment
            var comment = ""
            if (commentIndex < comments.size) {
                comment = comments[commentIndex]
                val nameMatcher = namePattern.matcher(comment)
                if (nameMatcher.find()) {
                    comment = nameMatcher.group(1).trim()
                    // Clean up comment: remove leading asterisks and extra whitespace
                    comment =
                        comment
                            .replace(Regex("(?m)^\\s*\\*\\s*"), "")
                            .replace(Regex("\\s+"), " ")
                }

                commentIndex++
            }

            // Skip INVALID property
            if (propertyName != "INVALID") {
                properties.add(
                    mapOf(
                        "name" to propertyName,
                        "id" to propertyId,
                        "description" to comment,
                    )
                )
            }
        }

        // Create JSON
        val jsonBuilder = JsonBuilder(mapOf("vehicleProperties" to properties))

        // Write JSON to file
        outputFile.writeText(jsonBuilder.toPrettyString())
        println("Vehicle properties extracted to ${outputFile.absolutePath}")
    }
}

// Ensure generation runs before assets are merged
tasks.matching { it.name in listOf("preBuild", "mergeDebugAssets", "mergeReleaseAssets") }
    .configureEach { dependsOn("extractVehicleProperties") }

dependencies {
    implementation(project(":common"))
    implementation(libs.bundles.car)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.gson)
}
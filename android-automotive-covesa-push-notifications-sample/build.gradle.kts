import java.io.ByteArrayOutputStream
import java.net.URL

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace =
        "com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample"
    compileSdk = 36

    defaultConfig {
        // applicationId =
        //   "com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample"
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
}


val sunupUrl = providers.gradleProperty("SUNUP_URL")
val sunupApkName = providers.gradleProperty("SUNUP_APK_NAME").getOrElse("sunup.apk")
val sunupDir = layout.projectDirectory.dir("third_party/sunup")

val sunupApkDirectory = sunupDir.file(sunupApkName)
val apkPath = "${projectDir}/third_party/sunup/sunup.apk"

tasks.register("downloadSunup") {
    outputs.file(sunupApkDirectory)
    doLast {
        sunupDir.asFile.mkdirs()
        URL(sunupUrl.get()).openStream().use { input ->
            sunupApkDirectory.asFile.outputStream().use { output -> input.copyTo(output) }
        }
        println("Downloaded Sunup to ${sunupApkDirectory.asFile.absolutePath}")
    }
}

val adbPath = android.sdkDirectory.resolve("platform-tools/adb").absolutePath
// Where you downloaded/placed the Sunup APK
val sunupApk = layout.projectDirectory.file("third_party/sunup/sunup.apk")
val sunupPkg = "org.unifiedpush.distributor.sunup"

// Optional: choose user; AAOS launcher is often user 10
val sunupUser = providers.gradleProperty("SUNUP_USER").orElse("10")

fun Project.execOut(vararg cmd: Any): Pair<Int, String> {
    val buf = ByteArrayOutputStream()
    val result = exec {
        isIgnoreExitValue = true
        commandLine(*cmd)
        standardOutput = buf
        errorOutput = buf
    }.exitValue
    return result to buf.toString().trim()
}

tasks.register("installSunup") {
    dependsOn("downloadSunup") // keep if you have a download task

    doLast {
        // Get adb path from AGP toolchain (null-safe)
        val adbFile = androidComponents.sdkComponents.adb.get().asFile
        check(adbFile.exists()) { "ADB not found from AGP: ${adbFile.absolutePath}. Check Android SDK install." }

        check(sunupApk.asFile.exists()) { "Sunup APK missing at ${sunupApk.asFile.absolutePath}" }

        val adb = adbFile.absolutePath

        // 1) device online?
        val (stCode, stOut) = project.execOut(adb, "get-state")
        check(stCode == 0 && stOut.contains("device")) { "No online device: adb get-state -> $stOut" }

        // 2) install-existing for target user (works if package present for another user)
        val (_, listOut) = project.execOut(adb, "shell", "pm", "list", "packages", sunupPkg)
        if (listOut.contains(sunupPkg)) {
            val (ieCode, ieOut) = project.execOut(
                adb,
                "shell",
                "cmd",
                "package",
                "install-existing",
                "--user",
                sunupUser.get(),
                sunupPkg
            )
            if (ieCode == 0) {
                println("Sunup enabled for user ${sunupUser.get()} (install-existing).")
                return@doLast
            } else {
                println("install-existing failed: $ieOut — trying full install…")
            }
        }

        // 3) full install for user
        val (insCode, insOut) = project.execOut(
            adb,
            "install",
            "--user",
            sunupUser.get(),
            "-r",
            "-g",
            sunupApk.asFile.absolutePath
        )
        if (insCode == 0 || insOut.contains("Success", true)) {
            println("Sunup installed for user ${sunupUser.get()}.")
            return@doLast
        }

        // 4) common failure handling
        when {
            insOut.contains("INSTALL_FAILED_ALREADY_EXISTS", true) -> {
                val (ie2Code, ie2Out) = project.execOut(
                    adb,
                    "shell",
                    "cmd",
                    "package",
                    "install-existing",
                    "--user",
                    sunupUser.get(),
                    sunupPkg
                )
                check(ie2Code == 0) { "Already exists but couldn’t enable for user ${sunupUser.get()}: $ie2Out" }
            }

            insOut.contains("UPDATE_INCOMPATIBLE", true) || insOut.contains("SIGNATURE", true) -> {
                project.execOut(adb, "uninstall", "--user", sunupUser.get(), sunupPkg)
                val (reCode, reOut) = project.execOut(
                    adb,
                    "install",
                    "--user",
                    sunupUser.get(),
                    "-r",
                    "-g",
                    sunupApk.asFile.absolutePath
                )
                check(reCode == 0 || reOut.contains("Success", true)) { "Reinstall failed: $reOut" }
            }

            else -> error("adb install failed (user ${sunupUser.get()}): $insOut")
        }
    }
}

tasks.matching { it.name in listOf("preBuild", "mergeDebugAssets", "mergeReleaseAssets") }
    .configureEach { dependsOn("installSunup") }

dependencies {
    implementation(project(":common"))
    api(files("libs/client-debug.aar"))
    implementation(libs.unifiedpush)
    implementation(libs.volley)
    implementation(libs.tink.webpush)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.gson)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
}
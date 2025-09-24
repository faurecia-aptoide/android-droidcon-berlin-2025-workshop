package com.forvia.droidcon.developerworkshop.presentation

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent

fun Context.launchPackageWithComponent(packageName: String, onMissing: (() -> Unit)? = null) {
    val pkg = packageName
    val comp = ComponentName(
        pkg,
        "androidx.car.app.activity.CarAppActivity"
    )
    val intent = Intent().setComponent(comp)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onMissing?.invoke()
    }
}

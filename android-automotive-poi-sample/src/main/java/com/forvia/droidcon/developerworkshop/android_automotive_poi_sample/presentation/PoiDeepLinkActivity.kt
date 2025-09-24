package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.car.app.CarContext

class PoiDeepLinkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // forward to CarAppService host
        val intent = Intent(CarContext.ACTION_NAVIGATE).apply {
            setPackage(packageName)
        }
        try {
            startService(intent) // The host will redirect this
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()
    }
}

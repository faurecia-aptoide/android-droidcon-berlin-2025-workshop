package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.model

import android.content.Intent
import androidx.core.net.toUri

data class PoiModel(
    val name: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toIntent(action: String): Intent {
        val uri =
            "geo:${latitude},${longitude}".toUri()
        return Intent(action, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}

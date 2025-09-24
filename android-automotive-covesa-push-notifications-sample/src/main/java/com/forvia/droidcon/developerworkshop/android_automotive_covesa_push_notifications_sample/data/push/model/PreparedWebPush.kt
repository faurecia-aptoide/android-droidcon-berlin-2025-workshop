package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model

/**
 * Data class to hold the prepared WebPush data.
 */
data class PreparedWebPush(
    val url: String,
    val headers: Map<String, String>,
    val body: ByteArray?
)

package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.permissions

interface NotificationPermissionHandler {
    fun isPermissionRequired(): Boolean
    fun isGranted(context: android.content.Context): Boolean
}

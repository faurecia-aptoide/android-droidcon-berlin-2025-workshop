package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.permissions

/**
 * Implementation of [NotificationPermissionHandler] for checking and managing notification permissions.
 */
class NotificationPermissionHandlerImpl : NotificationPermissionHandler {

    /**
     * Checks if notification permission is required based on the Android SDK version.
     * @return `true` if permission is required, `false` otherwise.
     */
    override fun isPermissionRequired(): Boolean =
        android.os.Build.VERSION.SDK_INT >= 33

    /**
     * Checks if notification permission is granted.
     * @param context The application context.
     * @return `true` if permission is granted, `false` otherwise.
     */
    override fun isGranted(context: android.content.Context): Boolean {
        if (!isPermissionRequired()) return true
        val pm = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        return pm == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}

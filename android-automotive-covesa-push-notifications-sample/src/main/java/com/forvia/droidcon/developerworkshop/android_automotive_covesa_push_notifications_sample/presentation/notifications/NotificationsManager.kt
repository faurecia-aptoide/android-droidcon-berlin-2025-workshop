package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.notifications

interface NotificationsManager {
    fun createNotificationChannel()
    fun showNotification(title: String?, message: String?)
}

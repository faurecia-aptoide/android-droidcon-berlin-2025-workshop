package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model

sealed class PushNotificationDataModel {
    class PayloadOnly(val payload: String) : PushNotificationDataModel()
    class Notification(
        val title: String,
        val body: String,
        val payload: String,
    ) : PushNotificationDataModel()
}

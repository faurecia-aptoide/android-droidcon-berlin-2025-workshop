package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.mappers

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.PushNotificationDTO
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.PushNotificationDataModel

fun PushNotificationDTO.toDataModel(): PushNotificationDataModel =
    notification?.let {
        PushNotificationDataModel.Notification(
            title = notification.title ?: "",
            body = notification.body ?: "",
            payload = data?.payload ?: ""
        )
    } ?: run {
        PushNotificationDataModel.PayloadOnly(
            payload = data?.payload ?: ""
        )
    }

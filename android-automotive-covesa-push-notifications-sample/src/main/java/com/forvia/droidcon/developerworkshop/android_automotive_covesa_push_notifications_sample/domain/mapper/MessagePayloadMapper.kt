package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.PushNotificationDTO

fun MessagePayload.toDomain(): PushNotificationDTO =
    PushNotificationDTO(
        notification = notification?.toDomain(),
        data = data?.toDomain()
    )

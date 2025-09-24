package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.NotificationContent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.NotificationContentDTO

fun NotificationContent.toDomain(): NotificationContentDTO =
    NotificationContentDTO(
        title = title,
        body = body,
    )

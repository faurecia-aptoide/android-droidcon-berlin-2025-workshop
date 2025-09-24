package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.DataContent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.DataContentDTO

fun DataContent.toDomain(): DataContentDTO =
    DataContentDTO(
        payload = payload,
    )

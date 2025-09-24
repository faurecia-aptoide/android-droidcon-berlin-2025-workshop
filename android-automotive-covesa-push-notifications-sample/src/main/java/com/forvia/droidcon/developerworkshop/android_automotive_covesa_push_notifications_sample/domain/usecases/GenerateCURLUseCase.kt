package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager

class GenerateCURLUseCase(
    private val myPushManager: MyPushManager
) {
    operator fun invoke(content: MessagePayload?): String = myPushManager.generateCURL(content)
}

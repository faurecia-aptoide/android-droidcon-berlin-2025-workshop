package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager

class SendPushNotificationUseCase(
    private val myPushManager: MyPushManager
) {
    operator fun invoke(messagePayload: MessagePayload) =
        myPushManager.sendPushNotification(messagePayload)
}

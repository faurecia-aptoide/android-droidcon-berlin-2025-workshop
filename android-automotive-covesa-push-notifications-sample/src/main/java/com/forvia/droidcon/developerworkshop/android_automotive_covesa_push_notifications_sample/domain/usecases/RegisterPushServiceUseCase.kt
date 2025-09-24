package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager

class RegisterPushServiceUseCase(
    private val myPushManager: MyPushManager
) {
    operator fun invoke() = myPushManager.registerPushService()
}

package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases

import android.util.Log
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper.toDomain
import kotlinx.coroutines.flow.map

class SubscribeToSavedPushNotificationsUseCase(
    private val myPushManager: MyPushManager
) {
    companion object STSPNUC {
        private const val TAG = "SubscribeToSavedPushNotificationsUseCase"
    }

    operator fun invoke() = myPushManager.subscribeToReceivedPushNotifications().map {
        it.map { each -> each.toDomain() }
    }.also {
        Log.i(TAG, "New PushReceived")
    }
}

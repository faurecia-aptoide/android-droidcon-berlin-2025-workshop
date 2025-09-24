package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.PushServiceImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import global.covesa.sdk.api.client.push.data.PushEndpoint
import global.covesa.sdk.api.client.push.data.PushMessage
import kotlinx.coroutines.flow.Flow

interface MyPushManager {

    var isPushRegistered: Boolean

    fun getDistributors(): List<String>
    fun registerPushService(): Flow<RegistrationAction>

    fun sendPushNotification(content: MessagePayload)

    fun handMessagePayload(message: PushMessage?)

    fun storePushEndpoint(endpoint: PushEndpoint?)

    fun subscribeToPushServiceEvents(): Flow<PushServiceImpl.NewRegistrationState>

    fun subscribeToReceivedPushNotifications(): Flow<List<MessagePayload>>

    fun generateCURL(content: MessagePayload?): String
}

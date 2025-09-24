package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManagerImpl
import com.google.gson.Gson
import org.koin.java.KoinJavaComponent.inject

/**
 * BroadcastReceiver that listens for push notification intents and triggers the display of the notification.
 * This class is responsible for receiving the push notification payload, parsing it, and then
 * using the [MyPushManager] to display the notification to the user.
 */
class PushTriggerBroadcastReceiver : BroadcastReceiver() {

    private val myPushManager: MyPushManager by inject(MyPushManagerImpl::class.java)

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast.
     * This method extracts the push notification payload from the intent,
     * decodes it from JSON into a [MessagePayload] object, and then
     * triggers the [MyPushManager] to send the push notification.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("payload")
        message?.let {
            val decoded = Gson().fromJson(message, MessagePayload::class.java)
            myPushManager.sendPushNotification(decoded)
        }
    }
}

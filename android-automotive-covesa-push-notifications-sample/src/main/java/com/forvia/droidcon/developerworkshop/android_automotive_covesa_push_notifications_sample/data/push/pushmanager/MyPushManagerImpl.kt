package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager

import android.content.Context
import android.util.Log
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.SetupManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.model.SetupState
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.PushServiceImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils.logAndShowCurlWithContent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.notifications.NotificationsManager
import com.google.gson.Gson
import global.covesa.sdk.api.client.push.PushManager
import global.covesa.sdk.api.client.push.data.PushEndpoint
import global.covesa.sdk.api.client.push.data.PushMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementation of [MyPushManager] that handles push notification registration,
 * message handling, and interaction with a fake application server.
 *
 * @property context The application context.
 * @property fakeApplicationServer The [FakeApplicationServer] instance for mocking server interactions.
 * @property notificationsManager The [NotificationsManager] for displaying notifications.
 */
class MyPushManagerImpl(
    private val context: Context,
    private val fakeApplicationServer: FakeApplicationServer,
    private val notificationsManager: NotificationsManager,
    private val setupManager: SetupManager,
) : MyPushManager {

    override var isPushRegistered: Boolean = false

    companion object MPMI {
        private const val TAG = "MyPushManagerImpl"
    }

    /**
     * Retrieves a list of available push distributors.
     *
     * @return A list of distributor names.
     */
    override fun getDistributors(): List<String> =
        PushManager.getDistributors(context)

    /**
     * Registers the push service with the selected distributor.
     *
     * @return A [Flow] emitting [RegistrationAction] to indicate the registration status.
     */
    override fun registerPushService(): Flow<RegistrationAction> {
        PushManager.saveDistributor(context, getDistributors().first())
        return registerPush()
    }

    /**
     * Sends a push notification through the fake application server.
     *
     * @param content The [MessagePayload] to send.
     */
    override fun sendPushNotification(content: MessagePayload) {
        fakeApplicationServer.MockApi().sendPushNotification(content)
    }

    /**
     * Handles an incoming push message.
     * Decodes the message content, displays a notification if present,
     * and stores the message payload in the fake application server.
     *
     * @param message The [PushMessage] received.
     */
    override fun handMessagePayload(message: PushMessage?) {
        val decoded = Gson().fromJson(
            message?.content?.toString(Charsets.UTF_8),
            MessagePayload::class.java
        )
        decoded?.let {
            it.notification?.let { notification ->
                notificationsManager.showNotification(
                    title = notification.title,
                    message = notification.body,
                )
            }
            fakeApplicationServer.storePushNotification(decoded)
        } ?: run {
            Log.d(TAG, "Message content is empty. Skipping")
        }
    }

    /**
     * Stores the push endpoint received from the push service in the fake application server.
     *
     * @param endpoint The [PushEndpoint] to store.
     */
    override fun storePushEndpoint(endpoint: PushEndpoint?) {
        endpoint?.let {
            setupManager.saveSetupState(SetupState.COMPLETE)
        }
        fakeApplicationServer.MockApi().storePushEndpoint(endpoint)
    }

    /**
     * Subscribes to events from the push service.
     *
     * @return A [Flow] emitting [String] events from the [PushServiceImpl].
     */
    override fun subscribeToPushServiceEvents() = PushServiceImpl.events

    /**
     * Subscribes to the list of received push notifications stored in the fake application server.
     *
     * @return A [Flow] emitting a list of [MessagePayload]s.
     */
    override fun subscribeToReceivedPushNotifications(): Flow<List<MessagePayload>> =
        fakeApplicationServer.notificationPayloads

    /**
     * Generates a cURL command string for sending a push notification with the given content.
     *
     * @param content The [MessagePayload] to include in the cURL command.
     * @return A string representing the cURL command.
     */
    override fun generateCURL(content: MessagePayload?): String =
        fakeApplicationServer.logAndShowCurlWithContent(content = content)
//    override fun generateCURL(content: MessagePayload?): String =
//        fakeApplicationServer.logAndShowCurl(content)

    /**
     * Private function to handle the push registration process.
     * It tries to use a saved distributor or the current/default distributor.
     *
     * @return A [Flow] emitting [RegistrationAction] to indicate the registration status.
     */
    private fun registerPush(): Flow<RegistrationAction> = callbackFlow {
        Log.w(TAG, "Registering push on $context")
        if (PushManager.getSavedDistributor(context) != null) {
            Log.d(TAG, "Using saved distributor")
            trySend(doRegistration())
        } else {
            PushManager.tryUseCurrentOrDefaultDistributor(
                context
            ) { success ->
                Log.d(TAG, "Using current or default distributor")
                if (success) {
                    trySend(doRegistration())
                } else {
                    trySend(RegistrationAction.Failed)
                }
            }
        }
        awaitClose {
            Log.d(TAG, "Closing flow")
        }
    }

    /**
     * Private function to perform the actual push registration with the COVESA SDK.
     * It retrieves the VAPID public key from the fake application server and attempts to register.
     *
     * @return A [RegistrationAction] indicating the success or failure of the registration.
     */
    private fun doRegistration(): RegistrationAction {
        val vapidPubKey = fakeApplicationServer.MockApi().getVapidPubKey()
        return try {
            PushManager.register(
                context,
                vapid = vapidPubKey
            )
            Log.w(TAG, "UnifiedPush registered successfully.")
            setupManager.saveSetupState(SetupState.WAITING_FOR_ENDPOINT)
            RegistrationAction.Success
        } catch (e: PushManager.VapidNotValidException) {
            Log.w(TAG, "UnifiedPush failed to register with exception $e")
            RegistrationAction.FailedWithVapid(e)
        }
    }
}

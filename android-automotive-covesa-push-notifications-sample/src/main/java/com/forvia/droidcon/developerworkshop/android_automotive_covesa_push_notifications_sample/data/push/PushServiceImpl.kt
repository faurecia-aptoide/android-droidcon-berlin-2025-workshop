package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push

import android.util.Log
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager
import global.covesa.sdk.api.client.push.PushService
import global.covesa.sdk.api.client.push.data.FailedReason
import global.covesa.sdk.api.client.push.data.PushEndpoint
import global.covesa.sdk.api.client.push.data.PushMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.android.ext.android.inject

/**
 * Implementation of the COVESA PushService.
 * This class handles incoming push notifications and registration events.
 */
class PushServiceImpl() : PushService() {
    private val myPushManager: MyPushManager by inject()

    /**
     * Represents the new registration state.
     * @property registered True if the client is registered, false otherwise.
     */
    class NewRegistrationState(val registered: Boolean)

    /**
     * Called when a new push endpoint is available.
     * Stores the endpoint and updates the registration state.
     * @param endpoint The new push endpoint.
     * @param instance The instance identifier (not used in this implementation).
     */
    override suspend fun onNewEndpoint(endpoint: PushEndpoint, instance: String) {
        if (endpoint.url.isNotEmpty()) {
            Log.d(TAG, "OnNewEndpoint: ${endpoint.url}")

            myPushManager.storePushEndpoint(endpoint)
            updateRegistrationState(true)
        }
    }

    /**
     * Called when a new push message is received.
     * Logs the message content, processes the payload, and updates the registration state.
     * @param message The received push message.
     * @param instance The instance identifier (not used in this implementation).
     */
    override suspend fun onMessage(message: PushMessage, instance: String) {
        Log.d(TAG, "Received message: ${message.content.toString(Charsets.UTF_8)}")

        myPushManager.handMessagePayload(
            message = message
        )
        updateRegistrationState(true)
    }

    /**
     * Called when push registration fails.
     * Logs the failure reason and updates the registration state.
     * @param reason The reason for the registration failure.
     * @param instance The instance identifier (not used in this implementation).
     */
    override suspend fun onRegistrationFailed(reason: FailedReason, instance: String) {
        Log.d(TAG, "Registration failed: $reason")
        updateRegistrationState(false)
    }

    /**
     * Called when the client is unregistered from push notifications.
     * Clears the stored push endpoint and updates the registration state.
     * @param instance The instance identifier (not used in this implementation).
     */
    override suspend fun onUnregistered(instance: String) {
        myPushManager.storePushEndpoint(null)
        updateRegistrationState(false)
    }

    /**
     * Update the UI
     * Emits a [NewRegistrationState] event to update observers about the registration status.
     * @param registered True if registered, false otherwise.
     */
    private suspend fun updateRegistrationState(registered: Boolean) {
        _events.emit(NewRegistrationState(registered))
    }

    companion object {
        private const val TAG = "PushServiceImpl"
        private val _events = MutableSharedFlow<NewRegistrationState>()

        /**
         * A [kotlinx.coroutines.flow.SharedFlow] that emits [NewRegistrationState] events.
         */
        val events = _events.asSharedFlow()
    }
}

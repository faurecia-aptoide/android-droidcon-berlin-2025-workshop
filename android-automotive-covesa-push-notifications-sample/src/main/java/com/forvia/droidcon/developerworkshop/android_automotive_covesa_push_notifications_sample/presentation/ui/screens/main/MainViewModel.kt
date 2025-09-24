package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.DataContent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.NotificationContent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.GenerateCURLUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.SendPushNotificationUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.SubscribeToSavedPushNotificationsUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.mappers.toDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.NotificationType
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions.MainEvent
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.viewstate.MainUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the MainScreen.
 *
 * This ViewModel handles the logic for the main screen, including:
 * - Managing the UI state ([com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.viewstate.MainUiState]).
 * - Handling user actions ([MainAction]).
 * - Emitting events ([MainEvent]).
 * - Interacting with use cases to perform operations such as:
 *   - Getting distributors.
 *   - Registering for push notifications.
 *   - Sending push notifications.
 *   - Subscribing to push notification events and saved notifications.
 *   - Checking and setting setup completion status.
 */
class MainViewModel(
    private val sendPushNotificationUseCase: SendPushNotificationUseCase,
    private val subscribeToSavedPushNotificationsUseCase: SubscribeToSavedPushNotificationsUseCase,
    private val generateCURLUseCase: GenerateCURLUseCase,
) : ViewModel() {

    private val _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    var mainUiState = _mainUiState.asStateFlow()

    private val _events = MutableSharedFlow<MainEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            subscribeToSavedPushNotificationsUseCase().collect {
                reduce { copy(pushNotificationList = it.map { each -> each.toDataModel() }) }
            }
        }
    }

    /**
     * Handles actions dispatched from the UI.
     *
     * @param action The [MainAction] to be processed.
     */
    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.OnPageLoaded -> {
            }
            is MainAction.SendNotification -> sendNotification(
                title = action.title,
                content = action.content,
                payload = action.payload
            )
            is MainAction.SendPayloadOnly -> sendPayloadOnly(action.payload)
            MainAction.TogglePushNotificationType -> toggleNotificationType()
            is MainAction.GenerateCURLNotification -> generateCURL(
                title = action.title,
                content = action.content,
                payload = action.payload,
            )

            is MainAction.GenerateCURLPayloadOnly -> generateCURL(
                title = null,
                content = null,
                payload = action.payload
            )

            MainAction.ClearForm -> clearForm()
            is MainAction.OnPushContentUpdate -> reduce { copy(pushContent = action.content) }
            is MainAction.OnPushPayloadUpdate -> reduce { copy(pushPayload = action.payload) }
            is MainAction.OnPushTitleUpdate -> reduce { copy(pushTitle = action.title) }
        }
    }

    /**
     * Clears the input fields in the UI.
     */
    private fun clearForm() {
        reduce {
            copy(
                pushTitle = "",
                pushContent = "",
                pushPayload = "",
            )
        }
    }

    /**
     * Generates a cURL command for sending a push notification.
     *
     * @param title The title of the notification (optional).
     * @param content The content of the notification (optional).
     * @param payload The payload of the notification (optional).
     */
    private fun generateCURL(title: String?, content: String?, payload: String?) {
        val messagePayload = MessagePayload(
            notification = if (title.isNullOrEmpty() && content.isNullOrEmpty()) null else NotificationContent(
                title,
                content
            ),
            data = DataContent(payload),
        )
        generateCURLUseCase(messagePayload)
        _events.tryEmit(MainEvent.ShowMessage("Check logcat for generated cURL"))
    }

    /**
     * Toggles the type of notification to be sent (Notification or Payload Only).
     */
    private fun toggleNotificationType() {
        reduce {
            copy(
                notificationTypeState =
                    if (notificationTypeState == NotificationType.NOTIFICATION)
                        NotificationType.PAYLOAD_ONLY
                    else
                        NotificationType.NOTIFICATION
            )
        }
    }

    /**
     * Sends a push notification with a title, content, and optional payload.
     *
     * @param title The title of the notification.
     * @param content The content of the notification.
     * @param payload The payload of the notification (optional).
     */
    private fun sendNotification(title: String, content: String, payload: String?) {
        sendPushNotificationUseCase(
            MessagePayload(
                notification = NotificationContent(title, content),
                data = DataContent(payload),
            )
        )
    }

    /**
     * Sends a push notification with only a payload.
     *
     * @param content The content of the payload.
     */
    private fun sendPayloadOnly(content: String) {
        sendPushNotificationUseCase(
            MessagePayload(
                notification = null,
                data = DataContent(content),
            )
        )
    }

    /**
     * Reduces the current UI state by applying the given block.
     *
     * @param block A lambda function that transforms the current [MainUiState] to a new state.
     */
    private inline fun reduce(block: MainUiState.() -> MainUiState) {
        _mainUiState.update { current ->
            val newState = current.block()
            newState.copy(
                buttonsEnabled = if (newState.notificationTypeState == NotificationType.NOTIFICATION) {
                    newState.pushTitle.isNotBlank() && newState.pushContent.isNotBlank()
                } else {
                    newState.pushPayload.isNotBlank()
                }
            )
        }
    }
}

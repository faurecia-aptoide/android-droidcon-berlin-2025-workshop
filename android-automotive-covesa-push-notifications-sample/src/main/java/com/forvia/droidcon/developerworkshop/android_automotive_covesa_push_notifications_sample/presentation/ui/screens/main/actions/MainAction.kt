package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions

import android.content.Context

sealed interface MainAction {
    data class OnPageLoaded(val context: Context) : MainAction
    data class SendNotification(val title: String, val content: String, val payload: String?) :
        MainAction
    data class SendPayloadOnly(val payload: String) : MainAction
    data class GenerateCURLNotification(
        val title: String,
        val content: String,
        val payload: String?
    ) : MainAction

    data class GenerateCURLPayloadOnly(val payload: String) : MainAction
    object TogglePushNotificationType : MainAction
    data class OnPushTitleUpdate(val title: String) : MainAction
    data class OnPushContentUpdate(val content: String) : MainAction
    data class OnPushPayloadUpdate(val payload: String) : MainAction
    object ClearForm : MainAction
}

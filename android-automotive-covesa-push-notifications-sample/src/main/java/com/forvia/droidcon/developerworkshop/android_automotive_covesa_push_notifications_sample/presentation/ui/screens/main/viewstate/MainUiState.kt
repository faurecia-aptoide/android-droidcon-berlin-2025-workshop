package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.viewstate

import android.content.Context
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.NotificationType
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.PushNotificationDataModel
import global.covesa.sdk.api.client.push.PushManager

data class MainUiState(
    val pushDistributor: String = "",
    val availableDistributors: List<String> = emptyList(),
    val pushNotificationList: List<PushNotificationDataModel> = emptyList(),
    val notificationTypeState: NotificationType = NotificationType.NOTIFICATION,
    val pushTitle: String = "",
    val pushContent: String = "",
    val pushPayload: String = "",
    val buttonsEnabled: Boolean = false,
) {
    constructor(context: Context) : this(
        pushDistributor = PushManager.getAckDistributor(context).toString(),
        availableDistributors = PushManager.getDistributors(context),
    )
}

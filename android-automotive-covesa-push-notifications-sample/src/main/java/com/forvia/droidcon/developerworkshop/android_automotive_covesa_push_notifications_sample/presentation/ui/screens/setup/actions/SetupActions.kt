package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.actions

import android.content.Context

sealed interface SetupActions {
    object RegisterPushService : SetupActions
    data class OnPageLoaded(val context: Context) : SetupActions
}

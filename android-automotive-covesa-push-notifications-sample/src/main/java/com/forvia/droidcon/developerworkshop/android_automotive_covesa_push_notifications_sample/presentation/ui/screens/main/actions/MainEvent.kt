package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.actions

sealed interface MainEvent {
    data class ShowMessage(val text: String) : MainEvent
}

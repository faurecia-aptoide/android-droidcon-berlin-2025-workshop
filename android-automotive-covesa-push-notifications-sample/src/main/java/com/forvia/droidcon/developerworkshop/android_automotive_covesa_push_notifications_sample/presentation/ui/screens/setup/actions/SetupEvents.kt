package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.actions

sealed interface SetupEvents {
    data object ShowLoading : SetupEvents
    data object ToMainScreen : SetupEvents
}

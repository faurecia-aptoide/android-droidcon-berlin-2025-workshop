package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.viewstate

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.SetupStateUiModel

data class SetupUiState(
    val isRegistered: Boolean = false,
    val setupState: SetupStateUiModel = SetupStateUiModel.NOT_STARTED,
)

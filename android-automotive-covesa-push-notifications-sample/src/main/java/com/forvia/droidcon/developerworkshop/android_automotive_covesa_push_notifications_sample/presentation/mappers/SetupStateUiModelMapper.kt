package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.mappers

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO.COMPLETE
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO.NOT_STARTED
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO.STARTED
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO.WAITING_FOR_ENDPOINT
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.SetupStateUiModel

fun SetupStateDTO.toDataModel(): SetupStateUiModel =
    when (this) {
        NOT_STARTED -> SetupStateUiModel.NOT_STARTED
        STARTED -> SetupStateUiModel.STARTED
        WAITING_FOR_ENDPOINT -> SetupStateUiModel.WAITING_FOR_ENDPOINT
        COMPLETE -> SetupStateUiModel.COMPLETE
    }

fun SetupStateUiModel.toDomain(): SetupStateDTO =
    when (this) {
        SetupStateUiModel.NOT_STARTED -> NOT_STARTED
        SetupStateUiModel.STARTED -> STARTED
        SetupStateUiModel.WAITING_FOR_ENDPOINT -> WAITING_FOR_ENDPOINT
        SetupStateUiModel.COMPLETE -> COMPLETE
    }

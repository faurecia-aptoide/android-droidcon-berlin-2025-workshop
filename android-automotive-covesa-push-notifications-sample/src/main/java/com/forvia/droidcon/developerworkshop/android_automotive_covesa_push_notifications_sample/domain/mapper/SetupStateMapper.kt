package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.model.SetupState
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO

fun SetupState.toDomain(): SetupStateDTO =
    when (this) {
        SetupState.NOT_STARTED -> SetupStateDTO.NOT_STARTED
        SetupState.STARTED -> SetupStateDTO.STARTED
        SetupState.WAITING_FOR_ENDPOINT -> SetupStateDTO.WAITING_FOR_ENDPOINT
        SetupState.COMPLETE -> SetupStateDTO.COMPLETE
    }

fun SetupStateDTO.fromDomain(): SetupState =
    when (this) {
        SetupStateDTO.NOT_STARTED -> SetupState.NOT_STARTED
        SetupStateDTO.STARTED -> SetupState.STARTED
        SetupStateDTO.WAITING_FOR_ENDPOINT -> SetupState.WAITING_FOR_ENDPOINT
        SetupStateDTO.COMPLETE -> SetupState.COMPLETE
    }

package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.SetupManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper.fromDomain
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.model.SetupStateDTO

class SaveSetupStateUseCase(
    private val setupManager: SetupManager,
) {
    operator fun invoke(state: SetupStateDTO) = setupManager.saveSetupState(state.fromDomain())
}

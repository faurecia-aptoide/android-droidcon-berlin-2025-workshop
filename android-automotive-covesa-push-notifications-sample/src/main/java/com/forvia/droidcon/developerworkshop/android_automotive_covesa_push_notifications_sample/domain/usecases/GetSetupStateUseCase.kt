package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.SetupManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.mapper.toDomain
import kotlinx.coroutines.flow.map

class GetSetupStateUseCase(
    private val setupManager: SetupManager
) {
    operator fun invoke() = setupManager.getSetupState().map { it.toDomain() }
}

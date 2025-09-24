package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore

import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.model.SetupState
import kotlinx.coroutines.flow.Flow

interface SetupManager {
    fun saveSetupState(setupState: SetupState)
    fun getSetupState(): Flow<SetupState>
    fun isSetupComplete(): Boolean
}

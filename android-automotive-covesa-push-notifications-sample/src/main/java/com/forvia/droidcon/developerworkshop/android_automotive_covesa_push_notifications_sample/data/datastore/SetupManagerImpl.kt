package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.model.SetupState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Implementation of [SetupManager] that uses [DataStore] to store setup preferences.
 *
 * @property dataStore The [DataStore] instance to use for storing preferences.
 *
 * @constructor Creates a new instance of [SetupManagerImpl].
 * @param dataStore The [DataStore] instance to use for storing preferences.
 */
class SetupManagerImpl(
    private val dataStore: DataStore<Preferences>
) : SetupManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Saves the setup process state.
     *
     * @param setupState The [com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.model.SetupState] to save.
     * @see SetupManager.saveSetupState
     */
    override fun saveSetupState(setupState: SetupState) {
        scope.launch {
            dataStore.edit { prefs ->
                prefs[KEY_SETUP] = setupState.name
            }
        }
    }

    /**
     * A [StateFlow] that emits the current setup state.
     */
    private val setupStateFlow: StateFlow<SetupState> = dataStore.data
        .map { prefs ->
            prefs[KEY_SETUP]?.let { SetupState.valueOf(it) } ?: SetupState.NOT_STARTED
        }
        .distinctUntilChanged()
        .catch { emit(SetupState.NOT_STARTED) } // fallback on read errors
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = SetupState.NOT_STARTED
        )

    /**
     * Returns a [Flow] that emits the current setup state.
     *
     * @return A [Flow] that emits the current [SetupState].
     * @see SetupManager.getSetupState
     */
    override fun getSetupState(): Flow<SetupState> = setupStateFlow

    /**
     * Returns `true` if the setup process has been completed, `false` otherwise.
     */
    override fun isSetupComplete(): Boolean = setupStateFlow.value == SetupState.COMPLETE

    private companion object Companion {
        const val TAG = "SetupPreferencesImpl"
        private val KEY_SETUP = stringPreferencesKey("setup_completed")
    }
}

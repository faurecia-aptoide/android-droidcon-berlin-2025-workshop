package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.RegistrationAction
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils.openOtherAppAndScheduleReturn
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.GetSetupStateUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.RegisterPushServiceUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.SaveSetupStateUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.mappers.toDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.mappers.toDomain
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.model.SetupStateUiModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.actions.SetupActions
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.actions.SetupEvents
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.viewstate.SetupUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetupViewModel(
    private val registerPushServiceUseCase: RegisterPushServiceUseCase,
    private val saveSetupStateUseCase: SaveSetupStateUseCase,
    private val getSetupStateUseCase: GetSetupStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableStateFlow<SetupEvents?>(null)
    val uiEvents = _uiEvents.asStateFlow()

    init {
        viewModelScope.launch {
            getSetupStateUseCase().map { it.toDataModel() }.collect { setupState ->
                _uiState.update {
                    it.copy(
                        setupState = setupState
                    )
                }
                if (setupState == SetupStateUiModel.COMPLETE) {
                    _uiEvents.update {
                        SetupEvents.ToMainScreen
                    }
                }
            }
        }
    }

    fun onAction(action: SetupActions) {
        when (action) {
            SetupActions.RegisterPushService -> registerPushService()
            is SetupActions.OnPageLoaded -> {
                when (_uiState.value.setupState) {
                    SetupStateUiModel.NOT_STARTED -> {
                        _uiEvents.update {
                            SetupEvents.ShowLoading
                        }
                        saveSetupStateUseCase(SetupStateUiModel.STARTED.toDomain())
                        action.context.openOtherAppAndScheduleReturn()
                    }

                    SetupStateUiModel.WAITING_FOR_ENDPOINT -> {
                        _uiEvents.update {
                            SetupEvents.ShowLoading
                        }
                    }

                    SetupStateUiModel.STARTED -> {
                        _uiEvents.update {
                            SetupEvents.ShowLoading
                        }
                        registerPushService()
                    }

                    SetupStateUiModel.COMPLETE -> _uiEvents.update { SetupEvents.ToMainScreen }
                }
            }
        }
    }

    private fun registerPushService() {
        viewModelScope.launch {
            registerPushServiceUseCase().collect { result ->
                when (result) {
                    RegistrationAction.Failed,
                    is RegistrationAction.FailedWithVapid -> {
                        _uiState.update {
                            it.copy(isRegistered = false)
                        }
                    }

                    RegistrationAction.Success -> {
                        _uiState.update {
                            it.copy(
                                isRegistered = true,
                            )
                        }
                    }
                }
            }
        }
    }
}

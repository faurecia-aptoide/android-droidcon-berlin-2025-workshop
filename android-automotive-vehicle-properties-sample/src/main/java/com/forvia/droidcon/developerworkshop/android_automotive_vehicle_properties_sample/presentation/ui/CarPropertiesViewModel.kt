package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.ConnectCarUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.DisconnectCarUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.GetCarConnectionStatusUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.GetCarPropertiesUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.viewstate.CarPropertiesViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing car properties and connection status.
 *
 * @property connectCarUseCase Use case for connecting to the car.
 * @property disconnectCarUseCase Use case for disconnecting from the car.
 * @property getCarPropertiesUseCase Use case for retrieving car properties.
 * @property getCarConnectionStatusUseCase Use case for retrieving car connection status.
 */
class CarPropertiesViewModel(
    val connectCarUseCase: ConnectCarUseCase,
    val disconnectCarUseCase: DisconnectCarUseCase,
    val getCarPropertiesUseCase: GetCarPropertiesUseCase,
    val getCarConnectionStatusUseCase: GetCarConnectionStatusUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarPropertiesViewState.DEFAULT)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCarConnectionStatusUseCase().distinctUntilChanged().collectLatest { status ->
                reduce { copy(carConnectionStatus = status) }
            }
        }
    }

    /**
     * Handles actions dispatched from the UI.
     *
     * @param action The action to handle.
     */
    fun onAction(action: MainAction) {
        when (action) {
            MainAction.OnConnectCar -> connectCar()
            MainAction.OnDisconnectCar -> disconnectCar()
            MainAction.OnGetCarProperties -> getCarProperties()
            is MainAction.OnSearchQueryChanged -> updateQuery(action.query)
            is MainAction.OnCarPropertySelected -> onCarPropertySelected(action.carProperty)
            is MainAction.OnShowOnlyImplementedToggle -> onPropertyImplementedChanged(action.toggleValue)
        }
    }

    private fun onPropertyImplementedChanged(showOnlyImplemented: Boolean) {
        viewModelScope.launch {
            val mutateShowOnlyImplemented = if (showOnlyImplemented) true else null
            val carProps = getCarPropertiesUseCase(
                filter = _uiState.value.queryString,
                showOnlyImplemented = mutateShowOnlyImplemented
            )
            reduce {
                copy(
                    carProperties = carProps,
                    selectedCarProperty = carProps.firstOrNull(),
                    showOnlyImplemented = showOnlyImplemented
                )
            }
        }
    }

    /**
     * Updates the selected car property in the UI state.
     *
     * @param carProperty The selected car property.
     */
    private fun onCarPropertySelected(carProperty: CarPropertiesDataModel?) {
        reduce { copy(selectedCarProperty = carProperty) }
    }

    /**
     * Connects to the car.
     */
    private fun connectCar() {
        viewModelScope.launch {
            connectCarUseCase()
        }
    }

    /**
     * Disconnects from the car.
     */
    private fun disconnectCar() {
        reduce { copy(selectedCarProperty = null) }
        disconnectCarUseCase()
    }

    /**
     * Retrieves car properties.
     */
    private fun getCarProperties() {
        viewModelScope.launch {
            val mutateShowOnlyImplemented = if (_uiState.value.showOnlyImplemented) true else null
            val carProps = getCarPropertiesUseCase(
                filter = "",
                showOnlyImplemented = mutateShowOnlyImplemented
            )
            reduce {
                copy(
                    carProperties = carProps,
                    selectedCarProperty = carProps.firstOrNull(),
                )
            }
        }
    }

    /**
     * Updates the search query and filters car properties accordingly.
     *
     * @param queryString The new search query.
     */
    private fun updateQuery(queryString: String) {
        val currentSelection = _uiState.value.selectedCarProperty
        val mutateShowOnlyImplemented = if (_uiState.value.showOnlyImplemented) true else null
        reduce {
            copy(
                queryString = queryString,
                carProperties = getCarPropertiesUseCase(
                    filter = queryString,
                    showOnlyImplemented = mutateShowOnlyImplemented
                ),
                selectedCarProperty = if (
                    currentSelection?.propertyName?.contains(queryString, true) == true ||
                    currentSelection?.propertyIdentifier?.contains(queryString, true) == true
                ) {
                    currentSelection
                } else {
                    null
                }
            )
        }
    }

    /**
     * Reduces the UI state by applying the given block.
     *
     * @param block The block to apply to the current UI state.
     */
    private inline fun reduce(block: CarPropertiesViewState.() -> CarPropertiesViewState) {
        _uiState.update { current ->
            current.block()
        }
    }
}

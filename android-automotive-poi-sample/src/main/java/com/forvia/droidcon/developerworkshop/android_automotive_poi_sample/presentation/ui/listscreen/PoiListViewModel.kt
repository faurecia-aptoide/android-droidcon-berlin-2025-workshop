package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen

import androidx.car.app.CarContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forvia.droidcon.common.location.model.LocationModel
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.usecases.GetPOIsUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.mappers.PoiModelMapper
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.model.PoiModel
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions.MainEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the POI list screen.
 *
 * This ViewModel is responsible for fetching and managing the list of POIs (Points of Interest)
 * and the current location. It uses [GetPOIsUseCase] to retrieve the POI data and the
 * current location, and [PoiModelMapper] to map the domain models to presentation models.
 *
 * The list of POIs, including the current location as the first item, is exposed as a [StateFlow]
 * named [poiList], which can be observed by the UI to display the data.
 * It also exposes a [StateFlow] named [event] to communicate one-time events like showing messages
 * to the UI.
 *
 * The main action this ViewModel handles is [com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions.MainAction.FetchList], which triggers the fetching
 * of POIs and location updates.
 *
 * @property getPOIsUseCase The use case for getting the list of POIs and current location.
 * @property poiModelMapper The mapper for converting POI domain models to presentation models.
 */
class PoiListViewModel(
    private val getPOIsUseCase: GetPOIsUseCase,
    private val poiModelMapper: PoiModelMapper
) : ViewModel() {

    fun onAction(action: MainAction) = when (action) {
        is MainAction.FetchList -> {
            fetchPOIsWithLocation(action.carContext)
        }
    }

    /**
     * A private [MutableStateFlow] that holds the current list of POIs.
     * The list is initially empty.
     */
    private val _poiList = MutableStateFlow<List<PoiModel>>(mutableListOf())

    /**
     * A public [StateFlow] that exposes the list of POIs as an immutable stream.
     * UI components can observe this flow to react to changes in the POI list.
     */
    val poiList: StateFlow<List<PoiModel>> = _poiList.asStateFlow()

    /**
     * A private [MutableStateFlow] that holds the current one-time event.
     * Events are used to communicate actions like showing messages to the UI.
     * It is nullable, allowing for no event to be present.
     */
    private val _event = MutableStateFlow<MainEvent?>(null)

    /**
     * A public [StateFlow] that exposes the current one-time event as an immutable stream.
     * UI components can observe this flow to react to events.
     * It is nullable, allowing for no event to be present.
     */
    val event: StateFlow<MainEvent?> = _event.asStateFlow()

    private fun fetchPOIsWithLocation(carContext: CarContext) {
        viewModelScope.launch {
            val pois = getPOIsUseCase.getPoiList().map { poiModelMapper.map(it) }
            _poiList.update {
                pois
            }
            getPOIsUseCase.getCurrentLocation(carContext).distinctUntilChanged()
                .collectLatest { update ->
                    update?.let {
                        when (update) {
                            is LocationModel.LocationSuccess -> {
                                _poiList.update { currentList ->
                                    // Create a new list with the current location at the beginning
                                    val newPoiList = mutableListOf(
                                        PoiModel(
                                            name = "Current Location",
                                            latitude = update.location.latitude,
                                            longitude = update.location.longitude
                                        )
                                    )
                                    newPoiList.addAll(currentList.filter { it.name != "Current Location" })
                                    newPoiList // Return the new list
                                }
                            }

                            LocationModel.NoLocation -> _event.update {
                                MainEvent.ShowMessage(
                                    title = "No location found",
                                    message = "Sorry, we couldn't retrieve your location at the moment."
                                )
                            }

                            LocationModel.NoPermission -> _event.update {
                                MainEvent.ShowMessage(
                                    title = "Permission not granted",
                                    message = "In order to present your current location we require your location."
                                )
                            }
                        }
                    }
                }
        }
    }
}

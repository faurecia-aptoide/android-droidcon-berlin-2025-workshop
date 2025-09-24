package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.viewstate

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarConnectState
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel

data class CarPropertiesViewState(
    val carConnectionStatus: CarConnectState,
    val carProperties: List<CarPropertiesDataModel>,
    val queryString: String,
    val selectedCarProperty: CarPropertiesDataModel? = null,
    val showOnlyImplemented: Boolean = false,
) {
    companion object {
        val DEFAULT = CarPropertiesViewState(
            carConnectionStatus = CarConnectState.DISCONNECTED,
            carProperties = emptyList(),
            queryString = "",
        )
    }
}

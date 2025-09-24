package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.actions

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel

sealed interface MainAction {
    object OnConnectCar : MainAction
    object OnDisconnectCar : MainAction
    object OnGetCarProperties : MainAction
    data class OnSearchQueryChanged(val query: String) : MainAction
    data class OnCarPropertySelected(val carProperty: CarPropertiesDataModel?) : MainAction
    data class OnShowOnlyImplementedToggle(val toggleValue: Boolean) : MainAction
}

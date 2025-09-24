package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarConnectState
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarPropertiesModel
import kotlinx.coroutines.flow.MutableStateFlow

interface CarPropertiesRepository {
    fun getCarProperties(filter: String?, showOnlyImplemented: Boolean?): List<CarPropertiesModel>
    fun disconnectCar()
    fun connectCar()
    val carConnectionStatus: MutableStateFlow<CarConnectState>
}

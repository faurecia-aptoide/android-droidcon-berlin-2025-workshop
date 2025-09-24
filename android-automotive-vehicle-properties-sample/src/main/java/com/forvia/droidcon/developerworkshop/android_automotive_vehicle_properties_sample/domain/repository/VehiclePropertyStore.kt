package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.VehiclePropertiesDataModel

interface VehiclePropertyStore {
    @Throws(Exception::class)
    fun ensureLoaded()
    fun getList(): List<VehiclePropertiesDataModel>
    fun find(id: Int): VehiclePropertiesDataModel?
    fun displayNameOf(model: VehiclePropertiesDataModel?): String?
    fun descriptionOf(model: VehiclePropertiesDataModel?): String?
    fun constantNameOf(id: Int?): String?
}
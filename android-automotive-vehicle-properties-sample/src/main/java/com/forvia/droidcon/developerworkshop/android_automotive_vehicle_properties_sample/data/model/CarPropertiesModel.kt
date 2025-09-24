package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model

data class CarPropertiesModel(
    val propertyName: String?,
    val propertyDescription: String?,
    val propertyIdentifier: String?,
    val area: String?,
    val value: String?,
    val isImplemented: Boolean = false,
)

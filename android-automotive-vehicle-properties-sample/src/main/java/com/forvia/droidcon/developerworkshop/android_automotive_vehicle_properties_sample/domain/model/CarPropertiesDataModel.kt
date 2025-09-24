package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model

data class CarPropertiesDataModel(
    val propertyName: String,
    val propertyDescription: String,
    val propertyIdentifier: String,
    val area: String?,
    val value: String?,
)

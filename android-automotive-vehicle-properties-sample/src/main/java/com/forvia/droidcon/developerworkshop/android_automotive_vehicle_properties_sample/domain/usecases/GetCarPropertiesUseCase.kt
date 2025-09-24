package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.CarPropertiesRepository

class GetCarPropertiesUseCase(
    private val repository: CarPropertiesRepository,
) {
    operator fun invoke(
        filter: String?,
        showOnlyImplemented: Boolean?
    ): List<CarPropertiesDataModel> =
        repository.getCarProperties(filter, showOnlyImplemented).map { before ->
            CarPropertiesDataModel(
                propertyName = before.propertyName.toString(),
                propertyDescription = before.propertyDescription.toString(),
                propertyIdentifier = before.propertyIdentifier.toString(),
                area = before.area,
                value = before.value,
            )
        }
}

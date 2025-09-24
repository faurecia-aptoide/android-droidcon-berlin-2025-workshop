package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.CarPropertiesRepository

class ConnectCarUseCase(
    private val repository: CarPropertiesRepository,
) {
    operator fun invoke() {
        repository.connectCar()
    }
}

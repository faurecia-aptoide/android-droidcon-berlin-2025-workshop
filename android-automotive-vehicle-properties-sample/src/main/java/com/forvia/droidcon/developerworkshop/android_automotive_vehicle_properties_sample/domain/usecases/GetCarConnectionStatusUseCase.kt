package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarConnectState
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.CarPropertiesRepository
import kotlinx.coroutines.flow.Flow

class GetCarConnectionStatusUseCase(
    private val repository: CarPropertiesRepository,
) {

    operator fun invoke(): Flow<CarConnectState> {
        return repository.carConnectionStatus
    }

}

package com.forvia.droidcon.common.location

import androidx.car.app.CarContext
import com.forvia.droidcon.common.location.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationInteractor {
    fun isLocationEnabled(carContext: CarContext): Boolean
    fun getCurrentLocation(carContext: CarContext): Flow<LocationModel?>

    companion object Companion {
        const val TAG: String = "LocationInteractor"
    }
}

package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.usecases

import androidx.car.app.CarContext
import com.forvia.droidcon.common.location.model.LocationModel
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.model.PoiDTO
import kotlinx.coroutines.flow.Flow

interface GetPOIsUseCase {
    fun getCurrentLocation(carContext: CarContext): Flow<LocationModel?>

    fun getPoiList(): List<PoiDTO>
}

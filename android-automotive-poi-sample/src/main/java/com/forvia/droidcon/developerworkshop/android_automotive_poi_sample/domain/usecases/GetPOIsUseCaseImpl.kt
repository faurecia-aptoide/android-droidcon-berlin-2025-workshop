package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.usecases

import androidx.car.app.CarContext
import com.forvia.droidcon.common.location.LocationInteractor
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.datasource.PoiDataSource
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.mappers.PoiMapper
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.model.PoiDTO

/**
 * Implementation of the [GetPOIsUseCase] interface.
 * This use case is responsible for fetching and mapping Points of Interest (POIs).
 *
 * @property dataSource The [PoiDataSource] used to fetch raw POI data.
 * @property poiMapper The [PoiMapper] used to map raw POI data to [PoiDTO] objects.
 * @property locationInteractor The [LocationInteractor] used to get the current device location.
 */
class GetPOIsUseCaseImpl(
    private val dataSource: PoiDataSource,
    private val poiMapper: PoiMapper,
    private val locationInteractor: LocationInteractor
) : GetPOIsUseCase {
    /**
     * Retrieves the current location of the device.
     * @param carContext The [CarContext] required by the [LocationInteractor].
     */
    override fun getCurrentLocation(carContext: CarContext) =
        locationInteractor.getCurrentLocation(carContext)

    override fun getPoiList(): List<PoiDTO> = dataSource.getPOIs().map { poiMapper.map(it) }
}

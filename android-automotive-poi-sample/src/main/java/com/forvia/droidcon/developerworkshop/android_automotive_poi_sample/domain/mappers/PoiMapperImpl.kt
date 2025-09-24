package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.mappers

import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.model.Poi
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.model.PoiDTO

class PoiMapperImpl : PoiMapper {
    override fun map(poi: Poi): PoiDTO =
        PoiDTO(
            name = poi.name,
            latitude = poi.latitude,
            longitude = poi.longitude,
        )
}

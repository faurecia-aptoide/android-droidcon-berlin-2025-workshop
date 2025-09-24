package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.mappers

import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.model.Poi
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.model.PoiDTO

interface PoiMapper {
    fun map(poi: Poi): PoiDTO
}

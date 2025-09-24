package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.mappers

import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.model.PoiDTO
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.model.PoiModel

interface PoiModelMapper {
    fun map(poi: PoiDTO): PoiModel
}

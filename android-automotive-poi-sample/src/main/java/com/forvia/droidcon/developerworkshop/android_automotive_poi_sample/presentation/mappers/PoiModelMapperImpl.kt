package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.mappers

import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.model.PoiDTO
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.model.PoiModel

class PoiModelMapperImpl : PoiModelMapper {
    override fun map(poi: PoiDTO): PoiModel =
        PoiModel(
            name = poi.name,
            latitude = poi.latitude,
            longitude = poi.longitude,
        )
}

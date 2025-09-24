package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.datasource

import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.model.Poi

interface PoiDataSource {
    fun getPOIs(): List<Poi>
}

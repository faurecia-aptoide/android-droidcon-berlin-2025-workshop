package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.datasource

import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.model.Poi

class PoiDataSourceImpl() : PoiDataSource {
    override fun getPOIs(): List<Poi> =
        listOf(
            Poi("CityCube Berlin", 52.5015, 13.2694),
            Poi("Summer Garden", 52.5007389, 13.2709354),
            Poi("Platform 17 Memorial", 52.4970, 13.2636),
            Poi("Messe Berlin Entrance South", 52.5003, 13.2746),
            Poi("Motel One Berlin-Messe", 52.5053, 13.2738),
            Poi("S-Bahn Messe Süd Station", 52.4993, 13.2673),
            Poi("Hotel Ibis Berlin Messe", 52.5070, 13.2723),
            Poi("Funkturm Berlin (Radio Tower)", 52.5063, 13.2795),
            Poi("McDonald's Kaiserdamm", 52.5072, 13.2822),
            Poi("Trattoria Milano (Italian Restaurant)", 52.5061, 13.2774),
            Poi("Edeka Supermarket Messedamm", 52.5047, 13.2740),
            Poi("Berliner Hof Restaurant", 52.5036, 13.2707),
            Poi("Polizeiabschnitt 24 (Police Station)", 52.4996, 13.2751)
        )
}

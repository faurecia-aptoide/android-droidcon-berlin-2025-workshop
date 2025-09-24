# Android Droidcon Berlin 2025 Workshop - Points of Interest


## Description

You will be prompted in order to ask location permissions. Even if you don't give them, the app will
function as expected.
If you give them, after some time the app will obtain the location and then place your current
location on the top of the list.
When you click on a POI, if there is an app that allows navigation it will open that app with the
selected POI. The app will try to use the `CarContext.startCarApp` first. If it fails, it will
resort to the `Context.startActivity`.


## Changing the list of Points of Interested

1. The hardcoded data is being declared under PoiDataSourceImpl.
```kotlin
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
```


## Current location
1. Current location is being obtained from [LocationInteractorImpl#getCurrentLocation](https://github.com/faurecia-aptoide/android-droidcon-berlin-2025-workshop/blob/main/common/src/main/java/com/forvia/droidcon/common/location/LocationInteractorImpl.kt#L48C18-L48C36).
2. Added to the PoI list under [LocationInteractorImpl#fetchPOIsWithLocation](https://github.com/faurecia-aptoide/android-droidcon-berlin-2025-workshop/blob/66c2461aaf775cfd62f7b51553ddfb28bb6e16ad/android-automotive-poi-sample/src/main/java/com/forvia/droidcon/developerworkshop/android_automotive_poi_sample/presentation/ui/listscreen/PoiListViewModel.kt#L75).


## Changing Markers

Markers are defined in PoiListScreen.
Additional components can be customized, for more details: https://developer.android.com/reference/androidx/car/app/model/PlaceMarker.Builder

```kotlin
Row.Builder()
    .setTitle("${poi.name} (${poi.latitude}, ${poi.longitude})")
    // …
    .setMetadata(
        Metadata.Builder()
            .setPlace(
                Place.Builder(CarLocation.create(poi.latitude, poi.longitude))
                    .setMarker(newMarker(poi)) 
                    .build()
            ).build()
    )
    .build()

private fun newMarker(poi: Poi): PlaceMarker =
    PlaceMarker.Builder()
        .setColor(CarColor.RED)
        .build()
```

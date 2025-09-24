package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.utils

import android.content.Context
import android.content.Intent.ACTION_VIEW
import androidx.car.app.CarContext
import androidx.car.app.CarContext.ACTION_NAVIGATE
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.model.PoiModel

/**
 * Launches the map chooser with the given Point of Interest (POI).
 *
 * This function uses the CarContext to start a car app that can handle navigation to the given POI.
 *
 * @param poi The Point of Interest to navigate to.
 * @param onLaunchFailed A callback function that is invoked if the map chooser fails to launch.
 * It takes an Exception as a parameter, which provides details about the failure.
 */
fun CarContext.launchMapChooser(poi: PoiModel, onLaunchFailed: (Exception) -> Unit) {
    try {
        startCarApp(poi.toIntent(ACTION_NAVIGATE))
    } catch (exception: Exception) {
        onLaunchFailed(exception)
    }
}


/**
 * Launches the map chooser with the given Point of Interest (POI) when not in a car environment.
 *
 * This function uses the standard Android Context to start an activity that can handle viewing the given POI.
 *
 * @param poi The Point of Interest to view.
 * @param onLaunchFailed A callback function that is invoked if the map chooser fails to launch.
 * It takes an Exception as a parameter, which provides details about the failure.
 */
fun Context.launchMapChooserNoCarApi(poi: PoiModel, onLaunchFailed: (Exception) -> Unit) {
    try {
        startActivity(poi.toIntent(ACTION_VIEW))
    } catch (exception: Exception) {
        onLaunchFailed(exception)
    }
}

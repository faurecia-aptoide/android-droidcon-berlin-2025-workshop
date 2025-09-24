package com.forvia.droidcon.common.location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.car.app.CarContext
import com.forvia.droidcon.common.location.model.LocationModel
import com.forvia.droidcon.common.permissions.PermissionsManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first


/**
 * Implementation of [LocationInteractor] that provides location-related functionalities.
 *
 * @property permissionsManager The [PermissionsManager] used to check for location permissions.
 */
class LocationInteractorImpl(
    private val permissionsManager: PermissionsManager,
) : LocationInteractor {

    /**
     * Checks if location services are enabled on the device.
     * @param carContext The [CarContext] to access system services.
     * @return `true` if location services are enabled, `false` otherwise.
     */
    override fun isLocationEnabled(carContext: CarContext): Boolean {
        val locationManager = carContext.getSystemService(LocationManager::class.java)
        val isEnabled = locationManager.isLocationEnabled
        Log.d(
            LocationInteractor.Companion.TAG, "isLocationEnabled: $isEnabled"
        )
        return isEnabled
    }

    /**
     * Retrieves the current location as a [Flow] of [Location] objects.
     * @param carContext The [CarContext] to access system services and permissions.
     * @return A [Flow] emitting the current [Location], or `null` if permission is not granted or location cannot be determined.
     */
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(carContext: CarContext): Flow<LocationModel?> = callbackFlow {
        val locationManager = carContext.getSystemService(LocationManager::class.java)
        val hasPermission = permissionsManager.checkLocationPermission(carContext)
        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
            else -> {
                ""
            }
        }
        Log.d(
            LocationInteractor.Companion.TAG,
            "getCurrentLocation hasLocationPermissions: ${hasPermission.first()}"
        )
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d(
                    LocationInteractor.Companion.TAG, "getCurrentLocation: $location"
                )
                trySend(LocationModel.LocationSuccess(location))
                locationManager.removeUpdates(this) // Clean up listener
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (hasPermission.first()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                locationManager.getCurrentLocation(
                    provider, null, carContext.mainExecutor
                ) { location ->
                    Log.d(
                        LocationInteractor.Companion.TAG, "getCurrentLocation: $location"
                    )
                    trySend(LocationModel.LocationSuccess(location))
                }
            } else {
                val location = getLastKnownLocation(carContext, provider)
                location?.let {
                    trySend(LocationModel.LocationSuccess(it))
                } ?: run {
                    trySend(LocationModel.NoLocation)
                }

            }
        } else {
            trySend(LocationModel.NoPermission)
        }
        awaitClose {
            Log.d(
                LocationInteractor.Companion.TAG, "getCurrentLocation close"
            )
            locationManager.removeUpdates(locationListener)
        }
    }

    /**
     * Retrieves the last known location.
     * @param carContext The [CarContext] to access system services.
     * @param provider The location provider (e.g., [LocationManager.GPS_PROVIDER]).
     * @return The last known [Location], or `null` if not available.
     */
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(carContext: CarContext, provider: String): Location? {
        val locationManager = carContext.getSystemService(LocationManager::class.java)
        val location = locationManager.getLastKnownLocation(provider)
        Log.d(
            LocationInteractor.TAG, "getLastKnownLocation: $location"
        )
        return location
    }
}

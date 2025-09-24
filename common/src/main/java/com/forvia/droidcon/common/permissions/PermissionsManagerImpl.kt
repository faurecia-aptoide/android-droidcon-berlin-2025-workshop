package com.forvia.droidcon.common.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.car.app.CarContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementation of [PermissionsManager] for handling permission requests in a Car App.
 */
class PermissionsManagerImpl : PermissionsManager {

    /**
     * Checks and requests location permissions (ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION).
     *
     * This function uses a [callbackFlow] to emit the permission status.
     * It first checks if the permissions are already granted. If not, it requests them.
     * The flow emits `true` if permissions are granted (either initially or after request), and `false` otherwise.
     * @param carContext The [CarContext] used to check and request permissions.
     * @return A [Flow] of [Boolean] indicating whether the location permissions are granted.
     */
    override fun checkLocationPermission(
        carContext: CarContext,
    ): Flow<Boolean> = callbackFlow {
        if (carContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            carContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            carContext.requestPermissions(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            ) { grantedPermissions, rejectedPermissions ->
                if (Manifest.permission.ACCESS_FINE_LOCATION in grantedPermissions &&
                    Manifest.permission.ACCESS_COARSE_LOCATION in grantedPermissions
                ) {
                    trySend(true)
                } else {
                    trySend(false)
                }
            }
        } else {
            trySend(true)
        }
        awaitClose {
            close()
        }
    }
}

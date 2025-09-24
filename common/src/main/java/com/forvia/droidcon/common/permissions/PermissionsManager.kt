package com.forvia.droidcon.common.permissions

import androidx.car.app.CarContext
import kotlinx.coroutines.flow.Flow

interface PermissionsManager {
    fun checkLocationPermission(
        carContext: CarContext,
    ): Flow<Boolean>
}
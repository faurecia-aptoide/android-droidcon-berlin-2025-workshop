package com.forvia.droidcon.common.location.model

import android.location.Location

sealed interface LocationModel {
    data class LocationSuccess(val location: Location) : LocationModel
    object NoLocation : LocationModel
    object NoPermission : LocationModel
}
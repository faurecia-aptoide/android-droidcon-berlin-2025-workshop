package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions

import androidx.car.app.CarContext

sealed interface MainAction {
    data class FetchList(val carContext: CarContext) : MainAction
}

package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation

import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.PoiListScreen

class PoiAppService : CarAppService() {

    override fun createHostValidator(): HostValidator = HostValidator.ALLOW_ALL_HOSTS_VALIDATOR

    override fun onCreateSession(): Session {
        return object : Session() {
            override fun onCreateScreen(intent: Intent): Screen {
                return PoiListScreen(
                    carContext,
                )
            }
        }
    }
}

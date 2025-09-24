package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.forvia.droidcon.common.theme.DeveloperWorkshopTheme
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.CarPropertiesScreen

class VehiclePropertiesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeveloperWorkshopTheme {
                CarPropertiesScreen()
            }
        }
    }
}

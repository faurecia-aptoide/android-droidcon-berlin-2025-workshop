package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui

import androidx.compose.runtime.Composable
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.composables.CarDetailsCard

@Composable
fun DetailsScreen(
    carPropertiesDataModel: CarPropertiesDataModel,
) {
    CarDetailsCard(
        carPropertiesDataModel = carPropertiesDataModel
    )
}

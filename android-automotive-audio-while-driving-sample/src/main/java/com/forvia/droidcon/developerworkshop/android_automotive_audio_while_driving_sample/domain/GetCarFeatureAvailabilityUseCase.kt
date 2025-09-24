package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.domain

import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.data.carfeature.CarFeatures
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.data.carfeature.ICarFeature

class GetCarFeatureAvailabilityUseCase(
    private val carFeature: ICarFeature
) {
    operator fun invoke(carFeatures: CarFeatures): Boolean =
        carFeature.isFeatureAvailable(carFeatures)
}

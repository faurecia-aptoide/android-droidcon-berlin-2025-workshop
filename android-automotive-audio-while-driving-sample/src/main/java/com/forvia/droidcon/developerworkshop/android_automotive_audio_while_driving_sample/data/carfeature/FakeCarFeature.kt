package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.data.carfeature

import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.BuildConfig

class FakeCarFeatures : ICarFeature {
    override fun isFeatureAvailable(carFeatures: CarFeatures): Boolean {
        return when (carFeatures) {
            CarFeatures.FEATURE_BACKGROUND_AUDIO_WHILE_DRIVING ->
                BuildConfig.FEATURE_BACKGROUND_AUDIO_WHILE_DRIVING
        }
    }
}

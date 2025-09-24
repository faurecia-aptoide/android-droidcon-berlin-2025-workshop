package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.di

import androidx.media3.common.util.UnstableApi
import com.forvia.droidcon.common.di.CommonModules
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.data.carfeature.FakeCarFeatures
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.data.carfeature.ICarFeature
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.domain.GetCarFeatureAvailabilityUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.domain.GetMediaAssetUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.PlayerViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@UnstableApi
object AudioWhileDrivingModules {
    val dataModule = module {
        singleOf(::FakeCarFeatures) { bind<ICarFeature>() }
    }

    val domainModule = module {
        factory { GetCarFeatureAvailabilityUseCase(get()) }
        factory { GetMediaAssetUseCase(get()) }
    }

    val presentationModule = module {
        viewModelOf(::PlayerViewModel)
    }

    val modules = listOf(
        dataModule,
        domainModule,
        presentationModule
    ) + CommonModules.modules
}

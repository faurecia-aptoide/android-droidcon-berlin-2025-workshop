package com.forvia.droidcon.developerworkshop.di

import androidx.media3.common.util.UnstableApi
import com.forvia.droidcon.common.di.CommonModules
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.di.AudioWhileDrivingModules
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.di.PushNotificationsModule
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.di.CarPropertiesModule
import com.forvia.droidcon.developerworkshop.domain.repository.AppListRepository
import com.forvia.droidcon.developerworkshop.domain.repository.AppListRepositoryImpl
import com.forvia.droidcon.developerworkshop.domain.usecases.GetAppListUseCase
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.MainViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@UnstableApi
object AppModule {
    val dataModule = module {
        singleOf(::AppListRepositoryImpl) { bind<AppListRepository>() }
    }

    val domainModule = module {
        factory { GetAppListUseCase(get()) }
    }

    val presentationModule = module {
        viewModelOf(::MainViewModel)
    }

    private val featureModules = listOf(
        CommonModules.modules,
        AudioWhileDrivingModules.modules,
        PushNotificationsModule.modules,
        CarPropertiesModule.modules,
    ).flatten()

    val modules = listOf(
        dataModule,
        domainModule,
        presentationModule,
    ) + featureModules
}

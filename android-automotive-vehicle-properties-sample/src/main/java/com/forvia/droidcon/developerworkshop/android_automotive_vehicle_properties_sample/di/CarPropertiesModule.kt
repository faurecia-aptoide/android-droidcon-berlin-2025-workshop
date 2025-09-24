package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.di

import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.VehiclePropertyStore
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.repository.VehiclePropertyStoreImpl
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.CarPropertiesRepository
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.repository.CarPropertiesRepositoryImpl
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.ConnectCarUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.DisconnectCarUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.GetCarConnectionStatusUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.usecases.GetCarPropertiesUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.CarPropertiesViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object CarPropertiesModule {
    val dataModule = module {
        singleOf(::VehiclePropertyStoreImpl) { bind<VehiclePropertyStore>() }
        singleOf(::CarPropertiesRepositoryImpl) { bind<CarPropertiesRepository>() }
    }

    val domainModule = module {
        factory { ConnectCarUseCase(get()) }
        factory { DisconnectCarUseCase(get()) }
        factory { GetCarConnectionStatusUseCase(get()) }
        factory { GetCarPropertiesUseCase(get()) }
    }

    val presentationModule = module {
        viewModelOf(::CarPropertiesViewModel)
    }

    val modules = listOf(
        dataModule,
        domainModule,
        presentationModule
    )
}

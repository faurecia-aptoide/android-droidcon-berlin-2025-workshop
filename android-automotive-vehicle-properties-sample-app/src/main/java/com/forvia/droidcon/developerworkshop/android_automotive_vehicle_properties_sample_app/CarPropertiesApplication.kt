package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample_app

import android.app.Application
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.di.CarPropertiesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CarPropertiesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CarPropertiesApplication)
            modules(CarPropertiesModule.modules)
        }
    }
}

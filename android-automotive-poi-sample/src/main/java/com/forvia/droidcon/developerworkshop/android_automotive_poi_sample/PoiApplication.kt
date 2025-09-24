package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample

import android.app.Application
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.di.PoiModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PoiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PoiApplication)
            modules(
                PoiModules.modules,
            )
        }
    }
}

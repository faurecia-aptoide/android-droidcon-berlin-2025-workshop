package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample_app

import android.app.Application
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.di.PushNotificationsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PushSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PushSampleApplication)
            modules(PushNotificationsModule.modules)
        }
    }
}

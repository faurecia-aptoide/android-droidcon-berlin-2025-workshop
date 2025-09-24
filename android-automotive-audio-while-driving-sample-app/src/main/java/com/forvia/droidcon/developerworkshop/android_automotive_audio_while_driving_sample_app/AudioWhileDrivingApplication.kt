package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample_app

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.di.AudioWhileDrivingModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AudioWhileDrivingApplication : Application() {
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AudioWhileDrivingApplication)
            modules(AudioWhileDrivingModules.modules)
        }
    }
}

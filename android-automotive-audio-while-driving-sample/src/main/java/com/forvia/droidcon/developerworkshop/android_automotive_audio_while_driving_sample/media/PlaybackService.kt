package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.media

import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.data.carfeature.CarFeatures
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.domain.GetCarFeatureAvailabilityUseCase
import org.koin.android.ext.android.getKoin

/**
 * Service for managing media playback.
 */
@UnstableApi
class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private val getCarFeatureAvailabilityUseCase: GetCarFeatureAvailabilityUseCase = getKoin().get()

    override fun onCreate() {
        super.onCreate()
        val player = WorkshopPlayer(
            context = this,
            isBackgroundAudioWhileDrivingSupported =
                getCarFeatureAvailabilityUseCase(
                    CarFeatures.FEATURE_BACKGROUND_AUDIO_WHILE_DRIVING
                )
        )
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        if (controllerInfo.isTrusted || controllerInfo.packageName == packageName) {
            return mediaSession
        }
        return null
    }
}

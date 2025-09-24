package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.media

import android.car.Car
import android.car.drivingstate.CarUxRestrictions
import android.car.drivingstate.CarUxRestrictionsManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.media3.common.ForwardingSimpleBasePlayer
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.common.util.concurrent.ListenableFuture

/**
 * A custom [Player] implementation that wraps an [ExoPlayer] instance and handles
 * Car User Experience (UX) restrictions.
 *
 * @param context The application context.
 * @param isBackgroundAudioWhileDrivingSupported Whether background audio is supported while driving.
 */
@UnstableApi
class WorkshopPlayer(
    context: Context,
    private val isBackgroundAudioWhileDrivingSupported: Boolean,
) :
    ForwardingSimpleBasePlayer(ExoPlayer.Builder(context).build()) {

    private var pausedByUxRestrictions = false
    private var shouldPreventPlay = false
    private lateinit var carUxRestrictionsManager: CarUxRestrictionsManager

    init {
        with(context) {
            // Only listen to UX restrictions if the device is running Android Automotive OS
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
                val car = Car.createCar(context)
                carUxRestrictionsManager =
                    car.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE) as CarUxRestrictionsManager

                // Get the initial UX restrictions and update the player state
                shouldPreventPlay = !isBackgroundAudioWhileDrivingSupported &&
                        carUxRestrictionsManager.currentCarUxRestrictions.isRequiresDistractionOptimization
                invalidateState()

                // Register a listener to update the player state as the UX restrictions change
                carUxRestrictionsManager.registerListener { carUxRestrictions: CarUxRestrictions ->
                    shouldPreventPlay = !isBackgroundAudioWhileDrivingSupported &&
                            carUxRestrictions.isRequiresDistractionOptimization

                    if (!shouldPreventPlay && pausedByUxRestrictions) {
                        handleSetPlayWhenReady(true)
                        invalidateState()
                    } else if (shouldPreventPlay && isPlaying) {
                        pausedByUxRestrictions = true
                        handleSetPlayWhenReady(false)
                        invalidateState()
                    }
                }
            }
        }

        addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.contains(EVENT_IS_PLAYING_CHANGED) && isPlaying) {
                    pausedByUxRestrictions = false
                }
            }
        })
    }

    override fun getState(): State {
        val state = super.getState()

        return state.buildUpon()
            .setAvailableCommands(
                state.availableCommands.buildUpon().removeIf(COMMAND_PLAY_PAUSE, shouldPreventPlay)
                    .build()
            ).build()
    }

    override fun handleRelease(): ListenableFuture<*> {
        if (::carUxRestrictionsManager.isInitialized) {
            carUxRestrictionsManager.unregisterListener()
        }
        return super.handleRelease()
    }
}

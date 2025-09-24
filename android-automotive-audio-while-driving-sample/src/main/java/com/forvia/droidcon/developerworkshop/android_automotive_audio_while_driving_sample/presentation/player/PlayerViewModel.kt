package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.domain.GetMediaAssetUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.media.PlaybackService
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.PlayerAction
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the WorkshopPlayer Composable.
 *
 * This class manages the state of the media player, including playback controls, UI state, and interactions with the PlaybackService.
 *
 * @property application The application context.
 * @property getMediaAssetUseCase Use case for retrieving media assets.
 */
@UnstableApi
class PlayerViewModel(
    private val application: Application,
    private val getMediaAssetUseCase: GetMediaAssetUseCase,
) : ViewModel(), Player.Listener {
    private val _player = MutableStateFlow<Player?>(null)

    /** A [MutableStateFlow] representing the current media player instance. */
    val player: MutableStateFlow<Player?> get() = _player

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: MutableStateFlow<PlayerUiState> get() = _uiState

    private var hideControlsJob: Job? = null
    private var playerUiStateUpdateJob: Job? = null

    /**
     * Initializes the ViewModel, setting up the media controller and observing player state changes.
     */
    init {
        viewModelScope.launch {
            _player.onEach { player ->
                playerUiStateUpdateJob?.cancel()

                if (player != null) {
                    initializePlayer(player)

                    playerUiStateUpdateJob = viewModelScope.launch {
                        while (true) {
                            delay(500)
                            _uiState.getAndUpdate {
                                it.withPlayerState(player)
                            }
                        }
                    }
                }
            }.collect()
        }

        val sessionToken = SessionToken(
            application,
            ComponentName(application, PlaybackService::class.java)
        )

        val mediaControllerFuture =
            MediaController.Builder(application, sessionToken).buildAsync()

        mediaControllerFuture.addListener(
            { _player.update { mediaControllerFuture.get() } },
            MoreExecutors.directExecutor()
        )
    }

    /**
     * Handles user actions related to player control.
     *
     * @param action The [PlayerAction] to be performed.
     */
    fun onAction(action: PlayerAction) {
        when (action) {
            PlayerAction.Close -> _player.value?.release()
            PlayerAction.HideControls -> hideControls()
            PlayerAction.PlayPause -> if (uiState.value.isPlaying) pause() else play()
            PlayerAction.SeekBackward -> _player.value?.seekBack()
            PlayerAction.SeekForward -> _player.value?.seekForward()
            is PlayerAction.SeekTo -> seekTo(action.millis)
            PlayerAction.ShowControls -> showControls()
        }
    }

    /**
     * Initializes the media player with the media asset and sets up listeners for player events.
     *
     * @param player The [Player] instance to initialize.
     */
    private fun initializePlayer(player: Player) {
        player.setMediaItem(getMediaAssetUseCase())
        player.prepare()
        player.playWhenReady = true

        player.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                if (events.contains(Player.EVENT_MEDIA_METADATA_CHANGED)
                    || events.contains(Player.EVENT_IS_LOADING_CHANGED)
                    || events.contains(Player.EVENT_IS_PLAYING_CHANGED)
                ) {
                    _uiState.getAndUpdate {
                        it.withPlayerState(player)
                    }
                }
            }
        })
    }

    /**
     * Starts or resumes playback and hides the controls.
     */
    private fun play() {
        _player.value?.play()
        hideControls()
    }

    /**
     * Pauses playback.
     */
    private fun pause() {
        _player.value?.pause()
    }

    /**
     * Seeks to a specific position in the media.
     *
     * @param millis The position to seek to, in milliseconds.
     */
    private fun seekTo(millis: Long) {
        _player.value?.seekForward()
        _player.value?.seekTo(millis)
    }

    /**
     * Shows the player controls and schedules them to be hidden after a delay.
     */
    private fun showControls() {
        _uiState.getAndUpdate { it.copy(isShowingControls = true) }
        hideControlsJob = viewModelScope.launch {
            delay(5000)
            _uiState.getAndUpdate { it.copy(isShowingControls = false) }
        }
    }

    /**
     * Hides the player controls immediately.
     */
    private fun hideControls() {
        hideControlsJob?.cancel()
        _uiState.getAndUpdate { it.copy(isShowingControls = false) }
    }

    /**
     * Called when the ViewModel is about to be destroyed. Releases the player resources.
     */
    override fun onCleared() {
        super.onCleared()
        _player.value?.apply {
            pause()
            release()
        }
    }
}

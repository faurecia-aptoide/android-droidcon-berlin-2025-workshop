package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player

import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player

data class PlayerUiState(
    val isShowingControls: Boolean = false,
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val durationMillis: Long = -1,
    val currentPositionMillis: Long = -1,
    val mediaMetadata: MediaMetadata = MediaMetadata.Builder().build()
) {
    fun withPlayerState(player: Player): PlayerUiState {
        return copy(
            isLoading = player.isLoading,
            isPlaying = player.isPlaying,
            durationMillis = player.duration,
            currentPositionMillis = player.currentPosition,
            mediaMetadata = player.mediaMetadata
        )
    }
}

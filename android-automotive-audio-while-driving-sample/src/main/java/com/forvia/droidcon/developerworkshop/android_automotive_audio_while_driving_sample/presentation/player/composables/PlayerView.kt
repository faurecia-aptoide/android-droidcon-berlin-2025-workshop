package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView

@Composable
fun PlayerView(
    player: Player,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = {
            PlayerView(it).apply {
                this.player = player
                useController = false
            }
        },
        modifier = modifier
    )
}

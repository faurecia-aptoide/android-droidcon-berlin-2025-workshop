package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.PlayerAction
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.composables.PlayerControls
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.composables.PlayerView
import org.koin.androidx.compose.koinViewModel

@UnstableApi
@Composable
fun PlayerScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = koinViewModel()
) {

    val player by viewModel.player.collectAsStateWithLifecycle()
    val playerUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        player?.let {
            PlayerView(
                it,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (playerUiState.isShowingControls) {
                            viewModel.onAction(PlayerAction.HideControls)
                        } else {
                            viewModel.onAction(PlayerAction.ShowControls)
                        }
                    }
            )

            PlayerControls(
                modifier = Modifier
                    .fillMaxSize(),
                uiState = playerUiState,
                onPlayerAction = { action ->
                    viewModel.onAction(action)
                    if (action is PlayerAction.Close) {
                        onClose()
                    }
                }
            )
        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Player is not available or was not initialized.",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

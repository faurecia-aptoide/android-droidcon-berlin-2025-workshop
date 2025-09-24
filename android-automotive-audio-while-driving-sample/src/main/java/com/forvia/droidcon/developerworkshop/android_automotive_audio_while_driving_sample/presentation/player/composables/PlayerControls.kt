package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.composables

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.PauseCircle
import androidx.compose.material.icons.twotone.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.PlayerAction
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.PlayerUiState

@Composable
fun PlayerControls(
    uiState: PlayerUiState,
    onPlayerAction: (action: PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = uiState.isShowingControls,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = modifier.background(Color.Black.copy(alpha = .5f))) {
            TopControls(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.TopCenter),
                title = uiState.mediaMetadata.title?.toString(),
                onPlayerAction = onPlayerAction
            )

            CenterControls(
                modifier = Modifier
                    .align(Alignment.Center),
                isPlaying = uiState.isPlaying,
                onPlayerAction = onPlayerAction,
            )

            BottomControls(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.BottomCenter),
                durationMillis = uiState.durationMillis,
                currentPositionMillis = uiState.currentPositionMillis,
                onPlayerAction = onPlayerAction
            )
        }
    }
}


@Composable
private fun TopControls(
    title: String?,
    onPlayerAction: (action: PlayerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(48.dp),
            onClick = {
                onPlayerAction(PlayerAction.Close)
            }
        ) {
            Icon(
                Icons.TwoTone.Close,
                contentDescription = "Close player",
                tint = Color.White
            )
        }

        if (title != null) {
            Text(
                title,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun CenterControls(
    isPlaying: Boolean,
    onPlayerAction: (action: PlayerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButton(
            modifier = Modifier
                .size(64.dp)
                .weight(0.33f),
            onClick = {
                onPlayerAction(PlayerAction.SeekBackward)
            }
        ) {
            Icon(
                Icons.Default.KeyboardDoubleArrowLeft,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Toggle play/pause",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            modifier = Modifier
                .size(64.dp)
                .weight(0.33f),
            onClick = {
                onPlayerAction(PlayerAction.PlayPause)
            }
        ) {
            Icon(
                if (isPlaying) Icons.TwoTone.PauseCircle else Icons.TwoTone.PlayCircle,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Toggle play/pause",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            modifier = Modifier
                .size(64.dp)
                .weight(0.33f),
            onClick = {
                onPlayerAction(PlayerAction.SeekForward)
            }
        ) {
            Icon(
                Icons.Default.KeyboardDoubleArrowRight,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Seek forward 10",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun BottomControls(
    durationMillis: Long,
    currentPositionMillis: Long,
    onPlayerAction: (action: PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        if (durationMillis > 0) {
            var desiredSeekMillis by remember { mutableFloatStateOf(Float.NaN) }

            Slider(
                value = if (!desiredSeekMillis.isNaN()) desiredSeekMillis else currentPositionMillis.toFloat(),
                onValueChange = { desiredSeekMillis = it },
                onValueChangeFinished = {
                    onPlayerAction(PlayerAction.SeekTo(desiredSeekMillis.toLong()))
                    desiredSeekMillis = Float.NaN
                },
                valueRange = 0f..durationMillis.toFloat(),
            )
            Text(
                text = "${
                    DateUtils.formatElapsedTime(currentPositionMillis / 1000)
                } / ${
                    DateUtils.formatElapsedTime(durationMillis / 1000)
                }",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

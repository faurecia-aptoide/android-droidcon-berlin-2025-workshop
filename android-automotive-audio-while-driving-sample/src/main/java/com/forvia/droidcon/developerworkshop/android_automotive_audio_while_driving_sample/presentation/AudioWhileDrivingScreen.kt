package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.R

@Composable
fun AudioWhileDrivingScreen(
    onPlayButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Thumbnail(
            onPlayButtonClicked = onPlayButtonClicked,
            modifier = modifier
        )

        Description(
            "This is a simple example of how a video app would behave in an automotive environment",
        )
    }
}

@Composable
fun Thumbnail(
    onPlayButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = LocalContext.current.getString(R.string.video_title)

    Box {
        Image(
            painterResource(R.drawable.sample_thumbnail),
            modifier = Modifier
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Inside,
            contentDescription = contentDescription
        )

        FilledIconButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = onPlayButtonClicked
        ) {
            Icon(
                Icons.TwoTone.PlayArrow,
                contentDescription = "Play"
            )
        }
    }
}

@Composable
fun Description(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    AudioWhileDrivingScreen(
        onPlayButtonClicked = {}
    )
}

package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.media3.common.util.UnstableApi
import com.forvia.droidcon.common.theme.DeveloperWorkshopTheme
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.navigation.AudioWhileDrivingNavigationGraph

@UnstableApi
class VideoToAudioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DeveloperWorkshopTheme {
                AudioWhileDrivingNavigationGraph()
            }
        }
    }
}

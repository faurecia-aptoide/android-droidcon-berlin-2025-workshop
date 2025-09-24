package com.forvia.droidcon.developerworkshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.forvia.droidcon.common.theme.DeveloperWorkshopTheme
import com.forvia.droidcon.developerworkshop.presentation.navigation.MainNavigationGraph

class MainActivity : ComponentActivity() {

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeveloperWorkshopTheme {
                MainNavigationGraph()
            }
        }
    }
}

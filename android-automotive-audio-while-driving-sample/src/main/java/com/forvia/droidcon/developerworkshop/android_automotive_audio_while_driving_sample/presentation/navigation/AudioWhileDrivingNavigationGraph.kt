package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.navigation

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forvia.droidcon.common.ui.ImmersiveModeController
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.AudioWhileDrivingScreen
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.navigation.routes.AudioWhileDrivingNavigationRoutes
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.player.PlayerScreen
import org.koin.compose.getKoin

/**
 * Created by Nuno Palma on 09/08/2023.
 */

@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun AudioWhileDrivingNavigationGraph(
    immersiveModeController: ImmersiveModeController = getKoin().get()
) {
    val navigationController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity
    val view = LocalView.current
    Scaffold { padding ->
        NavHost(
            navController = navigationController,
            startDestination = AudioWhileDrivingNavigationRoutes.MainScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(AudioWhileDrivingNavigationRoutes.MainScreen.route) {
                AudioWhileDrivingScreen(
                    onPlayButtonClicked = {
                        immersiveModeController.toggleImmersiveMode(activity, view)
                        navigationController.navigate(AudioWhileDrivingNavigationRoutes.PlayerScreen.route)
                    },
                )
            }

            composable(AudioWhileDrivingNavigationRoutes.PlayerScreen.route) {
                PlayerScreen(
                    onClose = {
                        immersiveModeController.toggleImmersiveMode(activity, view)
                        navigationController.popBackStack()
                    }
                )
            }
        }
    }
}

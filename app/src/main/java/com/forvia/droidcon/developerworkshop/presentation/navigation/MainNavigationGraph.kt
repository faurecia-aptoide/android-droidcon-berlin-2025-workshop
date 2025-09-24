package com.forvia.droidcon.developerworkshop.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.forvia.droidcon.common.composables.InfoDialog
import com.forvia.droidcon.common.composables.ShowDialogData
import com.forvia.droidcon.common.ui.ImmersiveModeController
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.navigation.AudioWhileDrivingNavigationGraph
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation.PushNotificationsNavigationGraph
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.CarPropertiesScreen
import com.forvia.droidcon.developerworkshop.presentation.composables.MainTopBar
import com.forvia.droidcon.developerworkshop.presentation.composables.SecondaryTopBar
import com.forvia.droidcon.developerworkshop.presentation.mainscreen.MainScreen
import com.forvia.droidcon.developerworkshop.presentation.navigation.routes.MainNavigationRoutes
import org.koin.compose.getKoin

/**
 * Created by Nuno Palma on 09/08/2023.
 */

@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun MainNavigationGraph(
    immersiveModeController: ImmersiveModeController = getKoin().get()
) {
    val navigationController = rememberNavController()
    val currentDestination by navigationController.currentBackStackEntryAsState()
    val showDialog = remember { mutableStateOf(ShowDialogData()) }

    Scaffold(
        topBar = {
            if (!immersiveModeController.immersiveModeEnabled) {
                if (currentDestination?.destination?.route != MainNavigationRoutes.MainScreen.route) {
                    SecondaryTopBar(
                        navController = navigationController,
                        currentDestination = currentDestination?.destination?.route.toString()
                    )
                } else {
                    MainTopBar(
                        showDialogState = showDialog
                    )
                }
            }
        }
    ) { padding ->
        InfoDialog(
            showDialogState = showDialog,
        )
        NavHost(
            navController = navigationController,
            startDestination = MainNavigationRoutes.MainScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(MainNavigationRoutes.MainScreen.route) {
                MainScreen(
                    navigationController = navigationController,
                    showDialogState = showDialog
                )
            }

            composable(MainNavigationRoutes.AudioWhileDrivingSampleGraph.route) {
                AudioWhileDrivingNavigationGraph()
            }

            composable(
                MainNavigationRoutes.PushNotificationsNavigationSampleGraph.route
            ) {
                PushNotificationsNavigationGraph()
            }

            composable(MainNavigationRoutes.CarPropertiesScreen.route) {
                CarPropertiesScreen()
            }
        }
    }
}

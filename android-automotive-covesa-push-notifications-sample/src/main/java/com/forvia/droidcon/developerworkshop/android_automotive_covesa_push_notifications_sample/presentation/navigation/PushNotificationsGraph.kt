package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.model.SetupState
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation.routes.PushNotificationsNavigationRoutes
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.permissions.NotificationPermissionHandler
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.composables.NotificationPermissionGate
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.MainScreen
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.SetupScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PushNotificationsNavigationGraph(
    vm: NavigationViewModel = koinViewModel(),
    notificationHandler: NotificationPermissionHandler = getKoin().get(),
) {
    var permissionGranted by remember { mutableStateOf(false) }
    val navigationController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val setupComplete = vm.isSetupComplete.collectAsStateWithLifecycle(false).value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        NavHost(
            navController = navigationController,
            startDestination = if (permissionGranted && setupComplete == SetupState.COMPLETE) PushNotificationsNavigationRoutes.MainScreen.route else PushNotificationsNavigationRoutes.SetupScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(PushNotificationsNavigationRoutes.SetupScreen.route) {

                NotificationPermissionGate(
                    notificationHandler = notificationHandler,
                    onGranted = { permissionGranted = true },
                    autoRequestOnStart = true
                )

                if (permissionGranted) {
                    SetupScreen(paddingValues = innerPadding) {
                        navigationController.navigate(PushNotificationsNavigationRoutes.MainScreen.route)
                    }
                }
            }

            composable(PushNotificationsNavigationRoutes.MainScreen.route) {
                MainScreen(
                    snackBarHostState = snackBarHostState,
                )
            }
        }
    }
}

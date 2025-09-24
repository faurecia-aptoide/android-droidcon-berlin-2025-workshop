package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation.routes

sealed class PushNotificationsNavigationRoutes(val route: String) {
    object MainScreen : PushNotificationsNavigationRoutes("main_screen")
    object SetupScreen : PushNotificationsNavigationRoutes("setup_screen")
}

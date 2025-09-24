package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.presentation.navigation.routes

sealed class AudioWhileDrivingNavigationRoutes(
    val route: String
) {
    data object MainScreen : AudioWhileDrivingNavigationRoutes("main_screen")
    data object PlayerScreen : AudioWhileDrivingNavigationRoutes("player_screen")
}

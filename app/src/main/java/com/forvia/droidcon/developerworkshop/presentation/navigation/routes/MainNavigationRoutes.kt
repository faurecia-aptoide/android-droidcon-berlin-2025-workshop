package com.forvia.droidcon.developerworkshop.presentation.navigation.routes

sealed class MainNavigationRoutes(
    val route: String,
) {
    data object MainScreen : MainNavigationRoutes(
        route = "Main Screen",
    )

    data object AudioWhileDrivingSampleGraph : MainNavigationRoutes(
        route = "Audio While Driving",
    )

    data object PushNotificationsNavigationSampleGraph : MainNavigationRoutes(
        route = "Push Notifications",
    )

    data object CarPropertiesScreen : MainNavigationRoutes(
        route = "Car Properties",
    )
}

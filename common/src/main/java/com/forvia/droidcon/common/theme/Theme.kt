package com.forvia.droidcon.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MainColorScheme = lightColorScheme(
    primary = DarkColor.Primary,
    secondary = DarkColor.Secondary,
    tertiary = DarkColor.Tertiary,
    surface = DarkColor.Secondary,
    surfaceVariant = DarkColor.Primary,
    surfaceContainerLow = DarkColor.Primary,
)

@Composable
fun DeveloperWorkshopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = MainColorScheme,
        typography = Typography,
        content = content
    )
}
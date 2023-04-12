package com.bradlaronde.smartship.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColors = darkColors(
    primary = Purple500, primaryVariant = Purple900, secondary = Teal900
)

private val LightColors = lightColors(
    primary = Purple500, primaryVariant = Purple700, secondary = Teal200
)

@Composable
fun ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
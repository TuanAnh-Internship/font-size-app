package com.example.fontsizecontroller.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    secondary = PurpleAccent,
    onSecondary = Color.White,
    tertiary = PurpleSecondary,
    background = BgLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = BadgeTint,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PurpleAccent,
    onPrimary = Color.White,
    secondary = PurpleSecondary,
    onSecondary = Color.White,
    background = NavyDarkBg,
    onBackground = TextDarkPrimary,
    surface = SurfaceDark,
    onSurface = TextDarkPrimary,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = TextDarkSecondary,
    outline = Color(0xFF334155)
)

@Composable
fun FontSizeControllerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
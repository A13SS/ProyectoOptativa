package com.example.f1challenge.ui.themes

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = F1Red,
    onPrimary = Color.White,
    secondary = F1LightGray,
    onSecondary = Color.Black,
    background = F1DarkGray,
    onBackground = Color.White,
    surface = F1DarkGray,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = F1Red,
    onPrimary = Color.White,
    secondary = F1LightGray,
    onSecondary = Color.Black,
    background = F1Background,
    onBackground = Color.Black,
    surface = F1Background,
    onSurface = Color.Black,

)

@Composable
fun F1ChallengeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
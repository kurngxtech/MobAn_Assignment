// ui/theme/Theme.kt

package com.example.d1_jetpackcompose.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.DarkerCardGray
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.DeleteColor
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.HistoryObjects
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightBackground
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightCardColor
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.SecondaryGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.TextColor

private val lightColorScheme = lightColorScheme(
    primary = LightGreen,
    secondary = SecondaryGreen,
    background = LightBackground,
    onBackground = LightCardColor,
    surface = HistoryObjects,
    onSurface = TextColor,
    surfaceVariant = DeleteColor,
    onSurfaceVariant = DarkerCardGray
)

private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    secondary = SecondaryGreen,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun SmartFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    // The new way to control system bars
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set the status bar color to transparent
            window.statusBarColor = Color.Transparent.toArgb()
            // Set the navigation bar color to transparent
            window.navigationBarColor = Color.Transparent.toArgb()

            // Set the content to appear behind the system bars
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Set the system bar icons (clock, battery, etc.) to be dark or light
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SmartFitTypography,
        content = content
    )
}
    
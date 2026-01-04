// ui/theme/Theme.kt

package com.example.d1_jetpackcompose.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.DarkerCardGray
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.DeleteColor
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.HistoryObjects
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightBackground
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightCardColor
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightGray
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.SecondaryGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.TextColor

// =========================================================================
// 1. REUSABLE DEVICE DETECTOR & DIMENSIONS SYSTEM
// =========================================================================

data class AppDimens(
    val isTablet: Boolean,
    val screenPadding: Dp,
    val contentMaxWidthPercent: Float, // 1f untuk HP, 0.6f untuk Tablet (Centered Layout)

    // Typography Scales
    val textSizeSmall: TextUnit,
    val textSizeBody: TextUnit,
    val textSizeTitle: TextUnit,
    val textSizeHeadline: TextUnit,

    // Component Sizes
    val dailyGoalHeight: Dp,
    val tipsCardHeight: Dp,
    val profilePicSize: Dp,
    val iconSizeSmall: Dp,
    val iconSizeMedium: Dp,

    // Spacing
    val paddingSmall: Dp,
    val paddingMedium: Dp
)

// Ukuran untuk HP (Compact - Portrait)
val CompactDimens = AppDimens(
    isTablet = false,
    screenPadding = 20.dp,
    contentMaxWidthPercent = 1f, // Full Width

    textSizeSmall = 12.sp,
    textSizeBody = 14.sp,
    textSizeTitle = 16.sp,
    textSizeHeadline = 24.sp,

    dailyGoalHeight = 110.dp,
    tipsCardHeight = 200.dp,
    profilePicSize = 56.dp,
    iconSizeSmall = 20.dp,
    iconSizeMedium = 40.dp,

    paddingSmall = 8.dp,
    paddingMedium = 16.dp
)

// Ukuran untuk Tablet (Expanded - Landscape)
val TabletDimens = AppDimens(
    isTablet = true,
    screenPadding = 32.dp,
    contentMaxWidthPercent = 0.6f, // 60% Width Centered

    textSizeSmall = 14.sp,
    textSizeBody = 16.sp,
    textSizeTitle = 20.sp,
    textSizeHeadline = 32.sp,

    dailyGoalHeight = 130.dp,
    tipsCardHeight = 240.dp,
    profilePicSize = 72.dp,
    iconSizeSmall = 28.dp,
    iconSizeMedium = 56.dp,

    paddingSmall = 12.dp,
    paddingMedium = 24.dp
)

// CompositionLocal agar bisa diakses di mana saja
val LocalAppDimens = staticCompositionLocalOf { CompactDimens }


// =========================================================================
// 2. EXISTING THEME COLORS (TIDAK DIUBAH)
// =========================================================================

private val lightColorScheme = lightColorScheme(
    primary = LightGreen,
    secondary = SecondaryGreen,
    tertiary = LightGray,
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

    // 💡 DETEKSI RESOLUSI DEVICE (Reusable Logic)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Logika: Jika lebar >= 600dp, gunakan TabletDimens, jika tidak gunakan CompactDimens
    val appDimens = if (screenWidth >= 600.dp) TabletDimens else CompactDimens

    // The new way to control system bars
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    // 💡 PROVIDE DIMENSI KE SELURUH APLIKASI
    CompositionLocalProvider(LocalAppDimens provides appDimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SmartFitTypography,
            content = content
        )
    }
}
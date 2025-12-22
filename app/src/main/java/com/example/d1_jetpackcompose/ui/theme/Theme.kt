// ui/theme/Theme.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme // <-- Kontrak Google
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.DeleteColor
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.HistoryObjects
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightBackground
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightCardColor
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.SecondaryGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.TextColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val lightColorScheme = lightColorScheme(
    primary = LightGreen,
    secondary = SecondaryGreen,
    background = LightBackground,
    onBackground = LightCardColor,
    surface = HistoryObjects,
    onSurface = TextColor,
    surfaceVariant = DeleteColor
)

private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // Versi terang dari PrimaryGreen untuk latar belakang gelap
    onPrimary = Color.Black,
    secondary = SecondaryGreen,
    background = Color(0xFF121212), // Sangat gelap
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun SmartFitTheme(
    // Tentukan apakah sistem sedang menggunakan Dark Mode (Wajib untuk assignment)
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Content adalah Composable yang akan dibungkus (seluruh aplikasi)
    content: @Composable () -> Unit
) {
    // 1. Pilih skema warna yang benar berdasarkan darkTheme
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )
    }

    // 2. Terapkan tema
    MaterialTheme(
        colorScheme = colorScheme, // <-- Gunakan variabel colorScheme yang sudah dipilih
        typography = SmartFitTypography, // dari Type.kt
        content = content
    )
}



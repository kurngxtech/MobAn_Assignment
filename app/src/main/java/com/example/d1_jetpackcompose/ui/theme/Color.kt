// ui/theme/Color.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.MainGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.SecondaryGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.WhiteFog

// Warna Utama (Custom SmartFit)
object SmartFitColors {
    // Kategori: Warna Dasar & Netral
    val WhiteFog = Color(0xFFF5F5F5)
    val GrayRoad = Color(0xFF757575)
    val PureBlack = Color(0xFF000000)
    val PureWhite = Color(0xFFFFFFFF)
    val SecondaryWhite = Color(0xFFE0E5E0)

    // Kategori: Warna Status & Notifikasi
    val ErrorRed = Color(0xFFB00020)
    val SuccessGreen = Color(0xFF00C853)
    val WarningOrange = Color(0xFFFFA000)

    val TextPrimary = Color.Black
    val TextSecondary = GrayRoad
    val MainGreen = Color(0xFF4F6A4E) // Warna biru (mengganti #4f6af4e menjadi Color)
    val SecondaryGreen = Color(0xFF2f402f)
    val LightGreen = Color(0xFF60975E)
}

// Skema Warna Terang (Light Theme)
val LightColorScheme = lightColorScheme(
    primary = MainGreen,
    onPrimary = Color.White,
    secondary = SecondaryGreen,
    background = WhiteFog,
    surface = WhiteFog,
    onSurface = Color.Black
    // ... tambahkan peran warna Material lainnya
)

// Skema Warna Gelap (Dark Theme)
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // Versi terang dari PrimaryGreen untuk latar belakang gelap
    onPrimary = Color.Black,
    secondary = SecondaryGreen,
    background = Color(0xFF121212), // Sangat gelap
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)
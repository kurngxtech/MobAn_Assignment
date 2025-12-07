// ui/theme/Color.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Warna Utama (Custom SmartFit)
val WhiteFog = Color(0xFFF5F5F5)        // Putih Kabut Pegunungan
val GrayRoad = Color(0xFF757575) // Abu-abu Jalan Raya

val DefaultButtonBackgroundColor = Color(0xFF4F6A4E) // Warna biru (mengganti #4f6af4e menjadi Color)
val PressedButtonBackgroundColor = Color(0xFF2f402f)

// Skema Warna Terang (Light Theme)
val LightColorScheme = lightColorScheme(
    primary = DefaultButtonBackgroundColor,
    onPrimary = Color.White,
    secondary = PressedButtonBackgroundColor,
    background = WhiteFog,
    surface = WhiteFog,
    onSurface = Color.Black
    // ... tambahkan peran warna Material lainnya
)

// Skema Warna Gelap (Dark Theme)
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // Versi terang dari PrimaryGreen untuk latar belakang gelap
    onPrimary = Color.Black,
    secondary = PressedButtonBackgroundColor,
    background = Color(0xFF121212), // Sangat gelap
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)
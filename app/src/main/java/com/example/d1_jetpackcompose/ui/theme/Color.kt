// ui/theme/Color.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Warna Utama (Custom SmartFit)
val PrimaryGreen = Color(0xFF4CAF50)    // Hijau Daun
val DarkGreen = Color(0xFF1B5E20)       // Hijau Tua Rumput
val WhiteFog = Color(0xFFF5F5F5)        // Putih Kabut Pegunungan
val GrayRoad = Color(0xFF757575)        // Abu-abu Jalan Raya

// Skema Warna Terang (Light Theme)
val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    secondary = DarkGreen,
    background = WhiteFog,
    surface = WhiteFog,
    onSurface = Color.Black
    // ... tambahkan peran warna Material lainnya
)

// Skema Warna Gelap (Dark Theme)
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // Versi terang dari PrimaryGreen untuk latar belakang gelap
    onPrimary = Color.Black,
    secondary = DarkGreen,
    background = Color(0xFF121212), // Sangat gelap
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)
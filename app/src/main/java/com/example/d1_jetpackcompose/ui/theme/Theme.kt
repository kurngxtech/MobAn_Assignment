// ui/theme/Theme.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme // <-- Kontrak Google

@Composable
fun SmartFitTheme(
    // Tentukan apakah sistem sedang menggunakan Dark Mode (Wajib untuk assignment)
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Content adalah Composable yang akan dibungkus (seluruh aplikasi)
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 💡 INI ADALAH TITIK UTAMA INJEKSI TEMA
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SmartFitTypography, // dari Type.kt
        // shapes = SmartFitShapes,       // jika kamu membuat Shape.kt
        content = content // Membungkus NavGraph dan seluruh aplikasi
    )
}
// ui/theme/Theme.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme // <-- Kontrak Google
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SmartFitTheme(
    // Tentukan apakah sistem sedang menggunakan Dark Mode (Wajib untuk assignment)
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Content adalah Composable yang akan dibungkus (seluruh aplikasi)
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent, // Buat warna bar menjadi transparan
            darkIcons = !darkTheme      // Gunakan ikon terang jika tema gelap, dan sebaliknya
        )
    }

    // 💡 INI ADALAH TITIK UTAMA INJEKSI TEMA
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SmartFitTypography, // dari Type.kt
        content = content // Membungkus NavGraph dan seluruh aplikasi
    )
}
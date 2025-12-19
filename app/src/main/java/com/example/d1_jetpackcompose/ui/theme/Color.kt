// ui/theme/Color.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.HistoryObjects
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightBackground
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.LightGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.SecondaryGreen
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors.TextColor

// Warna Utama (Custom SmartFit)
object SmartFitColors {
    val SecondaryGreen = Color(0xFF2f402f)
    val LightGreen = Color(0xFF8AAE89)
    val LightBackground = Color(0xFFF2F0EF)
    val HistoryObjects = Color(0xFFECECEC)
    val TextColor = Color(0xFF4F6A4E)
    val LightCardColor = Color(0xFFFDFFFD)


    val SecondaryWhite = Color(0xFFc9d4c9)

    // Kategori: Warna Status & Notifikasi
    val ErrorRed = Color(0xFFB00020)
    val SuccessGreen = Color(0xFF00C853)
    val WarningOrange = Color(0xFFFFA000)
    val DarkGreen = Color(0xFF4F6A4E) // Warna biru (mengganti #4f6af4e menjadi Color)
    val SmartFitLightGray = Color(0xFFD9E4E1) // Untuk tombol Add
    val SmartFitGreen = Color(0xFF4F6A4E)    // Warna Utama
    val SmartFitDarkGray = Color(0xFF1e1e1f)  // Untuk background bar/card
    val SegmentedActiveGray = Color(0xFF434346) // Abu-abu tombol aktif
}

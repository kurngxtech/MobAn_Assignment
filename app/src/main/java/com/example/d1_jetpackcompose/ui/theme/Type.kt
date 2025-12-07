// ui/theme/Type.kt

package com.example.d1_jetpackcompose.ui.theme

// Import yang hilang DITAMBAHKAN di sini:
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle // <-- INI YANG HILANG!
import androidx.compose.ui.text.font.FontFamily // <-- INI YANG HILANG!
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp // <-- Perlu untuk ukuran font (sp)

// 💡 PASTIKAN kamu membuat file ini (atau gunakan Alt+Enter)

val SmartFitTypography = Typography(
    // Contoh: Customisasi style Judul/Headline
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    // Contoh: Customisasi style Body Text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    // Kamu bisa tambahkan style lain (misalnya titleMedium, labelSmall, dll.)
)
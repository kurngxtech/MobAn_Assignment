// File: ui/theme/Type.kt

package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.d1_jetpackcompose.R // Pastikan import R benar

// source font family
val outfitFontFamily = FontFamily(
    Font(R.font.outfit_regular)
)
val robotoFontFamily = FontFamily(
    Font(R.font.roboto_regular)
)
val robotoFontFamilyItalic = FontFamily(
    Font(R.font.roboto_italic)
)

val SmartFitTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = robotoFontFamily, // Sekarang ini akan menunjuk ke font yang benar
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = robotoFontFamily, // Sekarang ini akan menunjuk ke font yang benar
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = robotoFontFamily, // Gunakan juga untuk body, misalnya
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    )
)

val buttonLoginStyle = TextStyle (
    fontFamily = robotoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)

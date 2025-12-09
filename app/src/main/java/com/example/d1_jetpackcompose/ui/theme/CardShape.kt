package com.example.d1_jetpackcompose.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 1. Buat fungsi helper untuk memanggil shape custom
// Kamu bisa ganti RoundedCornerShape dengan CutCornerShape atau lainnya jika mau ganti gaya
fun smartFitShape(size: Dp): RoundedCornerShape {
    return RoundedCornerShape(size)
}

// 2. Opsional: Kamu bisa simpan ukuran default di sini juga
val DefaultCardSize = 16.dp
val LargeCardSize = 32.dp
package com.example.d1_jetpackcompose.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors
import com.example.d1_jetpackcompose.ui.theme.buttonLoginStyle
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme


/**
 * Komponen Composable untuk tombol Start Now.
 *  */
@Composable
fun RoundedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Buat InteractionSource untuk melacak keadaan tombol (ditekan atau tidak)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 2. Tentukan warna background berdasarkan keadaan isPressed
    val targetBackgroundColor = if (isPressed) {
        SmartFitColors.SecondaryGreen
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    // Gunakan animateColorAsState untuk transisi warna yang halus (opsional, tapi disarankan)
    val backgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor,
        label = "backgroundColorAnimation"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth() // Mengisi lebar penuh
            .height(48.dp), // Tinggi standar atau sesuai kebutuhan
        shape = RoundedCornerShape(50), // Bentuk bulat sempurna
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, // Gunakan warna yang sudah dianimasikan/ditentukan
            contentColor = Color.White // Warna teks
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        interactionSource = interactionSource // Terapkan InteractionSource
    ) {
        Text(
            text = "Start Now",
            style = buttonLoginStyle
        )
    }
}
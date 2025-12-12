package com.example.d1_jetpackcompose.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors
import com.example.d1_jetpackcompose.ui.theme.buttonLoginStyle
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily


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
        SmartFitColors.MainGreen
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

@Composable
fun GeneralButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetBackgroundColor = if (isPressed) {
        SmartFitColors.SecondaryGreen
    } else {
        SmartFitColors.MainGreen
    }

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
        interactionSource = interactionSource, // Terapkan InteractionSource
    ) {
        Text(
            text = text,
            style = buttonLoginStyle,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DropDownButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color,
    text: String,
    fontFamily: FontFamily
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetBackgroundColor = if (isPressed) {
        SmartFitColors.SecondaryGreen
    } else {
        SmartFitColors.MainGreen
    }

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
            containerColor = color, // Gunakan warna yang sudah dianimasikan/ditentukan
            contentColor = SmartFitColors.MainGreen // Warna teks
        ),
        interactionSource = interactionSource, // Terapkan InteractionSource
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = buttonLoginStyle,
                color = SmartFitColors.MainGreen,
                fontWeight = FontWeight.Bold
            )

            Image (
                painter = painterResource(R.drawable.dropdown_arrow),
                contentDescription = "Dropdown Arrow",
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun HorizontalLineProfile(modifier: Modifier = Modifier) {
    val lineGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color.Transparent,                      // Mulai dari transparan (ujung kiri)
            SmartFitColors.MainGreen.copy(alpha = 0.5f), // Cepat menjadi warna utama
            SmartFitColors.MainGreen.copy(alpha = 0.5f), // Tetap warna utama di tengah
            Color.Transparent                       // Memudar menjadi transparan (ujung kanan)
        ),
        // Anda bisa menyesuaikan posisi "stops" untuk kontrol lebih
        // Contoh:
        // startX = 0.0f,
        // endX = Float.POSITIVE_INFINITY
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButton() {
    // Contoh penggunaan dalam Preview
//    DropDownButton(
//        onClick = {},
//        modifier = Modifier,
//        color = SmartFitColors.PureBlack,
//        text = "This Week",
//        fontFamily = robotoFontFamily
//    )
//    GeneralButton(
//        onClick = {},
//        modifier = Modifier,
//        text = "Start Now"
//
//    )
    HorizontalLineProfile()
}
package com.example.d1_jetpackcompose.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.screens.colorEnd
import com.example.d1_jetpackcompose.ui.screens.colorStartDark
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors
import com.example.d1_jetpackcompose.ui.theme.buttonLoginStyle
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter


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
fun TimeProgressButtons(
    modifier: Modifier = Modifier
) {
// State untuk menyimpan index yang terpilih (Default "D" = index 0)
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("D", "W", "M", "Y")

    // Container Utama (Bar Card)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(35.dp) // Tinggi disesuaikan dengan proporsi gambar
            .clip(RoundedCornerShape(9.dp)) // Corner radius mengikuti bar card di gambar
            .background(Color.Black.copy(alpha = 0.6f)) // Ketentuan 1: Pure Black Opacity 0.6f
            .padding(4.dp) // Gap antara border bar dengan background tombol aktif
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, text ->
                val isSelected = selectedIndex == index

                // Animasi perubahan warna background (opsional untuk kehalusan)
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) Color(0xFF4F6A4E) else Color.Transparent,
                    label = "bgAnimation"
                )

                // Layout Tombol Individu
                Box(
                    modifier = Modifier
                        .weight(1f) // Membagi panjang secara merata untuk 4 tombol
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(5.dp)) // Radius tombol aktif sesuai gambar
                        .background(backgroundColor) // Ketentuan 3 & 4: Warna Aktif #4F6A4E 100%
                        .clickable { selectedIndex = index }, // Ketentuan 3: Clickable
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = Color.White, // Ketentuan 1 & 3: Text Color Putih
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold // Analisis: Font tebal agar terbaca jelas
                    )
                }

                // Menambahkan Vertical Divider (Garis pemisah antar tombol)
                // Analisis: Hanya muncul di antara tombol yang TIDAK aktif (W|M dan M|Y)
                if (index < options.size - 1) {
                    // Garis hanya muncul jika tombol saat ini dan tombol setelahnya TIDAK terpilih
                    // agar tidak memotong background hijau tombol aktif
                    val showDivider = !isSelected && selectedIndex != index + 1

                    if (showDivider) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(24.dp) // Panjang garis pemisah sesuai analisis gambar
                                .background(Color.White.copy(alpha = 0.2f)) // Warna garis halus
                        )
                    } else {
                        // Spacer tetap ada agar lebar tombol tidak berubah saat garis hilang
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                }
            }
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

@Composable
fun BubbleNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Di sinilah Anda mendesain navigasi "mengambang" Anda.
    // Kita gunakan Card untuk mendapatkan efek bubble/mengambang.
    Card(
        shape = MaterialTheme.shapes.extraLarge, // Bentuk pil atau sangat bulat
        colors = CardDefaults.cardColors(
            containerColor = colorStartDark.copy(alpha = 0.7f),
            contentColor = colorEnd
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(50.dp)
                    .clickable {
                        navController.navigate(AppRoutes.DASHBOARD) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Image(painter = painterResource(R.drawable.home_icon), contentDescription = "Home")
                Text(
                    text = "Home",
                    fontSize = 12.sp,
                    fontFamily = robotoFontFamily,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .width(50.dp)
                    .clickable{
                        navController.navigate(AppRoutes.ACTIVITY) { // Pastikan AppRoutes.ACTIVITY ada
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.activity_log_logo),
                    contentDescription = "Activity Log"
                )
                Text(
                    text = "Activity",
                    fontSize = 12.sp,
                    fontFamily = robotoFontFamily,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .width(50.dp)
                    .clickable{
                        navController.navigate(AppRoutes.PROFILE) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.settings_icon),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(30.dp),
                    colorFilter = ColorFilter.tint(
                        color = SmartFitColors.MainGreen, // Specify your desired color here
                        blendMode = BlendMode.SrcIn // Use SrcIn for standard icon tinting
                    )
                )
                Text(
                    text = "Settings",
                    fontSize = 12.sp,
                    fontFamily = robotoFontFamily,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButton() {
    SmartFitTheme {
        val navController = rememberNavController()
        // Sekarang teruskan navController palsu tersebut
        BubbleNavigationBar(navController = navController)
    }
}
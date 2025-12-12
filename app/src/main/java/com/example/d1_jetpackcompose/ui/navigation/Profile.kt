package com.example.d1_jetpackcompose.ui.navigation

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.theme.smartFitShape
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ActivityPage()
                }
            }
        }
    }
}

@Composable
fun ActivityPage(modifier: Modifier = Modifier) {
    val gradientDarkMode = Brush.verticalGradient(
        colors = listOf(SmartFitColors.PureBlack, SmartFitColors.MainGreen),
    )
    val gradientLightMode = Brush.verticalGradient(
        colors = listOf(SmartFitColors.PureWhite, SmartFitColors.MainGreen),
    )
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientDarkMode)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column {
                Text(
                    text = "My Profile",
                    fontSize = 52.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = SmartFitColors.MainGreen
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image (
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .offset(y = (0).dp)
                        .clip(CircleShape)
                )

            }

            Spacer(modifier = Modifier.size(20.dp))

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Jamal Runner",
                    fontSize = 24.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = SmartFitColors.MainGreen
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button (
                    onClick = {},
                    modifier = Modifier.height(35.dp)
                ) {
                    Text(
                        text = "Edit Profile",
                        fontFamily = robotoFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Profile Detail",
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(0.6f),
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                Card (
                    shape = smartFitShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorStartDark.copy(alpha = 0.6f),
                        contentColor = colorEnd
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)
                            .padding(horizontal = 30.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .drawBehind {
                                    val strokeWidth = 2.dp.toPx()
                                    // Tentukan posisi Y garis di bagian bawah area Box (setelah padding)
                                    val y = size.height - (strokeWidth / 2)

                                    // Gambar garis dengan BRUSH, bukan color
                                    drawLine(
                                        brush = lineGradientBrush, // <-- Gunakan Brush yang sudah Anda buat
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = strokeWidth
                                    )
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Age",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Text(
                                text = "20",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        Spacer(modifier = Modifier.size(15.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .drawBehind {
                                    val strokeWidth = 2.dp.toPx()
                                    // Tentukan posisi Y garis di bagian bawah area Box (setelah padding)
                                    val y = size.height - (strokeWidth / 2)

                                    // Gambar garis dengan BRUSH, bukan color
                                    drawLine(
                                        brush = lineGradientBrush, // <-- Gunakan Brush yang sudah Anda buat
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = strokeWidth
                                    )
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Gender",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Text(
                                text = "Male",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        Spacer(modifier = Modifier.size(15.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .drawBehind {
                                    val strokeWidth = 2.dp.toPx()
                                    // Tentukan posisi Y garis di bagian bawah area Box (setelah padding)
                                    val y = size.height - (strokeWidth / 2)

                                    // Gambar garis dengan BRUSH, bukan color
                                    drawLine(
                                        brush = lineGradientBrush, // <-- Gunakan Brush yang sudah Anda buat
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = strokeWidth
                                    )
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Height",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Text(
                                text = "177 cm",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        Spacer(modifier = Modifier.size(15.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .drawBehind {
                                    val strokeWidth = 2.dp.toPx()
                                    // Tentukan posisi Y garis di bagian bawah area Box (setelah padding)
                                    val y = size.height - (strokeWidth / 2)

                                    // Gambar garis dengan BRUSH, bukan color
                                    drawLine(
                                        brush = lineGradientBrush, // <-- Gunakan Brush yang sudah Anda buat
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = strokeWidth
                                    )
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Weight",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Text(
                                text = "100 kg",
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(20.dp))
            
            Column (
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Body Mass Index (BMI)",
                        color = Color.White.copy(alpha = 0.5f),
                        fontFamily = robotoFontFamily,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Card (
                        shape = smartFitShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorStartDark.copy(alpha = 0.6f),
                            contentColor = colorEnd
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(vertical = 15.dp)
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box (
                               modifier = Modifier
                                   .size(100.dp)
                                   .clip(CircleShape)
                                   .border (
                                       width = 5.dp,
                                       color = Color.Red,
                                       shape = CircleShape
                                   )

                            ) {

                            }

                            Spacer(modifier = Modifier.size(10.dp))

                            Box (
                               modifier = Modifier
                                   .size(100.dp)
                                   .clip(CircleShape)
                                   .border (
                                       width = 5.dp,
                                       color = Color.Red,
                                       shape = CircleShape
                                   )

                            ) {

                            }

                            Spacer(modifier = Modifier.size(10.dp))

                            Box (
                               modifier = Modifier
                                   .size(100.dp)
                                   .clip(CircleShape)
                                   .border (
                                       width = 5.dp,
                                       color = Color.Red,
                                       shape = CircleShape
                                   )

                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ActivityPagePrev() {
    SmartFitTheme {
        ActivityPage()
    }
}
package com.example.d1_jetpackcompose.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
                    ProfilePage()
                }
            }
        }
    }
}

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
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
                .padding(20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Profile",
                    fontSize = 40.sp,
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
                Image(
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

            Column(
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        // Beri jarak antar Card
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Card Pertama
                        Card(
                            modifier = Modifier
                                .weight(1f) // 1. Ambil 1/3 dari lebar yang tersedia
                                .aspectRatio(1f), // 2. Buat tingginya sama dengan lebarnya (membentuk kotak)
                            shape = smartFitShape(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SmartFitColors.PureBlack.copy(
                                    alpha = 0.6f
                                )
                            )
                        ) {
                            // Isi Card 1 di sini, misalnya dengan Box dan Text
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "177 cm",
                                    color = SmartFitColors.MainGreen,
                                    fontFamily = robotoFontFamily,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.size(3.dp))
                                Text(
                                    text = "Height",
                                    color = SmartFitColors.MainGreen.copy(alpha = 0.5f),
                                    fontFamily = robotoFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Card Kedua
                        Card(
                            modifier = Modifier
                                .weight(1f) // Ambil 1/3 dari lebar yang tersedia
                                .aspectRatio(1f), // Buat tingginya sama dengan lebarnya
                            shape = smartFitShape(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SmartFitColors.PureBlack.copy(
                                    alpha = 0.6f
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "90 kg",
                                    color = SmartFitColors.MainGreen,
                                    fontFamily = robotoFontFamily,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.size(3.dp))
                                Text(
                                    text = "Weight",
                                    color = SmartFitColors.MainGreen.copy(alpha = 0.5f),
                                    fontFamily = robotoFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Card Ketiga
                        Card(
                            modifier = Modifier
                                .weight(1f) // Ambil 1/3 dari lebar yang tersedia
                                .aspectRatio(1f), // Buat tingginya sama dengan lebarnya
                            shape = smartFitShape(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SmartFitColors.PureBlack.copy(
                                    alpha = 0.6f
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "25 y",
                                    color = SmartFitColors.MainGreen,
                                    fontFamily = robotoFontFamily,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.size(3.dp))
                                Text(
                                    text = "Age",
                                    color = SmartFitColors.MainGreen.copy(alpha = 0.5f),
                                    fontFamily = robotoFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(), // Ambil 1/3 dari lebar yang tersedia
                        shape = smartFitShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SmartFitColors.PureBlack.copy(
                                alpha = 0.6f
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Male",
                                color = SmartFitColors.MainGreen,
                                fontFamily = robotoFontFamily,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.size(3.dp))
                            Text(
                                text = "Gender",
                                color = SmartFitColors.MainGreen.copy(alpha = 0.5f),
                                fontFamily = robotoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Body Mass Index (BMI)",
                    color = Color.White.copy(alpha = 0.5f),
                    fontFamily = robotoFontFamily,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.size(10.dp))

                Card(
                    shape = smartFitShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorStartDark.copy(alpha = 0.6f),
                        contentColor = colorEnd
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
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
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 5.dp,
                                    color = Color.Green,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = SmartFitColors.MainGreen
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Ideal",
                                        color = Color.White,
                                        fontFamily = robotoFontFamily,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.size(5.dp))

                                    Text(
                                        text = "18,5 - 24,9",
                                        color = Color.White,
                                        fontFamily = robotoFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            Card (
                modifier = Modifier
                    .fillMaxWidth(),
                shape = smartFitShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SmartFitColors.PureBlack.copy(
                        alpha = 0.6f
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{}
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.user_logo),
                                contentDescription = "right arrow",
                                colorFilter = ColorFilter.tint(
                                    color = SmartFitColors.MainGreen, // Warna yang kamu inginkan
                                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                                ),
                                modifier = Modifier.size(30.dp)
                            )

                            Column (
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth(0.9f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text (
                                    text = "Personal Info",
                                    color = SmartFitColors.MainGreen,
                                    fontFamily = robotoFontFamily,
                                    fontSize = 20.sp
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.right_arrow_icon),
                                contentDescription = "right arrow",
                                colorFilter = ColorFilter.tint(
                                    color = SmartFitColors.MainGreen, // Warna yang kamu inginkan
                                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                                ),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable{}
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.help_logo),
                                contentDescription = "right arrow",
                                colorFilter = ColorFilter.tint(
                                    color = SmartFitColors.MainGreen, // Warna yang kamu inginkan
                                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                                ),
                                modifier = Modifier.size(30.dp)
                            )

                            Column (
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth(0.9f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text (
                                    text = "FAQ",
                                    color = SmartFitColors.MainGreen,
                                    fontFamily = robotoFontFamily,
                                    fontSize = 20.sp
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.right_arrow_icon),
                                contentDescription = "right arrow",
                                colorFilter = ColorFilter.tint(
                                    color = SmartFitColors.MainGreen, // Warna yang kamu inginkan
                                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                                ),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable{}
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logout_logo),
                                contentDescription = "right arrow",
                                colorFilter = ColorFilter.tint(
                                    color = SmartFitColors.MainGreen, // Warna yang kamu inginkan
                                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                                ),
                                modifier = Modifier.size(30.dp)
                            )

                            Column (
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth(0.9f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text (
                                    text = "Log Out",
                                    color = SmartFitColors.MainGreen,
                                    fontFamily = robotoFontFamily,
                                    fontSize = 20.sp
                                )
                            }

                            Image(
                                painter = painterResource(id = R.drawable.right_arrow_icon),
                                contentDescription = "right arrow",
                                colorFilter = ColorFilter.tint(
                                    color = SmartFitColors.MainGreen, // Warna yang kamu inginkan
                                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                                ),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Scrollable Profile",
    heightDp = 1200
)
@Composable
private fun ActivityPagePrev() {
    SmartFitTheme {
        ProfilePage()
    }
}
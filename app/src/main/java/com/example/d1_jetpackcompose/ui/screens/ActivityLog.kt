package com.example.d1_jetpackcompose.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.d1_jetpackcompose.ui.components.TimeProgressButtons
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.theme.smartFitShape
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors

class ActivityLogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ActivityLog()
                }
            }
        }
    }
}

@Composable
fun ActivityLog(modifier: Modifier = Modifier) {
    val gradientDarkMode = Brush.verticalGradient(
        colors = listOf(SmartFitColors.PureBlack, SmartFitColors.MainGreen),
    )
    val gradientLightMode = Brush.verticalGradient(
        colors = listOf(SmartFitColors.PureWhite, SmartFitColors.MainGreen),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientDarkMode)
    ) {
        Image(
            painter = painterResource(R.drawable.darkmode_background),
            contentDescription = "Background Overlay",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.30f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Activity Log",
                    fontSize = 40.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = SmartFitColors.MainGreen
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeProgressButtons(modifier = Modifier)
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                Card(
                    shape = smartFitShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorStartDark.copy(alpha = 0.6f),
                        contentColor = colorEnd
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 15.dp)
                            .padding(vertical = 10.dp)
                    ) {
                        // column untuk bottom border
                        Column(
                            modifier = Modifier
                                .drawBehind {
                                    val strokeWidth =
                                        1.dp.toPx() // Ketebalan garis
                                    val y =
                                        size.height - (strokeWidth / 2) // Posisi Y di bagian paling bawah

                                    // Gambar garis dari kiri ke kanan di posisi Y tersebut
                                    drawLine(
                                        color = colorEnd, // Anda bisa gunakan warna dari theme Anda
                                        start = Offset(
                                            0f,
                                            y
                                        ),
                                        end = Offset(
                                            size.width,
                                            y
                                        ),
                                        strokeWidth = strokeWidth
                                    )
                                }
                        ) {
                            // row untuk judul dan button atas
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.12f)
                                    .padding(bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Your Activity",
                                    fontSize = 24.sp,
                                    fontFamily = robotoFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row {
                                    ButtonActivityLog(
                                        onAddClicked = {},
                                        onEditClicked = {},
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.size(25.dp))
                        // column untuk data log
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            // Column pembungkus border bawah
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        val strokeWidth =
                                            2.dp.toPx() // Ketebalan garis
                                        val y =
                                            size.height - (strokeWidth / 2) // Posisi Y di bagian paling bawah

                                        // Gambar garis dari kiri ke kanan di posisi Y tersebut
                                        drawLine(
                                            color = colorEnd, // Anda bisa gunakan warna dari theme Anda
                                            start = Offset(
                                                0f,
                                                y
                                            ),
                                            end = Offset(
                                                size.width,
                                                y
                                            ),
                                            strokeWidth = strokeWidth
                                        )
                                    }
                            ) {
                                // row untuk isi log dan bin icon
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(bottom = 5.dp)

                                ) {
                                    ActivityLogItemCard(
                                        logTitle = "Today's morning run",
                                        onLogClicked = {},
                                        onDeleteClicked = {},
                                        modifier = Modifier
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.size(10.dp))

                            // Column pembungkus border bawah
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        val strokeWidth =
                                            2.dp.toPx() // Ketebalan garis
                                        val y =
                                            size.height - (strokeWidth / 2) // Posisi Y di bagian paling bawah

                                        // Gambar garis dari kiri ke kanan di posisi Y tersebut
                                        drawLine(
                                            color = colorEnd, // Anda bisa gunakan warna dari theme Anda
                                            start = Offset(
                                                0f,
                                                y
                                            ),
                                            end = Offset(
                                                size.width,
                                                y
                                            ),
                                            strokeWidth = strokeWidth
                                        )
                                    }
                            ) {
                                // row untuk isi log dan bin icon
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(bottom = 5.dp)

                                ) {
                                    ActivityLogItemCard(
                                        logTitle = "Today's morning run",
                                        onLogClicked = {},
                                        onDeleteClicked = {},
                                        modifier = Modifier
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.size(10.dp))
                            // Column pembungkus border bawah
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        val strokeWidth =
                                            2.dp.toPx() // Ketebalan garis
                                        val y =
                                            size.height - (strokeWidth / 2) // Posisi Y di bagian paling bawah

                                        // Gambar garis dari kiri ke kanan di posisi Y tersebut
                                        drawLine(
                                            color = colorEnd, // Anda bisa gunakan warna dari theme Anda
                                            start = Offset(
                                                0f,
                                                y
                                            ),
                                            end = Offset(
                                                size.width,
                                                y
                                            ),
                                            strokeWidth = strokeWidth
                                        )
                                    }
                            ) {
                                // row untuk isi log dan bin icon
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(bottom = 5.dp)

                                ) {
                                    ActivityLogItemCard(
                                        logTitle = "Today's morning run",
                                        onLogClicked = {},
                                        onDeleteClicked = {},
                                        modifier = Modifier
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.size(10.dp))

                            // Column pembungkus border bawah
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        val strokeWidth =
                                            2.dp.toPx() // Ketebalan garis
                                        val y =
                                            size.height - (strokeWidth / 2) // Posisi Y di bagian paling bawah

                                        // Gambar garis dari kiri ke kanan di posisi Y tersebut
                                        drawLine(
                                            color = colorEnd, // Anda bisa gunakan warna dari theme Anda
                                            start = Offset(
                                                0f,
                                                y
                                            ),
                                            end = Offset(
                                                size.width,
                                                y
                                            ),
                                            strokeWidth = strokeWidth
                                        )
                                    }
                            ) {
                                // row untuk isi log dan bin icon
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(bottom = 5.dp)

                                ) {
                                    ActivityLogItemCard(
                                        logTitle = "Today's morning run",
                                        onLogClicked = {},
                                        onDeleteClicked = {},
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonActivityLog(
    onAddClicked: () -> Unit,
    onEditClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Definisikan ukuran radius yang akan kita gunakan
    val fullRadius = 30.dp // Untuk sudut luar
    val smallRadius =
        10.dp // Untuk sudut dalam (agar terlihat menyatu, tapi tetap ada sedikit pemisah)

    // 💡 SHAPE CUSTOM ASIMETRIS
    // Tombol Kiri (Add): Penuh di Kiri, Kecil di Kanan
    val LeftPillShape = RoundedCornerShape(
        topStart = fullRadius,
        bottomStart = fullRadius,
        topEnd = smallRadius,
        bottomEnd = smallRadius
    )

    // Tombol Kanan (Edit): Kecil di Kiri, Penuh di Kanan
    val RightPillShape = RoundedCornerShape(
        topStart = smallRadius,
        bottomStart = smallRadius,
        topEnd = fullRadius,
        bottomEnd = fullRadius
    )

    // ... (Definisi warna lainnya)
    val customGreen = SmartFitColors.MainGreen
    val lightContainer = SmartFitColors.SecondaryWhite


    // GROUP BUTTONS
    Row(verticalAlignment = Alignment.CenterVertically) {

        // TOMBOL 1: ADD (Menggunakan LeftPillShape)
        Button(
            onClick = onAddClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = lightContainer,
                contentColor = customGreen // Warna teks hijau
            ),
            modifier = Modifier.width(66.dp),
            shape = LeftPillShape, // <-- Shape Kustom Kiri!
            // Hilangkan padding bawaan agar tombol terlihat berdempetan
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Add")
        }

        // 💡 PENTING: Gunakan SPACER sangat kecil atau jangan gunakan sama sekali
        // untuk membuat ilusi penyatuan seperti di gambar
        Spacer(modifier = Modifier.width(5.dp))

        // TOMBOL 2: EDIT (Menggunakan RightPillShape)
        Button(
            onClick = onEditClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = customGreen,
                contentColor = Color.White
            ),
            modifier = Modifier.width(66.dp),
            shape = RightPillShape, // <-- Shape Kustom Kanan!
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Edit")
        }
    }
}

@Composable
fun ActivityLogItemCard(
    logTitle: String,
    onLogClicked: () -> Unit, // Handler untuk klik di area log (Button)
    onDeleteClicked: () -> Unit, // Handler untuk klik di ikon Delete (Icon)
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            // Hanya berikan padding horizontal
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onLogClicked,
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth(0.9f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmartFitColors.MainGreen, // Hapus warna background Button
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(25)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = logTitle,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.delete_logo),
                contentDescription = "Delete Icon",
                modifier = Modifier
                    .size(24.dp),
                colorFilter = ColorFilter.tint(
                    color = Color.Red, // Warna yang kamu inginkan
                    blendMode = BlendMode.SrcIn // Cara warna diaplikasikan (SrcIn adalah yang paling umum untuk ikon)
                )
            )
        }
    }
}

@Preview
@Composable
private fun ActivityLogPreview() {
    SmartFitTheme {
//        ActivityLogItemCard(
//            logTitle = "Running",
//            onLogClicked = {},
//            onDeleteClicked = {},
//            modifier = Modifier
//        )
//        ButtonActivityLog(
//            onAddClicked = {},
//            onEditClicked = {},
//            modifier = Modifier
//        )
        ActivityLog()
    }
}
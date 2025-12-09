package com.example.d1_jetpackcompose.ui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.theme.smartFitShape
import com.example.d1_jetpackcompose.ui.components.GeneralButton

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DashboardLayout()
                }
            }
        }
    }
}

@Composable
fun DashboardLayout(modifier: Modifier = Modifier) {
    val homeLogo = painterResource(R.drawable.home_icon)
    val activityLog = painterResource(R.drawable.activity_log_logo)
    val profileLogo = painterResource(R.drawable.profile_logo)

    val colorStartDark = Color(0xFF000000)
    val colorStartLight = Color(0xFFDAE0E0)
    val colorEnd = Color(0xFF4F6A4E)

    val gradientDarkMode = Brush.verticalGradient(
        colors = listOf(colorStartDark, colorEnd),
    )
    val gradientLightMode = Brush.verticalGradient(
        colors = listOf(colorStartLight, colorEnd),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientDarkMode)
    ) {
        Image(
            painter = painterResource(R.drawable.darkmode_background),
            contentDescription = "Background Overlay", // Deskripsi untuk aksesibilitas
            modifier = Modifier.fillMaxSize(), // Pastikan gambar mengisi seluruh Box
            contentScale = ContentScale.Crop, // Agar gambar menutupi semua area tanpa distorsi
            alpha = 0.30f // Atur opacity di sini (0.0f = transparan, 1.0f = solid)
        )

        // The outest wrapper
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Settings wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.038f)
            ) {

            }

            // Main Content Wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp)
            ) {

                // Title Wrapper
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.15f)
                        .padding(horizontal = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Welcome to",
                        fontFamily = robotoFontFamily,
                        color = Color(0xFF4F6A4E),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                    Text(
                        text = "SMARTFIT",
                        fontFamily = robotoFontFamily,
                        color = Color(0xFF4F6A4E),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                // Card 1 Wrapper
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.28f)
                ) {
                    Card(
                        shape = smartFitShape(30.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorStartDark,
                            contentColor = colorEnd
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.7f),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 17.dp)
                                        .fillMaxWidth(0.33f)
                                ) {
                                    Text(
                                        text = "Step Count",
                                        fontSize = 20.sp,
                                        fontFamily = robotoFontFamily,
                                    )

                                    Text(
                                        text = "300",
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = robotoFontFamily,
                                    )
                                }

                                Spacer(modifier = Modifier.size(10.dp))

                                Column {
                                    Image(
                                        painter = painterResource(R.drawable.walk_icon),
                                        contentDescription = "SmartFit Logo",
                                        modifier = Modifier
                                            .size(96.dp)
                                            .fillMaxWidth(0.33f)
                                    )
                                }

                                Spacer(modifier = Modifier.size(10.dp))

                                Column {
                                    Column {
                                        Text(
                                            text = "Calories Burned",
                                            fontSize = 14.sp,
                                            fontFamily = robotoFontFamily,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )

                                        Text(
                                            text = "150 Cal",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = robotoFontFamily,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "Distance",
                                            fontSize = 14.sp,
                                            fontFamily = robotoFontFamily,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )

                                        Text(
                                            text = "99 Km",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = robotoFontFamily,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            ) {
                                GeneralButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .padding(top = 5.dp),
                                    "Start Your Journey"
                                )
                            }

                        }
                    }

                }
                Spacer(modifier = Modifier.size(45.dp))

                // Card 2 & 3 Wrapper
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                ) {
                    Row (

                    ) {
                        Column (

                        ) {

                        }

                        Column (

                        ) {

                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun DashboardPreview() {
    SmartFitTheme {
        DashboardLayout()
    }
}
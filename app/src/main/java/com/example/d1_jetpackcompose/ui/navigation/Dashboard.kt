package com.example.d1_jetpackcompose.ui.navigation

import android.R.attr.fontFamily
import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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

val colorStartDark = Color(0xFF000000)
val colorStartLight = Color(0xFFDAE0E0)
val colorEnd = Color(0xFF4F6A4E)

@Composable
fun DashboardLayout(modifier: Modifier = Modifier) {
    val homeLogo = painterResource(R.drawable.home_icon)
    val activityLog = painterResource(R.drawable.activity_log_logo)
    val profileLogo = painterResource(R.drawable.profile_logo)
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
            contentDescription = "Background Overlay",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.30f
        )
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BubbleNavigationBar()
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {

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

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.95f),
                            verticalArrangement = Arrangement.spacedBy(30.dp)
                        ) {
                            // Card 1 Wrapper
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Card(
                                    shape = smartFitShape(25.dp),
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
                                                .fillMaxHeight(),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            GeneralButton(
                                                onClick = {},
                                                modifier = Modifier.padding(top = 5.dp),
                                                "Start Your Journey"
                                            )
                                        }

                                    }
                                }
                            }

                            // Card 2 & 3 Wrapper
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ) {
                                    Card(
                                        shape = smartFitShape(25.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = colorStartDark,
                                            contentColor = colorEnd
                                        ),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                    ) {
                                        Column (
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp)
                                                .padding(vertical = 10.dp)
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                        ) {

                                            Column (
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(0.7f),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Top
                                            ) {
                                                Text(
                                                    text = "Today",
                                                    fontFamily = robotoFontFamily,
                                                    fontSize = 16.sp
                                                )

                                                Text(
                                                    text = "0",
                                                    fontFamily = robotoFontFamily,
                                                    fontSize = 40.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Column (
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                GeneralButton(
                                                    onClick = {},
                                                    modifier = Modifier
                                                        .padding(top = 2.dp),
                                                    "Step Count"
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.size(20.dp))

                                    Card(
                                        shape = smartFitShape(25.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = colorStartDark,
                                            contentColor = colorEnd
                                        ),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                    ) {
                                        Column (
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp)
                                                .padding(vertical = 10.dp)
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                        ) {

                                            Column (
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(0.7f),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Top
                                            ) {
                                                Text(
                                                    text = "Today",
                                                    fontFamily = robotoFontFamily,
                                                    fontSize = 16.sp
                                                )

                                                Text(
                                                    text = "0.0 km",
                                                    fontFamily = robotoFontFamily,
                                                    fontSize = 40.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Column (
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                GeneralButton(
                                                    onClick = {},
                                                    modifier = Modifier
                                                        .padding(top = 2.dp),
                                                    "Step Distance",

                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Card 4 wrapper
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ) {
                                    Card(
                                        shape = smartFitShape(25.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = colorStartDark,
                                            contentColor = colorEnd
                                        ),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(0.35f)
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
                                                    },
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(horizontal = 20.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "Online Tips",
                                                        fontFamily = robotoFontFamily,
                                                        fontSize = 24.sp
                                                    )

                                                    Image(
                                                        painter = painterResource(R.drawable.arrow_icon),
                                                        contentDescription = "SmartFit Logo",
                                                    )
                                                }
                                            }

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight()
                                                    .padding(15.dp)
                                            ) {

                                            }
                                        }
                                    }
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
fun BubbleNavigationBar() {
    // Di sinilah Anda mendesain navigasi "mengambang" Anda.
    // Kita gunakan Card untuk mendapatkan efek bubble/mengambang.
    Card(
        shape = MaterialTheme.shapes.extraLarge, // Bentuk pil atau sangat bulat
        colors = CardDefaults.cardColors(
            containerColor = colorStartDark,
            contentColor = colorEnd
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp), // Memberi jarak dari tepi layar
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Memberi bayangan
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.clickable{

                }
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
                modifier = Modifier.clickable{

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
                modifier = Modifier.clickable{

                }
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_logo),
                    contentDescription = "Profile"
                )
                Text(
                    text = "Profile",
                    fontSize = 12.sp,
                    fontFamily = robotoFontFamily,
                )
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
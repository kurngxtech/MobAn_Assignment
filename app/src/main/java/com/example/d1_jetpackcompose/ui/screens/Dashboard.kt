package com.example.d1_jetpackcompose.ui.screens

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.theme.smartFitShape
import com.example.d1_jetpackcompose.ui.components.GeneralButton
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    DashboardLayout(navController = navController)
                }
            }
        }
    }
}

val colorStartDark = Color(0xFF000000)
val colorStartLight = Color(0xFFDAE0E0)
val colorEnd = Color(0xFF4F6A4E)

@Composable
fun DashboardLayout(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val gradientDarkMode = Brush.verticalGradient(
        colors = listOf(colorStartDark, colorEnd),
    )
    val gradientLightMode = Brush.verticalGradient(
        colors = listOf(colorStartLight, colorEnd),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .drawBehind {

                // Lingkaran kanan atas
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4F6A4E).copy(alpha = 0.7f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = size.width * 0.75f,
                            y = size.height * 0.25f
                        ),
                        radius = size.minDimension * 0.85f
                    ),
                    radius = size.minDimension * 85f,
                    center = Offset(
                        x = size.width * 0.75f,
                        y = size.height * 0.25f
                    )
                )

                // Lingkaran kiri bawah
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4F6A4E).copy(alpha = 0.7f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = size.width * 0.25f,
                            y = size.height * 0.75f
                        ),
                        radius = size.minDimension * 0.85f
                    ),
                    radius = size.minDimension * 0.85f,
                    center = Offset(
                        x = size.width * 0.25f,
                        y = size.height * 0.75f
                    )
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.18f))
        )
                // The outest wrapper
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Main Content Wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp)
                    .padding(top = 30.dp)
            ) {
                // Title Wrapper
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.12f)
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
                            .padding(bottom = 0.dp)
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
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    // Card 1 Wrapper
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.33f)
                    ) {
                        Card(
                            shape = smartFitShape(25.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorStartDark.copy(alpha = 0.5f),
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
                            .fillMaxHeight(0.45f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Card(
                                shape = smartFitShape(25.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorStartDark.copy(alpha = 0.5f),
                                    contentColor = colorEnd
                                ),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable {
                                        navController.navigate(AppRoutes.STEPS_COUNT) {
                                            launchSingleTop = true
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            restoreState = true
                                        }
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .padding(vertical = 10.dp)
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ) {

                                    Column(
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

                                    Column(
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
                                    containerColor = colorStartDark.copy(alpha = 0.5f),
                                    contentColor = colorEnd
                                ),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable {
                                        navController.navigate(AppRoutes.DISTANCE_COUNT) {
                                            launchSingleTop = true
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            restoreState = true
                                        }
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .padding(vertical = 10.dp)
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                ) {

                                    Column(
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

                                    Column(
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
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Card(
                                shape = smartFitShape(25.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorStartDark.copy(alpha = 0.5f),
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
                                        .clickable(
                                            onClick = {}
                                        )
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

@Preview
@Composable
private fun DashboardPreview() {
    SmartFitTheme {
        val navController = rememberNavController()
        DashboardLayout(navController = navController)
    }
}
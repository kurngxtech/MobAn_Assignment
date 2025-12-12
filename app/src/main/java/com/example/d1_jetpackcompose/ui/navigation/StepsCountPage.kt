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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.d1_jetpackcompose.ui.components.DropDownButton
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors

class StepsCountPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    StepsCountPage()
                }
            }
        }
    }
}

@Composable
fun StepsCountPage(modifier: Modifier = Modifier) {
    val gradientDarkMode = Brush.verticalGradient(
        colors = listOf(SmartFitColors.PureBlack, SmartFitColors.MainGreen),
    )
    val gradientLightMode = Brush.verticalGradient(
        colors = listOf(SmartFitColors.PureWhite, SmartFitColors.MainGreen),
    )

    Box (
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
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Column {
                Text(
                    text = "Steps",
                    fontSize = 52.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = SmartFitColors.MainGreen
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DropDownButton(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "This Week",
                    color = SmartFitColors.PureBlack.copy(alpha = 0.6f),
                    fontFamily = robotoFontFamily
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
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
                            .padding(horizontal = 60.dp)
                            .padding(vertical = 30.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box (
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .background(SmartFitColors.MainGreen.copy(0.7f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.footstep_logo),
                                    contentDescription = "Steps Logo",
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "You have reached!",
                                fontFamily = robotoFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.size(10.dp))

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "850",
                                    fontSize = 64.sp,
                                    fontFamily = robotoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(modifier = Modifier.size(20.dp))

                                Text(
                                    text = "Steps",
                                    fontSize = 32.sp,
                                    fontFamily = robotoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                )
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
private fun StepsCountPagePrev() {
    SmartFitTheme {
        StepsCountPage()
    }
}
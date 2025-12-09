package com.example.d1_jetpackcompose.ui.navigation // Assuming this is your package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.components.RoundedButton
import com.example.d1_jetpackcompose.ui.theme.SmartFitTypography
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

class BelajarLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    BasicLayout()
                }
            }
        }
    }
}

@Composable
fun BasicLayout(modifier: Modifier = Modifier) {
    val backgroundImagePainter = painterResource(R.drawable.background_login)

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = backgroundImagePainter,
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column ( // column untuk wrapper logo,text,button
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Smartfit logo
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.smartfit_logo),
                    contentDescription = "SmartFit Logo",
                    modifier = Modifier
                        .size(220.dp)
                )
            }

            // empty spacer
            Spacer(
                modifier = Modifier
                    .fillMaxHeight(0.65f)
            )

            // column for text and button below
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.58f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Text judul dan deskripsi singkat (atas)
                Text(
                    text = "Track your steps",
                    style = SmartFitTypography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(1.dp))

                Text(
                    text = "and get healthier life",
                    style = SmartFitTypography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(1.dp))

                Text(
                    text = "check every steps and every distance you take",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(1.dp))
                Text(
                    text = "and get healthier life",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White
                )
            }

            // button
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                RoundedButton(
                    onClick = {},
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already has an account?",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "Login",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BasicLayoutPreview() {
    SmartFitTheme {
        BasicLayout()
    }
}
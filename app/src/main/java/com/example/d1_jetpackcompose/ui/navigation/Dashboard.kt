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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.theme.SmartFitTypography
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

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

    val colorStartDark = Color.Black
    val colorStartLight = Color(0xFFDAE0E0)
    val colorEnd = Color(0xFF4F6A4E)

    val gradientDarkMode = Brush.verticalGradient(
        colors = listOf(colorStartDark, colorEnd),
    )
    val gradientLightMode = Brush.verticalGradient(
        colors = listOf(colorStartLight, colorEnd),
    )

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientDarkMode)
    ) {

    }
    
}

@Preview
@Composable
private fun DashboardPreview() {
    SmartFitTheme {
        DashboardLayout()
    }
}
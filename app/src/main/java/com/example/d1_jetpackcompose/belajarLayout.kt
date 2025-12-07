package com.example.d1_jetpackcompose // Assuming this is your package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme


class BelajarLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BasicLayout("Bagus Kurniawan")
                }
            }
        }
    }
}

@Composable
fun BasicLayout(name: String, modifier: Modifier = Modifier) {
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

        // Smartfit logo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.smartfit_logo),
                contentDescription = "SmartFit Logo",
                modifier = Modifier
                    .size(180.dp)
            )
        }


    }
}


@Preview(showBackground = true)
@Composable
private fun BasicLayoutPreview() {
    SmartFitTheme {
        BasicLayout("Bagus Kurniawan")
    }
}
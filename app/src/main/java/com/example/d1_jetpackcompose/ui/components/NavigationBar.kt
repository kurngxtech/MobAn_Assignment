package com.example.d1_jetpackcompose.ui.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

class NavigationBarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationBarLayout()
                }
            }
        }
    }
}

@Composable
fun NavigationBarLayout(modifier: Modifier = Modifier) {

}
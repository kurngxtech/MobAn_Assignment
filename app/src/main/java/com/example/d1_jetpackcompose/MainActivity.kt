// File: MainActivity.kt
package com.example.d1_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.d1_jetpackcompose.ui.screens.MainScreen // <- IMPORT INI
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                // Panggil MainScreen, bukan halaman individual.
                // MainScreen akan mengatur segalanya.
                MainScreen()
            }
        }
    }
}

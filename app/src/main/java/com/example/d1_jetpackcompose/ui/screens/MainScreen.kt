package com.example.d1_jetpackcompose.ui.screens

// File baru: ui/screens/MainScreen.kt

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.components.BubbleNavigationBar
import com.example.d1_jetpackcompose.ui.navigation.AppNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        // Di sinilah kita menempatkan BubbleNavigationBar Anda
        bottomBar = {
            BubbleNavigationBar(navController = navController) // Kita perlu modifikasi sedikit agar bisa berinteraksi
        }
    ) { innerPadding ->
        // AppNavHost akan menjadi konten utama dari Scaffold
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding) // Padding ini penting!
        )
    }
}

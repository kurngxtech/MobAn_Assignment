package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.components.BubbleNavigationBar
import com.example.d1_jetpackcompose.ui.navigation.AppNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // Pastikan warna ini sama dengan warna background yang Anda inginkan
    val globalBackgroundColor = MaterialTheme.colorScheme.background

    // Gunakan Box sebagai root untuk menumpuk elemen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(globalBackgroundColor) // Background menyeluruh
    ) {
        // 1. Konten Utama (NavHost)
        // Kita beri padding bawah secara manual agar konten tidak tertutup bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp) // Jarak aman agar konten berhenti sebelum Navbar
        ) {
            AppNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 2. Navbar (Floating Overlay)
        // Kita letakkan secara independen di atas NavHost
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BubbleNavigationBar(navController = navController)
        }
    }
}
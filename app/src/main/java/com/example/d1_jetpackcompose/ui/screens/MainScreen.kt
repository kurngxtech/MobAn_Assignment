// File: ui/screens/MainScreen.kt

package com.example.d1_jetpackcompose.ui.screens

// Import yang diperlukan, terutama Boximport androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.components.BubbleNavigationBar
import com.example.d1_jetpackcompose.ui.navigation.AppNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // SOLUSI: Gunakan Box untuk menumpuk NavHost dan NavigationBar
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. AppNavHost sekarang menjadi latar belakang, mengisi seluruh layar.
        //    Gradien dari halaman Anda akan digambar di sini dari ujung ke ujung.
        AppNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize() // Pastikan ini mengisi seluruh Box
        )

        // 2. BubbleNavigationBar ditempatkan di atas AppNavHost.
        //    Modifier.align(Alignment.BottomCenter) akan menempatkannya
        //    di bagian tengah-bawah layar.
        BubbleNavigationBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

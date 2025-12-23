package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.ui.components.BubbleNavigationBar
import com.example.d1_jetpackcompose.ui.navigation.AppNavHost
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModelFactory

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Inisialisasi Database (Single Source of Truth)
    val database = AppDatabase.getDatabase(context)
    val repository = ActivityRepository(database.activityDao())

    // 2. Buat ViewModel menggunakan Factory agar data tersimpan secara persistent
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(repository)
    )

    val globalBackgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(globalBackgroundColor)
    ) {
        // Konten Utama
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp) // Beri jarak agar tidak tertutup Navbar
        ) {
            AppNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                viewModel = sharedViewModel // Kirim viewModel yang sama ke semua screen
            )
        }

        // Navbar di bagian bawah
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BubbleNavigationBar(navController = navController)
        }
    }
}
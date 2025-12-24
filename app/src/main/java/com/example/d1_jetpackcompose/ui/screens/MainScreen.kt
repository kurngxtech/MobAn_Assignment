package com.example.d1_jetpackcompose.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.ui.components.BubbleNavigationBar
import com.example.d1_jetpackcompose.ui.navigation.AppNavHost
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModelFactory
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModelFactory

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 1. Inisialisasi Database & SharedPreferences
    val database = AppDatabase.getDatabase(context)
    val activityRepository = ActivityRepository(database.activityDao())
    val authRepository = AuthRepository(database.userDao())
    val sharedPreferences = context.getSharedPreferences("smartfit_prefs", Context.MODE_PRIVATE) // 💡 Prefs

    // 2. ViewModels
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(activityRepository)
    )

    // ViewModel Auth Baru dengan SharedPreferences
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, sharedPreferences)
    )

    // 💡 3. CEK STATUS AUTO-LOGIN
    // Jika sesi valid (Remember Me true & ada data), langsung ke DASHBOARD
    val isSessionValid by authViewModel.isSessionValid.collectAsState()
    val startDestination = if (isSessionValid) AppRoutes.DASHBOARD else AppRoutes.WELCOME

    // 4. LOGIKA NAVBAR DINAMIS
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute !in listOf(
        AppRoutes.WELCOME,
        AppRoutes.LOGIN,
        AppRoutes.SIGNUP
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
                .padding(bottom = if (showBottomBar) 90.dp else 0.dp)
        ) {
            AppNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                viewModel = sharedViewModel,
                authViewModel = authViewModel,
                startDestination = startDestination // 💡 Kirim Start Destination Dinamis
            )
        }

        // Navbar di bagian bawah
        if (showBottomBar) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BubbleNavigationBar(navController = navController)
            }
        }
    }
}
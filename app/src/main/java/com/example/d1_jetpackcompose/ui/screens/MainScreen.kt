package com.example.d1_jetpackcompose.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    // 1. Inisialisasi Database
    val database = AppDatabase.getDatabase(context)
    val activityRepository = ActivityRepository(database.activityDao())
    val authRepository = AuthRepository(database.userDao())
    val sharedPreferences = context.getSharedPreferences("smartfit_prefs", Context.MODE_PRIVATE)

    // 2. ViewModels
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(activityRepository)
    )
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, sharedPreferences)
    )

    // 3. LOGIKA SPLASH / START DESTINATION
    val isCheckingSession by authViewModel.isCheckingSession.collectAsState()
    val isSessionValid by authViewModel.isSessionValid.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // 💡 LOGIKA UTAMA: Aplikasi siap HANYA JIKA session check selesai.
    // Jika session valid (user login), kita juga harus menunggu 'currentUser' tidak null.
    val isAppReady = !isCheckingSession && if (isSessionValid) currentUser != null else true

    val globalBackgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(globalBackgroundColor)
    ) {
        if (!isAppReady) {
            // 💡 TAMPILKAN LOADING SCREEN SELAMA CEK SESI/DATA
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            // 💡 APLIKASI SIAP DIRENDER

            // Hitung start destination setelah data siap
            val startDestination = remember(isSessionValid, currentUser) {
                if (isSessionValid) {
                    val user = currentUser
                    // Pengecekan aman karena user dijamin tidak null di blok ini
                    if (user != null && authViewModel.isSurveyCompleted(user)) {
                        AppRoutes.DASHBOARD
                    } else {
                        AppRoutes.SURVEY
                    }
                } else {
                    AppRoutes.WELCOME
                }
            }

            // Navbar Logic
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute !in listOf(
                AppRoutes.WELCOME,
                AppRoutes.LOGIN,
                AppRoutes.SIGNUP,
                AppRoutes.SURVEY,
                AppRoutes.EXERCISE, // Tambahkan ini agar navbar hilang di form exercise
                AppRoutes.FOOD,
                AppRoutes.EDIT_PROFILE
            )

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
                    startDestination = startDestination
                )
            }

            // Navbar
            if (showBottomBar) {
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    BubbleNavigationBar(navController = navController)
                }
            }
        }
    }
}
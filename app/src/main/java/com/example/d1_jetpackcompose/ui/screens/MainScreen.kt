package com.example.d1_jetpackcompose.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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

    val database = AppDatabase.getDatabase(context)
    val activityRepository = ActivityRepository(database.activityDao())
    val authRepository = AuthRepository(database.userDao())
    val sharedPreferences = context.getSharedPreferences("smartfit_prefs", Context.MODE_PRIVATE)

    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(activityRepository)
    )
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, sharedPreferences)
    )

    val isCheckingSession by authViewModel.isCheckingSession.collectAsState()
    val isSessionValid by authViewModel.isSessionValid.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val isAppReady = !isCheckingSession && if (isSessionValid) currentUser != null else true
    val globalBackgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(globalBackgroundColor)
    ) {
        if (!isAppReady) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val startDestination = remember(isSessionValid, currentUser) {
                if (isSessionValid) {
                    val user = currentUser
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

            // 💡 LOGIC SEMBUNYIKAN NAVBAR
            // Navbar disembunyikan jika rute saat ini ada di dalam list ini
            val showBottomBar = currentRoute !in listOf(
                AppRoutes.WELCOME,
                AppRoutes.LOGIN,
                AppRoutes.SIGNUP,
                AppRoutes.SURVEY,
                AppRoutes.EXERCISE,
                AppRoutes.FOOD,
                AppRoutes.EDIT_PROFILE,
                AppRoutes.DETAIL_LOG_ROUTE // 💡 DITAMBAHKAN: Sembunyikan di detail page
            )

            // 1. ANIMASI PADDING KONTEN
            val animatedBottomPadding by animateDpAsState(
                targetValue = if (showBottomBar) 110.dp else 0.dp, // Sesuaikan tinggi navbar
                animationSpec = tween(durationMillis = 500),
                label = "PaddingAnimation"
            )

            // Konten Utama
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = animatedBottomPadding)
            ) {
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                    viewModel = sharedViewModel,
                    authViewModel = authViewModel,
                    startDestination = startDestination
                )
            }

            // 2. ANIMASI NAVBAR (SLIDE)
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it }, // Muncul dari bawah
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, // Turun ke bawah
                    animationSpec = tween(durationMillis = 500)
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BubbleNavigationBar(navController = navController)
            }
        }
    }
}
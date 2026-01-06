package com.example.d1_jetpackcompose.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.data.repository.TipsRepository
import com.example.d1_jetpackcompose.ui.components.BubbleNavigationBar
import com.example.d1_jetpackcompose.ui.components.FloatingNavigationRail
import com.example.d1_jetpackcompose.ui.navigation.AppNavHost
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes.TIPS_LIST
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModelFactory
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModelFactory
import com.example.d1_jetpackcompose.ui.viewModel.ThemeViewModel
import com.example.d1_jetpackcompose.ui.viewModel.TipViewModelFactory
import com.example.d1_jetpackcompose.ui.viewModel.TipsViewModel

@Composable
fun MainScreen(
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    val database = AppDatabase.getDatabase(context)
    val activityRepository = ActivityRepository(database.activityDao())
    val authRepository = AuthRepository(database.userDao())
    val sharedPreferences = context.getSharedPreferences("smartfit_prefs", Context.MODE_PRIVATE)
    val tipRepository = remember { TipsRepository() }

    val sharedViewModel: SharedViewModel =
        viewModel(factory = SharedViewModelFactory(activityRepository))
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            authRepository,
            activityRepository,
            sharedPreferences
        )
    )
    val tipViewModel: TipsViewModel = viewModel(
        factory = TipViewModelFactory(
            repository = tipRepository,
            activityRepository = activityRepository
        )
    )

    val isCheckingSession by authViewModel.isCheckingSession.collectAsState()
    val isSessionValid by authViewModel.isSessionValid.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isAppReady = !isCheckingSession && if (isSessionValid) currentUser != null else true
    val globalBackgroundColor = MaterialTheme.colorScheme.background

    Box(modifier = Modifier
        .fillMaxSize()
        .background(globalBackgroundColor)) {
        if (!isAppReady) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val startDestination = remember(isSessionValid, currentUser) {
                if (isSessionValid) {
                    if (currentUser != null && authViewModel.isSurveyCompleted(currentUser!!)) AppRoutes.DASHBOARD else AppRoutes.SURVEY
                } else AppRoutes.WELCOME
            }

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val hideBottomBar = currentRoute in listOf(
                AppRoutes.WELCOME,
                AppRoutes.LOGIN,
                AppRoutes.SIGNUP,
                AppRoutes.SURVEY,
                AppRoutes.EXERCISE,
                AppRoutes.FOOD,
                TIPS_LIST,
                AppRoutes.TIP_DETAIL,
                AppRoutes.EDIT_PROFILE,
                AppRoutes.PERSONAL_INFO,
                AppRoutes.CHANGE_PASSWORD,
                AppRoutes.FAQ
            ) || currentRoute?.startsWith("detail_log") == true

            val showBottomBar = !hideBottomBar

            if (isTablet) {
                // --- TABLET LAYOUT ---
                Row(modifier = Modifier.fillMaxSize()) {
                    // 1. Navigation Rail
                    // PERBAIKAN: Menggunakan Box dengan lebar minimal/fixed, BUKAN weight.
                    // Ini membuat sisa ruang (weight 1f) lebih besar dan menarik konten ke kiri.
                    if (showBottomBar) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(110.dp) // Fixed width yang cukup untuk NavRail (80dp + padding)
                                .padding(vertical = 24.dp), // Padding vertikal biar rapi
                            contentAlignment = Alignment.CenterStart
                        ) {
                            FloatingNavigationRail(navController = navController)
                        }
                    }

                    // 2. Konten Utama
                    // Mengambil sisa ruang sepenuhnya
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.fillMaxSize(),
                            viewModel = sharedViewModel,
                            authViewModel = authViewModel,
                            tipViewModel = tipViewModel,
                            startDestination = startDestination,
                            themeViewModel = themeViewModel
                        )
                    }
                }
            } else {
                // --- MOBILE LAYOUT ---
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
                        tipViewModel = tipViewModel,
                        startDestination = startDestination,
                        themeViewModel = themeViewModel
                    )
                }

                if (showBottomBar) {
                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        BubbleNavigationBar(navController = navController)
                    }
                }
            }
        }
    }
}
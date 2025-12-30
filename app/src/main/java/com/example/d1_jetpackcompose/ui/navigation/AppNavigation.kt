package com.example.d1_jetpackcompose.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.d1_jetpackcompose.ui.screens.*
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel

object AppRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val ACTIVITY = "activity"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val EXERCISE = "exercise"
    const val FOOD = "food"
    const val SURVEY = "survey"
    const val DETAIL_LOG_ROUTE = "detail_log/{activityId}"
}

// Konstanta durasi animasi agar konsisten
private const val ANIM_DURATION = 500

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,

        // 1. GLOBAL ENTER TRANSITION (Saat halaman MUNCUL)
        enterTransition = {
            val initial = initialState.destination.route
            val target = targetState.destination.route

            when {
                // Welcome -> Login (Login muncul diam di tengah, Welcome naik)
                initial == AppRoutes.WELCOME && target == AppRoutes.LOGIN -> {
                    EnterTransition.None
                }

                // Login -> Sign Up (Forward: Slide Left)
                initial == AppRoutes.LOGIN && target == AppRoutes.SIGNUP -> {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(ANIM_DURATION)
                    )
                }

                // Sign Up -> Login (Backward: Slide Right)
                initial == AppRoutes.SIGNUP && target == AppRoutes.LOGIN -> {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(ANIM_DURATION)
                    )
                }

                // Login -> Dashboard (Dashboard muncul diam di tengah, Login naik)
                initial == AppRoutes.LOGIN && target == AppRoutes.DASHBOARD -> {
                    EnterTransition.None
                }

                // Survey -> Dashboard (Dashboard muncul diam di tengah, Survey naik)
                initial == AppRoutes.SURVEY && target == AppRoutes.DASHBOARD -> {
                    EnterTransition.None
                }

                // Navigasi Menu Bawah (Slide Horizontal Cerdas)
                isBottomNavRoute(initial) && isBottomNavRoute(target) -> {
                    val initialIndex = getBottomNavIndex(initial)
                    val targetIndex = getBottomNavIndex(target)
                    if (targetIndex < initialIndex) {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(ANIM_DURATION)
                        )
                    } else {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(ANIM_DURATION)
                        )
                    }
                }

                // Halaman "Overlay" (Add Exercise/Food) - Muncul dari Bawah (Slide Up)
                isOverlayRoute(target) -> {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(ANIM_DURATION)
                    )
                }

                // Default
                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(ANIM_DURATION)
                )
            }
        },

        // 2. GLOBAL EXIT TRANSITION (Saat halaman PERGI)
        exitTransition = {
            val initial = initialState.destination.route
            val target = targetState.destination.route

            when {
                // Welcome -> Login (Welcome Naik ke Atas)
                initial == AppRoutes.WELCOME && target == AppRoutes.LOGIN -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(ANIM_DURATION)
                    )
                }

                // Login -> Sign Up (Login Geser Kiri)
                initial == AppRoutes.LOGIN && target == AppRoutes.SIGNUP -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(ANIM_DURATION)
                    )
                }

                // Sign Up -> Login (Sign Up Geser Kanan - Balik)
                initial == AppRoutes.SIGNUP && target == AppRoutes.LOGIN -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(ANIM_DURATION)
                    )
                }

                // Login -> Dashboard (Login Naik ke Atas)
                initial == AppRoutes.LOGIN && target == AppRoutes.DASHBOARD -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(ANIM_DURATION)
                    )
                }

                // Survey -> Dashboard (Survey Naik ke Atas)
                initial == AppRoutes.SURVEY && target == AppRoutes.DASHBOARD -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(ANIM_DURATION)
                    )
                }

                // Navigasi Menu Bawah
                isBottomNavRoute(initial) && isBottomNavRoute(target) -> {
                    val initialIndex = getBottomNavIndex(initial)
                    val targetIndex = getBottomNavIndex(target)
                    if (targetIndex < initialIndex) {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(ANIM_DURATION)
                        )
                    } else {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(ANIM_DURATION)
                        )
                    }
                }

                // Halaman "Overlay" Ditutup (Turun ke Bawah)
                isOverlayRoute(initial) -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(ANIM_DURATION)
                    )
                }

                // Default
                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(ANIM_DURATION)
                )
            }
        },

        // 3. POP ENTER (Saat tombol Back ditekan / kembali ke halaman ini)
        popEnterTransition = {
            val initial = initialState.destination.route
            // Jika kembali dari halaman Overlay, halaman ini diam di tempat
            if (isOverlayRoute(initial)) {
                EnterTransition.None
            } else {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(ANIM_DURATION)
                )
            }
        },

        // 4. POP EXIT (Saat halaman ini ditutup via Back)
        popExitTransition = {
            val initial = initialState.destination.route
            // Jika halaman Overlay ditutup, dia turun ke bawah
            if (isOverlayRoute(initial)) {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(ANIM_DURATION)
                )
            } else {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(ANIM_DURATION)
                )
            }
        }
    ) {
        composable(AppRoutes.WELCOME) { WelcomePage(navController = navController) }
        composable(AppRoutes.LOGIN) { LoginScreen(navController, authViewModel) }
        composable(AppRoutes.SIGNUP) { SignUpScreen(navController, authViewModel) }
        composable(AppRoutes.SURVEY) {
            SurveyScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(navController, viewModel, authViewModel)
        }
        composable(AppRoutes.ACTIVITY) { ActivityLogScreen(navController, viewModel) }

        // --- HALAMAN OVERLAY (ADD/DETAIL) ---
        // Transisi lokal di sini akan mengikuti logika Global di atas (Slide Up/Down)

        composable(AppRoutes.EXERCISE) {
            AddExerciseScreen(navController, viewModel)
        }

        composable(AppRoutes.FOOD) {
            AddFoodScreen(navController, viewModel)
        }

        composable(AppRoutes.PROFILE) {
            ProfileScreen(navController, viewModel, authViewModel)
        }

        composable(AppRoutes.EDIT_PROFILE) {
            EditProfileScreen(navController, authViewModel)
        }

        composable(
            route = AppRoutes.DETAIL_LOG_ROUTE,
            arguments = listOf(navArgument("activityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("activityId") ?: 0
            DetailLogScreen(navController, id, viewModel)
        }
    }
}

// --- HELPER FUNCTIONS ---

fun isBottomNavRoute(route: String?): Boolean {
    return route in listOf(AppRoutes.DASHBOARD, AppRoutes.ACTIVITY, AppRoutes.PROFILE)
}

// Helper baru untuk mendeteksi rute overlay (Add/Detail)
fun isOverlayRoute(route: String?): Boolean {
    if (route == null) return false
    return route == AppRoutes.EXERCISE ||
            route == AppRoutes.FOOD ||
            route.startsWith("detail_log")
}

fun getBottomNavIndex(route: String?): Int {
    return when (route) {
        AppRoutes.DASHBOARD -> 0
        AppRoutes.ACTIVITY -> 1
        AppRoutes.PROFILE -> 2
        else -> -1
    }
}
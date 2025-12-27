package com.example.d1_jetpackcompose.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
}

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
        // 1. GLOBAL ENTER TRANSITION (Masuk)
        enterTransition = {
            val initialRoute = initialState.destination.route
            val targetRoute = targetState.destination.route

            // Logic: Jika pindah antar Menu Navbar -> Slide Cerdas
            if (isBottomNavRoute(initialRoute) && isBottomNavRoute(targetRoute)) {
                val initialIndex = getBottomNavIndex(initialRoute)
                val targetIndex = getBottomNavIndex(targetRoute)
                if (targetIndex < initialIndex) {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
                } else {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
                }
            } else {
                // Default Slide (Maju)
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
            }
        },

        // 2. GLOBAL EXIT TRANSITION (Keluar/Ditinggalkan)
        exitTransition = {
            val initialRoute = initialState.destination.route
            val targetRoute = targetState.destination.route

            // 💡 PERBAIKAN: Jika tujuan adalah "Fade Route" (Add/Detail),
            // halaman saat ini (Activity Log) harus Fade Out, JANGAN Slide.
            if (isFadeRoute(targetRoute)) {
                fadeOut(tween(500))
            }
            else if (isBottomNavRoute(initialRoute) && isBottomNavRoute(targetRoute)) {
                // Logic Slide Navbar
                val initialIndex = getBottomNavIndex(initialRoute)
                val targetIndex = getBottomNavIndex(targetRoute)
                if (targetIndex < initialIndex) {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
                } else {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
                }
            } else {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
            }
        },

        // 3. GLOBAL POP ENTER (Kembali ke halaman ini)
        popEnterTransition = {
            val initialRoute = initialState.destination.route // Route yang kita tinggalkan

            // 💡 PERBAIKAN: Jika kita kembali DARI halaman Add/Detail,
            // halaman ini (Activity Log) harus Fade In, JANGAN Slide.
            if (isFadeRoute(initialRoute)) {
                fadeIn(tween(500))
            } else {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
            }
        },

        // 4. GLOBAL POP EXIT (Halaman ini ditutup/back)
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
        }
    ) {
        composable(AppRoutes.WELCOME) { WelcomePage(navController = navController) }
        composable(AppRoutes.LOGIN) { LoginScreen(navController, authViewModel) }
        composable(AppRoutes.SIGNUP) { SignUpScreen(navController, authViewModel) }
        composable(AppRoutes.SURVEY) { SurveyScreen(navController = navController, authViewModel = authViewModel) }

        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(navController, viewModel, authViewModel)
        }
        composable(AppRoutes.ACTIVITY) { ActivityLogScreen(navController, viewModel) }

        // --- ROUTES DENGAN ANIMASI FADE (ADD/DETAIL) ---
        // (Enter/Exit di sini menimpa global untuk halaman INI saja)

        composable(
            route = AppRoutes.EXERCISE,
            enterTransition = { fadeIn(tween(500)) },
            exitTransition = { fadeOut(tween(500)) },
            popEnterTransition = { fadeIn(tween(500)) }, // Tidak terpakai jika popBackStack, tapi aman
            popExitTransition = { fadeOut(tween(500)) }
        ) {
            AddExerciseScreen(navController, viewModel)
        }

        composable(
            route = AppRoutes.FOOD,
            enterTransition = { fadeIn(tween(500)) },
            exitTransition = { fadeOut(tween(500)) },
            popEnterTransition = { fadeIn(tween(500)) },
            popExitTransition = { fadeOut(tween(500)) }
        ) {
            AddFoodScreen(navController, viewModel)
        }

        composable(AppRoutes.PROFILE) {
            ProfileScreen(navController, viewModel, authViewModel)
        }

        composable(AppRoutes.EDIT_PROFILE) {
            EditProfileScreen(navController, authViewModel)
        }

        composable(
            route = "detail_log/{activityId}",
            arguments = listOf(navArgument("activityId") { type = NavType.IntType }),
            enterTransition = { fadeIn(tween(500)) },
            exitTransition = { fadeOut(tween(500)) },
            popEnterTransition = { fadeIn(tween(500)) },
            popExitTransition = { fadeOut(tween(500)) }
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

// 💡 HELPER BARU: Cek apakah route tujuan memerlukan Fade Animation
fun isFadeRoute(route: String?): Boolean {
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
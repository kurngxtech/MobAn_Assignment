package com.example.d1_jetpackcompose.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
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
import com.example.d1_jetpackcompose.ui.viewModel.TipsViewModel

object AppRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val ACTIVITY = "activity"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val PERSONAL_INFO = "personal_info"
    const val CHANGE_PASSWORD = "change_password"

    // 💡 RUTE BARU: FAQ
    const val FAQ = "faq"
    const val EXERCISE = "exercise"
    const val FOOD = "food"
    const val SURVEY = "survey"
    const val DETAIL_LOG_ROUTE = "detail_log/{activityId}"
    const val TIPS_LIST = "tips_list"
    const val TIP_DETAIL = "tip_detail/{tipId}"
}

private const val ANIM_DURATION = 500

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    tipViewModel: TipsViewModel,
    startDestination: String
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        // ... (Transisi biarkan sama, dipotong agar tidak terlalu panjang)
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(ANIM_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(ANIM_DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(ANIM_DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(ANIM_DURATION)
            )
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
        composable(AppRoutes.TIPS_LIST) { OnlineTipsListScreen(navController, tipViewModel) }
        composable(
            route = AppRoutes.TIP_DETAIL,
            arguments = listOf(navArgument("tipId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("tipId") ?: 0
            TipDetailScreen(navController, tipViewModel, id)
        }
        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                navController,
                viewModel,
                authViewModel,
                tipViewModel
            )
        }
        composable(AppRoutes.ACTIVITY) { ActivityLogScreen(navController, viewModel) }
        composable(AppRoutes.EXERCISE) { AddExerciseScreen(navController, viewModel) }
        composable(AppRoutes.FOOD) { AddFoodScreen(navController, viewModel) }
        composable(AppRoutes.PROFILE) { ProfileScreen(navController, viewModel, authViewModel) }
        composable(AppRoutes.EDIT_PROFILE) { EditProfileScreen(navController, authViewModel) }
        composable(AppRoutes.PERSONAL_INFO) { PersonalInfoScreen(navController, authViewModel) }
        composable(AppRoutes.CHANGE_PASSWORD) { ChangePasswordScreen(navController, authViewModel) }

        // 💡 UPDATE: Daftarkan Screen FAQ
        composable(AppRoutes.FAQ) {
            FAQScreen(navController)
        }

        composable(
            route = AppRoutes.DETAIL_LOG_ROUTE,
            arguments = listOf(navArgument("activityId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("activityId") ?: 0
            DetailLogScreen(navController, id, viewModel)
        }
    }
}

// ... (Helper functions tetap sama)
fun isBottomNavRoute(route: String?): Boolean {
    return route in listOf(AppRoutes.DASHBOARD, AppRoutes.ACTIVITY, AppRoutes.PROFILE)
}

fun isOverlayRoute(route: String?): Boolean {
    if (route == null) return false
    return route == AppRoutes.EXERCISE || route == AppRoutes.FOOD || route.startsWith("detail_log")
}

fun getBottomNavIndex(route: String?): Int {
    return when (route) {
        AppRoutes.DASHBOARD -> 0
        AppRoutes.ACTIVITY -> 1
        AppRoutes.PROFILE -> 2
        else -> -1
    }
}
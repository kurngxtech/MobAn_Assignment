package com.example.d1_jetpackcompose.ui.navigation

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
        startDestination = startDestination
    ) {
        composable(AppRoutes.WELCOME) { WelcomePage(navController = navController) }
        composable(AppRoutes.LOGIN) { LoginScreen(navController, authViewModel) }
        composable(AppRoutes.SIGNUP) { SignUpScreen(navController, authViewModel) }

        composable(AppRoutes.SURVEY) {
            SurveyScreen(navController = navController)
        }

        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(navController, viewModel, authViewModel)
        }
        composable(AppRoutes.ACTIVITY) { ActivityLogScreen(navController, viewModel) }
        composable(AppRoutes.EXERCISE) { AddExerciseScreen(navController, viewModel) }
        composable(AppRoutes.FOOD) { AddFoodScreen(navController, viewModel) }

        composable(AppRoutes.PROFILE) {
            // 💡 Kirim authViewModel ke sini
            ProfileScreen(navController, viewModel, authViewModel)
        }

        composable(
            route = "detail_log/{activityId}",
            arguments = listOf(navArgument("activityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("activityId") ?: 0
            DetailLogScreen(navController, id, viewModel)
        }
    }
}
package com.example.d1_jetpackcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.d1_jetpackcompose.ui.screens.ActivityLogScreen
import com.example.d1_jetpackcompose.ui.screens.AddExerciseScreen
import com.example.d1_jetpackcompose.ui.screens.AddFoodScreen
import com.example.d1_jetpackcompose.ui.screens.DashboardScreen
import com.example.d1_jetpackcompose.ui.screens.DetailLogScreen
import com.example.d1_jetpackcompose.ui.screens.LoginScreen
import com.example.d1_jetpackcompose.ui.screens.ProfileScreen
import com.example.d1_jetpackcompose.ui.screens.SignUpScreen
import com.example.d1_jetpackcompose.ui.screens.WelcomePage
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel


/**
 * Mendefinisikan rute (konstanta string) untuk setiap halaman dalam aplikasi.
 * Menggunakan object memastikan tidak ada typo dan memudahkan pengelolaan.
 */
object AppRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val ACTIVITY = "activity"
    const val PROFILE = "profile"
    const val EXERCISE = "exercise"
    const val FOOD = "food"
}

/**
 * Composable utama yang mengatur grafik navigasi.
 * Ia menentukan Composable mana yang akan ditampilkan untuk setiap rute.
 *
 * @param navController Controller yang bertanggung jawab untuk navigasi.
 * @param modifier Modifier yang diteruskan dari parent (misalnya, dari Scaffold).
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier, viewModel: SharedViewModel, authViewModel: AuthViewModel) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoutes.WELCOME, // Halaman pertama yang muncul

    ) {
        // Mendefinisikan layar untuk rute "welcome"
        composable(AppRoutes.WELCOME) {
            WelcomePage(navController = navController)
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController, authViewModel = authViewModel) // Pass VM
        }

        composable(AppRoutes.SIGNUP) {
            SignUpScreen(navController = navController, authViewModel = authViewModel) // Pass VM
        }

        // Mendefinisikan layar untuk rute "dashboard"
        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(navController = navController, viewModel = viewModel)
        }
        // Mendefinisikan layar untuk rute "profile"
        composable(AppRoutes.ACTIVITY) {
            ActivityLogScreen(navController = navController, viewModel = viewModel)
        }

        composable(AppRoutes.EXERCISE) {
            AddExerciseScreen(navController = navController, viewModel = viewModel)
        }

        composable(AppRoutes.FOOD) {
            AddFoodScreen(navController = navController, viewModel = viewModel)
        }
        // Mendefinisikan layar untuk rute "profile"
        composable(AppRoutes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = "detail_log/{activityId}",
            arguments = listOf(navArgument("activityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("activityId") ?: 0
            DetailLogScreen(
                navController = navController,
                activityId = id,
                viewModel = viewModel
            )
        }
    }
}


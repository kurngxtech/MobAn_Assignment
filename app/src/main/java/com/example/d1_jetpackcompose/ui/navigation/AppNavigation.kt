package com.example.d1_jetpackcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.screens.ActivityLogScreen
import com.example.d1_jetpackcompose.ui.screens.AddExerciseScreen
import com.example.d1_jetpackcompose.ui.screens.AddFoodScreen
import com.example.d1_jetpackcompose.ui.screens.DashboardScreen
import com.example.d1_jetpackcompose.ui.screens.WelcomePage
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

/**
 * Mendefinisikan rute (konstanta string) untuk setiap halaman dalam aplikasi.
 * Menggunakan object memastikan tidak ada typo dan memudahkan pengelolaan.
 */
object AppRoutes {
    const val WELCOME = "welcome"
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
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.DASHBOARD, // Halaman pertama yang muncul
        modifier = modifier
    ) {
        // Mendefinisikan layar untuk rute "welcome"
        composable(AppRoutes.WELCOME) {
            WelcomePage()
        }
        // Mendefinisikan layar untuk rute "dashboard"
        composable(AppRoutes.DASHBOARD) {
            DashboardScreen()
        }
        // Mendefinisikan layar untuk rute "profile"
        composable(AppRoutes.ACTIVITY) {
            ActivityLogScreen(navController = navController)
        }

        composable(AppRoutes.EXERCISE) {
            AddExerciseScreen(navController = navController)
        }

        composable(AppRoutes.FOOD) {
            AddFoodScreen(navController = navController)
        }
        // Mendefinisikan layar untuk rute "profile"
        composable(AppRoutes.PROFILE) {
        }
    }
}


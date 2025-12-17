package com.example.d1_jetpackcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.screens.ActivityLog
import com.example.d1_jetpackcompose.ui.screens.DashboardLayout
import com.example.d1_jetpackcompose.ui.screens.DistanceCountPage
import com.example.d1_jetpackcompose.ui.screens.ProfilePage
import com.example.d1_jetpackcompose.ui.screens.StepsCountPage
import com.example.d1_jetpackcompose.ui.screens.WelcomePage

/**
 * Mendefinisikan rute (konstanta string) untuk setiap halaman dalam aplikasi.
 * Menggunakan object memastikan tidak ada typo dan memudahkan pengelolaan.
 */
object AppRoutes {
    const val WELCOME = "welcome"
    const val DASHBOARD = "dashboard"
    const val ACTIVITY = "activity"
    const val PROFILE = "profile"
    const val STEPS_COUNT = "steps_count"
    const val DISTANCE_COUNT = "distance_count"
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
            DashboardLayout(navController = navController)
        }
        // Mendefinisikan layar untuk rute "profile"
        composable(AppRoutes.ACTIVITY) {
            ActivityLog()
        }
        // Mendefinisikan layar untuk rute "profile"
        composable(AppRoutes.PROFILE) {
            ProfilePage()
        }
        // Mendefinisikan layar untuk rute "steps_count"
        composable(AppRoutes.STEPS_COUNT) {
            StepsCountPage()
        }
        // Mendefinisikan layar untuk rute "distance_count"
        composable(AppRoutes.DISTANCE_COUNT){
            DistanceCountPage()
        }
    }
}

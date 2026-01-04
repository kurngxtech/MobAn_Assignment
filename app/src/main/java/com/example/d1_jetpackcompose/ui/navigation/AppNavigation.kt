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
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.AddExerciseScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.AddFoodScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.ChangePasswordScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.DetailLogScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.EditProfileScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.FAQScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.OnlineTipsListScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.PersonalInfoScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.TipDetailScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel.ActivityLogScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel.DashboardScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel.ProfileScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.surveyScreen.SurveyScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.welcomeAuthScreens.LoginScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.welcomeAuthScreens.SignUpScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.welcomeAuthScreens.WelcomePage
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

// Konstanta durasi animasi agar konsisten
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

        // 1. ENTER TRANSITION (Halaman yang baru muncul)
        enterTransition = {
            val initial = initialState.destination.route
            val target = targetState.destination.route

            if (isBottomNavRoute(initial) && isBottomNavRoute(target)) {
                // Jika antar Bottom Nav, cek indeksnya
                if (getBottomNavIndex(target) > getBottomNavIndex(initial)) {
                    // Navigasi ke depan (Kanan): Muncul dari kanan ke kiri
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(ANIM_DURATION)
                    )
                } else {
                    // Navigasi ke belakang (Kiri): Muncul dari kiri ke kanan
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(ANIM_DURATION)
                    )
                }
            } else if (isOverlayRoute(target)) {
                // Halaman Overlay (Add/Detail) muncul dari bawah
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    tween(ANIM_DURATION)
                )
            } else {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(ANIM_DURATION)
                )
            }
        },

        // 2. EXIT TRANSITION (Halaman lama yang pergi)
        exitTransition = {
            val initial = initialState.destination.route
            val target = targetState.destination.route

            if (isBottomNavRoute(initial) && isBottomNavRoute(target)) {
                if (getBottomNavIndex(target) > getBottomNavIndex(initial)) {
                    // Navigasi ke depan: Pergi ke arah kiri
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(ANIM_DURATION)
                    )
                } else {
                    // Navigasi ke belakang: Pergi ke arah kanan
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(ANIM_DURATION)
                    )
                }
            } else if (isOverlayRoute(initial)) {
                // Halaman Overlay ditutup ke arah bawah
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(ANIM_DURATION)
                )
            } else {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(ANIM_DURATION)
                )
            }
        },

        // 3. POP ENTER (Kembali ke halaman sebelumnya)
        popEnterTransition = {
            val initial = initialState.destination.route
            if (isOverlayRoute(initial)) {
                // Jika kembali dari overlay, halaman utama tetap diam
                EnterTransition.None
            } else {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(ANIM_DURATION)
                )
            }
        },

        // 4. POP EXIT (Halaman saat ini ditutup)
        popExitTransition = {
            val initial = initialState.destination.route
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

        composable(AppRoutes.FAQ) {
            FAQScreen(navController)
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
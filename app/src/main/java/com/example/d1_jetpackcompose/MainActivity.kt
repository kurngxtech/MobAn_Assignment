// File: MainActivity.kt
package com.example.d1_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.d1_jetpackcompose.ui.screens.MainScreen // <- IMPORT INI
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.d1_jetpackcompose.ui.viewModel.ThemeViewModel
import com.example.d1_jetpackcompose.ui.viewModel.ThemeViewModelFactory
import com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel.AppThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val themeViewModel = ViewModelProvider(
            this,
            ThemeViewModelFactory(applicationContext)
        )[ThemeViewModel::class.java]
        setContent {
            // 2. Observe Theme State
            val themeMode by themeViewModel.themeMode.collectAsState()

            // 3. Determine if Dark Mode should be active
            val darkTheme = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                else -> {}
            }

            // 4. Pass darkTheme boolean to your Theme Wrapper
            SmartFitTheme(darkTheme = darkTheme as Boolean) {
                // Pass themeViewModel down to MainScreen
                MainScreen(themeViewModel = themeViewModel)
            }
        }
    }
}

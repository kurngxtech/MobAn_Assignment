package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.viewModel.AuthInput
import com.example.d1_jetpackcompose.ui.viewModel.AuthResult
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.PrimaryAuthButton

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading.collectAsState()
    val loginResult by authViewModel.loginState.collectAsState()

    // 💡 OBSERVASI USER UNTUK CEK SURVEY
    val currentUser by authViewModel.currentUser.collectAsState()

    // LOGIKA LOGIN SUKSES
    LaunchedEffect(loginResult, currentUser) {
        if (loginResult is AuthResult.Success) {
            val user = currentUser
            // Tunggu sampai data user ter-load dari database
            if (user != null) {
                if (authViewModel.isSurveyCompleted(user)) {
                    // Jika data sudah ada -> Dashboard
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                } else {
                    // Jika data masih kosong ("-") -> Survey
                    navController.navigate(AppRoutes.SURVEY) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            }
        }
    }

    // Tampilkan Alert Error
    if (loginResult is AuthResult.Error) {
        val errorMsg = (loginResult as AuthResult.Error).message
        AlertDialog(
            onDismissRequest = { authViewModel.resetLoginState() },
            title = { Text("Login Failed") },
            text = { Text(errorMsg, color = Color.Black) },
            confirmButton = {
                TextButton(onClick = { authViewModel.resetLoginState() }) {
                    Text("Try Again", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(top = 40.dp),
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back\nto SmartFit",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 40.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AuthInput(
                label = "Email address",
                value = email,
                onValueChange = { email = it },
                placeholder = "Email address",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthInput(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(text = "Remember me", fontSize = 14.sp, color = Color.Gray)
                }

                Text(
                    text = "Forgot Password?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { /* Navigate to Forgot Pass */ }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            PrimaryAuthButton(
                text = "Log in",
                onClick = { authViewModel.login(email, password, rememberMe) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "New to SmartFit? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Sign up",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.SIGNUP)
                    }
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Authenticating...", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
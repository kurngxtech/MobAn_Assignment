package com.example.d1_jetpackcompose.ui.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.viewModel.*

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState()
    val signUpResult by authViewModel.signUpState.collectAsState()

    // --- 💡 POP UP LOGIC ---
    signUpResult?.let { result ->
        AlertDialog(
            onDismissRequest = { authViewModel.resetSignUpState() },
            title = { Text(if (result is AuthResult.Success) "Registration Success" else "Registration Failed") },
            text = {
                Text(
                    if (result is AuthResult.Success) result.message else (result as AuthResult.Error).message,
                    color = Color.Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (result is AuthResult.Success) {
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.SIGNUP) { inclusive = true }
                        }
                    }
                    authViewModel.resetSignUpState()
                }) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(150.dp))
            Text(
                "Create an account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(text = "Already have an account? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate(AppRoutes.LOGIN) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AuthInput(
                label = "Username",
                value = username,
                onValueChange = { username = it },
                placeholder = "Username"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthInput(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
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
            AuthInput(
                label = "Confirm Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryAuthButton(
                text = "Sign Up",
                onClick = { authViewModel.signUp(username, email, password, confirmPassword) }
            )

            // 💡 REMOVED: Divider "or sign up with" & GoogleSignInButton

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "By clicking sign up you agree to our Terms of use and Privacy policy",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- 💡 LOADING OVERLAY ---
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreenPreview() {
    val context = LocalContext.current

    // 💡 Gunakan 'remember' untuk menghindari instansiasi ulang yang menyebabkan error lint
    val authViewModel = remember {
        val database = AppDatabase.getDatabase(context)
        val repository = AuthRepository(database.userDao())
        val sharedPrefs = context.getSharedPreferences("preview_prefs", Context.MODE_PRIVATE)
        AuthViewModel(repository, sharedPrefs)
    }

    SmartFitTheme {
        SignUpScreen(
            navController = rememberNavController(),
            authViewModel = authViewModel
        )
    }
}
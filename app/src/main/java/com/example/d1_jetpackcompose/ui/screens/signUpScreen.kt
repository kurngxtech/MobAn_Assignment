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
            text = { Text(if (result is AuthResult.Success) result.message else (result as AuthResult.Error).message) },
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
                .padding(top = 40.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("Create an account", fontSize = 32.sp, fontWeight = FontWeight.Bold)

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

            AuthInput(label = "Username", value = username, onValueChange = { username = it }, placeholder = "Username")
            Spacer(modifier = Modifier.height(16.dp))
            AuthInput(label = "Email", value = email, onValueChange = { email = it }, placeholder = "Email", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            Spacer(modifier = Modifier.height(16.dp))
            AuthInput(label = "Password", value = password, onValueChange = { password = it }, placeholder = "Password", isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))
            AuthInput(label = "Confirm Password", value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Confirm Password", isPassword = true)

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryAuthButton(
                text = "Sign Up",
                onClick = { authViewModel.signUp(username, email, password, confirmPassword) } // 💡 Trigger VM
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.3f))
                Text(
                    text = "or sign up with",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.3f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            GoogleSignInButton(onClick = { /* Handle Google Sign In */ })

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
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
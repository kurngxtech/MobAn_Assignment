package com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.viewModel.AuthInput
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val focusManager = LocalFocusManager.current
    val isLoading by authViewModel.isLoading.collectAsState()

    // State untuk Input
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    // State untuk Feedback
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Validasi Form
    val isFormValid = currentPassword.isNotEmpty() &&
            newPassword.length >= 6 &&
            confirmNewPassword.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Change Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Your password must be at least 6 characters long.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Kartu Input
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Current Password
                        AuthInput(
                            label = "Current Password",
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            placeholder = "Enter current password",
                            isPassword = true
                        )

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))

                        // New Password
                        AuthInput(
                            label = "New Password",
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            placeholder = "Enter new password (min. 6 chars)",
                            isPassword = true
                        )

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))

                        // Confirm Password
                        AuthInput(
                            label = "Confirm New Password",
                            value = confirmNewPassword,
                            onValueChange = { confirmNewPassword = it },
                            placeholder = "Re-enter new password",
                            isPassword = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Error Message (Jika password lama salah / tidak cocok)
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Tombol Save
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        errorMessage = null
                        authViewModel.changePassword(
                            currentPass = currentPassword,
                            newPass = newPassword,
                            confirmPass = confirmNewPassword,
                            onSuccess = {
                                showSuccessDialog = true
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    enabled = isFormValid && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = "Save Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Loading Overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(enabled = false) {}, // Block interaksi
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {}, // Tidak bisa di-dismiss user harus klik Logout
            title = { Text("Password Changed Successfully") },
            text = {
                Text(
                    "Your password has been updated. For security reasons, please log in again with your new password.",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        authViewModel.logout()
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Login Again")
                }
            }
        )
    }
}
package com.example.d1_jetpackcompose.ui.viewModel

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.UserEntity
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- 1. VIEW MODEL LOGIC ---

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // States
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loginState = MutableStateFlow<AuthResult?>(null)
    val loginState = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow<AuthResult?>(null)
    val signUpState = _signUpState.asStateFlow()

    // Fungsi Sign Up
    fun signUp(username: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Validasi Ketat
            if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                _signUpState.value = AuthResult.Error("All fields are required")
                _isLoading.value = false
                return@launch
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _signUpState.value = AuthResult.Error("Invalid email address format")
                _isLoading.value = false
                return@launch
            }

            if (pass.length < 6) {
                _signUpState.value = AuthResult.Error("Password must be at least 6 characters")
                _isLoading.value = false
                return@launch
            }

            if (pass != confirmPass) {
                _signUpState.value = AuthResult.Error("Passwords do not match")
                _isLoading.value = false
                return@launch
            }

            // Proses ke Database
            val success = repository.registerUser(UserEntity(username = username, email = email, password = pass))

            delay(1000) // Simulasi loading sebentar

            if (success) {
                _signUpState.value = AuthResult.Success("Account created successfully!")
            } else {
                _signUpState.value = AuthResult.Error("Email already registered")
            }
            _isLoading.value = false
        }
    }

    // Fungsi Login
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Validasi Input
            if (email.isEmpty() || pass.isEmpty()) {
                _loginState.value = AuthResult.Error("Please fill in all fields")
                _isLoading.value = false
                return@launch
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _loginState.value = AuthResult.Error("Invalid email format")
                _isLoading.value = false
                return@launch
            }

            // Simulasi Loading Screen Perpindahan (2 detik sesuai request)
            delay(2000)

            val user = repository.loginUser(email, pass)

            if (user != null) {
                _loginState.value = AuthResult.Success("Login Successful! Welcome ${user.username}")
            } else {
                _loginState.value = AuthResult.Error("Wrong email or password")
            }
            _isLoading.value = false
        }
    }

    // Reset State (agar pop-up tidak muncul terus saat rotasi layar)
    fun resetLoginState() { _loginState.value = null }
    fun resetSignUpState() { _signUpState.value = null }
}

// Helper Class untuk hasil Auth
sealed class AuthResult {
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

// Factory untuk ViewModel
class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// --- 2. UI COMPONENTS (INPUT & BUTTONS) ---

@Composable
fun AuthInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = label, color = Color.Gray, fontSize = 14.sp)
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                        keyboardOptions = keyboardOptions,
                        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (isPassword) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(24.dp).clickable { passwordVisible = !passwordVisible }) {
                        Image(
                            painter = painterResource(id = R.drawable.hide_pass),
                            contentDescription = "Toggle Password",
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PrimaryAuthButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(50),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(20.dp)) {
                Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google Logo", modifier = Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Sign up with Google", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        }
    }
}
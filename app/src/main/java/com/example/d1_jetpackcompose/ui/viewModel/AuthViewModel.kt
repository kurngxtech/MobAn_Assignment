package com.example.d1_jetpackcompose.ui.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
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
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.ui.screens.compactPhone.surveyScreen.UserSurveyData
import com.example.d1_jetpackcompose.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AuthViewModel(
    private val repository: AuthRepository,
    private val activityRepository: ActivityRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    // States
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loginState = MutableStateFlow<AuthResult?>(null)
    val loginState = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow<AuthResult?>(null)
    val signUpState = _signUpState.asStateFlow()

    private val _currentUsername = MutableStateFlow("Guest")
    val currentUsername = _currentUsername.asStateFlow()

    private val _isSessionValid = MutableStateFlow(false)
    val isSessionValid = _isSessionValid.asStateFlow()

    private val _isCheckingSession = MutableStateFlow(true)
    val isCheckingSession = _isCheckingSession.asStateFlow()

    // --- REALTIME USER DATA ---
    val currentUser: StateFlow<UserEntity?> = _currentUsername
        .flatMapLatest { username -> repository.getCurrentUserFlow(username) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- 💡 FITUR BARU: CHANGE PASSWORD (HASHED) ---
    fun changePassword(
        currentPass: String,
        newPass: String,
        confirmPass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Validasi Input Dasar
            if (newPass.length < 6) {
                onError("New password must be at least 6 characters")
                _isLoading.value = false
                return@launch
            }
            if (newPass != confirmPass) {
                onError("New passwords do not match")
                _isLoading.value = false
                return@launch
            }

            // 2. Ambil data user saat ini untuk cek password lama
            val user = currentUser.value
            if (user == null) {
                onError("User session invalid")
                _isLoading.value = false
                return@launch
            }

            // 3. Verifikasi Password Lama (Dengan Hashing)
            // 🔒 Hash input user dulu, baru bandingkan dengan database
            val hashedCurrentInput = SecurityUtils.hashPassword(currentPass)

            if (user.password != hashedCurrentInput) {
                delay(1000) // Fake delay untuk keamanan (mencegah brute force cepat)
                onError("Incorrect current password")
                _isLoading.value = false
                return@launch
            }

            // 4. Proses Update Password (Dengan Hashing)
            delay(1500) // Simulasi loading sistem

            // 🔒 Hash password baru sebelum disimpan
            val hashedNewPass = SecurityUtils.hashPassword(newPass)
            val updatedUser = user.copy(password = hashedNewPass)

            repository.updateUserProfile(updatedUser)

            _isLoading.value = false
            onSuccess()
        }
    }

    // --- FUNGSI UPDATE GAMBAR ---
    fun updateProfilePicture(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            val newPath = copyImageToInternalStorage(context, imageUri)
            delay(1500)
            if (newPath != null) {
                val user = currentUser.value
                if (user != null) {
                    val updatedUser = user.copy(profilePicturePath = newPath)
                    repository.updateUserProfile(updatedUser)
                }
            }
            _isLoading.value = false
        }
    }

    private suspend fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
                val fileName = "profile_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                return@withContext file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    // --- UPDATE DATA AKUN ---
    fun updateUserAccount(
        newUsername: String,
        newAge: String,
        newGender: String,
        newHeight: String,
        newWeight: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = currentUser.value
            if (user != null) {
                val updatedUser = user.copy(
                    username = newUsername,
                    age = newAge.toIntOrNull() ?: user.age,
                    gender = newGender,
                    height = newHeight.toFloatOrNull() ?: user.height,
                    weight = newWeight.toFloatOrNull() ?: user.weight,
                    bmi = calculateBmiInternal(newHeight.toFloatOrNull(), newWeight.toFloatOrNull()) ?: user.bmi
                )
                repository.updateUserProfile(updatedUser)
                if (user.username != newUsername) {
                    sharedPreferences.edit().putString("USER_NAME", newUsername).apply()
                    _currentUsername.value = newUsername
                }
                delay(1500)
                onSuccess()
            }
            _isLoading.value = false
        }
    }

    private fun calculateBmiInternal(height: Float?, weight: Float?): Float? {
        if (height == null || weight == null || height <= 0) return null
        val heightM = height / 100f
        return weight / (heightM * heightM)
    }

    init {
        checkSession()
    }

    // LOGIKA CEK SESI
    private fun checkSession() {
        viewModelScope.launch {
            _isCheckingSession.value = true

            try {
                val savedUsername = sharedPreferences.getString("USER_NAME", null)
                val isRemembered = sharedPreferences.getBoolean("IS_REMEMBERED", false)

                delay(500)

                if (isRemembered && !savedUsername.isNullOrEmpty()) {
                    val dbUser = repository.getUserByUsername(savedUsername)

                    if (dbUser != null) {
                        _currentUsername.value = savedUsername
                        _isSessionValid.value = true
                    } else {
                        logout()
                    }
                } else {
                    _isSessionValid.value = false
                }
            } catch (e: Exception) {
                _isSessionValid.value = false
            } finally {
                _isCheckingSession.value = false
            }
        }
    }

    // Hapus Total (User + Aktivitas + Sesi)
    fun deleteAccount(onComplete: () -> Unit) {
        viewModelScope.launch {
            val username = _currentUsername.value

            // 1. Hapus User dari DB
            repository.deleteUser(username)

            // 2. Hapus SEMUA Data Aktivitas (Cascade Manual)
            activityRepository.clearUserData()

            // 3. Bersihkan Sesi Lokal
            logout()

            onComplete()
        }
    }

    fun isSurveyCompleted(user: UserEntity?): Boolean {
        return user != null && user.gender != "-" && user.gender.isNotEmpty()
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
        _currentUsername.value = "Guest"
        _isSessionValid.value = false
        _loginState.value = null
    }

    fun saveUserProfile(surveyData: UserSurveyData) {
        viewModelScope.launch {
            val username = _currentUsername.value
            val existingUser = repository.getUserByUsername(username)
            if (existingUser != null) {
                val updatedUser = existingUser.copy(
                    gender = surveyData.gender,
                    age = surveyData.age.toIntOrNull() ?: 0,
                    height = surveyData.height.toFloatOrNull() ?: 0f,
                    weight = surveyData.weight.toFloatOrNull() ?: 0f,
                    bmi = surveyData.bmi,
                    goal = surveyData.goal,
                    activityLevel = surveyData.activityLevel,
                    dailyStepsGoal = surveyData.dailySteps.toIntOrNull() ?: 5000,
                    preferredWorkout = surveyData.workoutPreference
                )
                repository.updateUserProfile(updatedUser)
            }
        }
    }

    // --- 💡 SIGN UP HASHING ---
    fun signUp(username: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch {
            _isLoading.value = true
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

            // 🔒 HASH PASSWORD SEBELUM DISIMPAN
            val hashedPass = SecurityUtils.hashPassword(pass)

            val success = repository.registerUser(UserEntity(username = username, email = email, password = hashedPass))
            delay(1000)
            if (success) _signUpState.value = AuthResult.Success("Account created successfully!")
            else _signUpState.value = AuthResult.Error("Email already registered")
            _isLoading.value = false
        }
    }

    // --- 💡 LOGIN HASHING ---
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
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
            delay(2000)

            // 🔒 HASH PASSWORD INPUT USER UNTUK PENCOCOKAN
            val hashedPass = SecurityUtils.hashPassword(pass)

            // Kirim hash ke repository untuk dicocokkan dengan DB
            val user = repository.loginUser(email, hashedPass)

            if (user != null) {
                sharedPreferences.edit().putString("USER_NAME", user.username).putBoolean("IS_REMEMBERED", true).apply()
                _currentUsername.value = user.username
                _isSessionValid.value = true
                _loginState.value = AuthResult.Success("Login Successful! Welcome ${user.username}")
            } else {
                _loginState.value = AuthResult.Error("Wrong email or password")
            }
            _isLoading.value = false
        }
    }

    fun resetLoginState() { _loginState.value = null }
    fun resetSignUpState() { _signUpState.value = null }
}

sealed class AuthResult {
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val activityRepository: ActivityRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, activityRepository, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// --- REUSABLE COMPONENT (PUBLIC) ---
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
            modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(text = label, color = Color.Gray, fontSize = 14.sp)
                    BasicTextField(
                        value = value, onValueChange = onValueChange,
                        textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                        keyboardOptions = keyboardOptions,
                        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                        singleLine = true, modifier = Modifier.fillMaxWidth()
                    )
                }
                if (isPassword) {
                    val iconId = if (passwordVisible) R.drawable.view_pass else R.drawable.hide_pass

                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = "Toggle Password",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { passwordVisible = !passwordVisible },
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            }
        }
    }
}

@Composable
fun PrimaryAuthButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick, modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
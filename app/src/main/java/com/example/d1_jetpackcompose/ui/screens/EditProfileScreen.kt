package com.example.d1_jetpackcompose.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    // State untuk Form
    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    // State untuk path gambar (preview lokal)
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }

    // Isi form saat data user tersedia
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            username = user.username
            age = if (user.age > 0) user.age.toString() else ""
            gender = if (user.gender.isNotEmpty() && user.gender != "-") user.gender else "Male"
            height = if (user.height > 0) user.height.toInt().toString() else ""
            weight = if (user.weight > 0) user.weight.toInt().toString() else ""
            currentPhotoPath = user.profilePicturePath
        }
    }

    // --- 1. PHOTO PICKER LAUNCHER ---
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                // Proses gambar via ViewModel
                authViewModel.updateProfilePicture(context, uri)
            }
        }
    )

    // --- 2. PERMISSION LAUNCHER (Untuk Pop-up Trust) ---
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Jika diizinkan, buka picker
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                Toast.makeText(context, "Permission Denied. Cannot change photo.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 8.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }

            Text(
                text = "Edit Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A6741)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. PROFILE PICTURE SECTION ---
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    // Gunakan AsyncImage dari Coil
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(currentPhotoPath ?: R.drawable.profile_picture) // Path file atau Default Res
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.profile_picture),
                        error = painterResource(R.drawable.profile_picture),
                        contentDescription = "Profile Pic",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Tombol Edit Foto
                Box(
                    modifier = Modifier
                        .offset(x = 2.dp, y = 2.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            // 💡 LOGIC TRUST POP-UP: Cek versi Android
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                // Android 13+ pake Photo Picker langsung (Permission less but secure)
                                // Tapi jika mau maksa permission check:
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                // Android lama butuh READ_EXTERNAL_STORAGE
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.edit_profile2),
                        contentDescription = "Edit Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- FORM CARD ---
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    EditProfileInput(label = "Username", value = username, onValueChange = { username = it })
                    EditProfileInput(label = "Age", value = age, onValueChange = { age = it }, isNumber = true)

                    Column {
                        Text(text = "Gender", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0E0E0))
                        ) {
                            GenderSegmentButton("Male", gender == "Male", Modifier.weight(1f)) { gender = "Male" }
                            GenderSegmentButton("Female", gender == "Female", Modifier.weight(1f)) { gender = "Female" }
                        }
                    }

                    EditProfileInput(label = "Height", value = height, onValueChange = { height = it }, isNumber = true, suffix = "cm")
                    EditProfileInput(label = "Weight", value = weight, onValueChange = { weight = it }, isNumber = true, suffix = "kg")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    authViewModel.updateUserAccount(
                        newUsername = username,
                        newAge = age,
                        newGender = gender,
                        newHeight = height,
                        newWeight = weight,
                        onSuccess = { navController.popBackStack() }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- LOADING SCREEN ---
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Processing...", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EditProfileInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isNumber: Boolean = false,
    suffix: String = ""
) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    if (suffix.isNotEmpty()) {
                        Text(text = suffix, color = Color.Gray, fontWeight = FontWeight.Normal)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))
    }
}

@Composable
fun GenderSegmentButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    val context = LocalContext.current
    val authViewModel = remember {
        val database = AppDatabase.getDatabase(context)
        val authRepo = AuthRepository(database.userDao())
        val sharedPrefs = context.getSharedPreferences("preview_prefs", Context.MODE_PRIVATE)
        AuthViewModel(authRepo, sharedPrefs)
    }
    SmartFitTheme {
        EditProfileScreen(navController = rememberNavController(), authViewModel = authViewModel)
    }
}
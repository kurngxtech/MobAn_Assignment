package com.example.d1_jetpackcompose.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

val CardWhite = Color.White

@SuppressLint("DefaultLocale")
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel
) {
    // 1. OBSERVASI DATA USER REALTIME
    val user by authViewModel.currentUser.collectAsState()

    // State Dialog
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER ---
        Text(
            text = "My Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- PROFILE IMAGE & NAME ---
        Box(modifier = Modifier.size(120.dp).clip(CircleShape)) {
            // 💡 GANTI IMAGE BIASA DENGAN ASYNC IMAGE
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user?.profilePicturePath ?: R.drawable.profile_picture) // Prioritas Path file, fallback ke Resource
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.profile_picture),
                error = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // NAMA DINAMIS
        Text(
            text = user?.username ?: "Guest",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 8.dp,
                hoveredElevation = 6.dp,
                focusedElevation = 6.dp
            ),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(text = "Edit Profile", fontSize = 12.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- STATS CARDS DINAMIS ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileStatCard(label = "Height", value = "${user?.height?.toInt() ?: 0} cm", modifier = Modifier.weight(1f))
            ProfileStatCard(label = "Weight", value = "${user?.weight?.toInt() ?: 0} kg", modifier = Modifier.weight(1f))
            ProfileStatCard(label = "Age", value = "${user?.age ?: 0} y", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileStatCard(label = "Gender", value = user?.gender ?: "-", modifier = Modifier.weight(1f))

            // --- KARTU BMI DENGAN WARNA DINAMIS ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier.weight(1f).height(110.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val bmi = user?.bmi ?: 0f

                    // Logic Label Kategori
                    val bmiLabel = when {
                        bmi < 18.5 -> "Underweight"
                        bmi < 25.0 -> "Normal"
                        bmi < 30.0 -> "Overweight"
                        else -> "Obese"
                    }

                    // 💡 LOGIC WARNA (Sesuai Request)
                    val bmiColor = when {
                        bmi < 18.5 -> Color(0xFF1A237E) // 1. Biru Tua Elegan (Underweight)
                        bmi < 25.0 -> MaterialTheme.colorScheme.primary // 2. Hijau (Normal)
                        else -> Color(0xFFE53935) // 3. Merah Menyala (Overweight & Obese)
                    }

                    Text(text = "BMI $bmiLabel", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { (bmi / 40f).coerceIn(0f, 1f) },
                            modifier = Modifier.size(60.dp),
                            color = bmiColor, // 💡 Terapkan warna dinamis di sini
                            strokeWidth = 6.dp,
                            trackColor = Color(0xFFE0E0E0),
                            strokeCap = StrokeCap.Round,
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("18,5 - 24,9", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = String.format("%.1f", bmi), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- ACCOUNT & HELP ---
        Text("Account", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        MenuContainer {
            MenuItem(R.drawable.user_logo, "Personal Info") {}
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(R.drawable.password_logo, "Change Password") {}
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Help", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // --- MENU 3 ITEM (FAQ - DELETE - SIGN OUT) ---
        MenuContainer {
            // 1. FAQ
            MenuItem(R.drawable.faq_logo, "FAQ") {}
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)

            // 2. DELETE ACCOUNT (Custom Image Manual)
            MenuItem(
                iconId = 0, // Ignored
                text = "Delete Account",
                isDestructive = true,
                useCustomImage = true,
                onClick = { showDeleteDialog = true }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.1f))

            // 3. SIGN OUT
            MenuItem(
                iconId = R.drawable.logout_logo,
                text = "Sign Out",
                isDestructive = true,
                onClick = {
                    viewModel.logout()
                    authViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) { popUpTo(0) { inclusive = true } }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    // --- DIALOG CONFIRMATION ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { Text("This action is permanent. All your data will be lost and you must register again to use this email.", color = Color.Black) },
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.deleteAccount {
                        navController.navigate(AppRoutes.WELCOME) { popUpTo(0) { inclusive = true } }
                    }
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ProfileStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MenuContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

// --- UPDATED MENU ITEM ---
@Composable
fun MenuItem(
    iconId: Int,
    text: String,
    isDestructive: Boolean = false,
    useCustomImage: Boolean = false, // Param baru
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logika Wadah Ikon
        if (useCustomImage) {
            Image(
                painter = painterResource(id = R.drawable.delete_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                // Gunakan ColorFilter.tint untuk mengubah warna Image
                colorFilter = if (isDestructive) {
                    ColorFilter.tint(MaterialTheme.colorScheme.error)
                } else {
                    null // Tetap warna asli jika tidak destructive
                }
            )
        } else {
            Icon(
                painter = painterResource(iconId), contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text, fontSize = 16.sp, fontWeight = FontWeight.Medium,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Go", tint = Color.Gray, modifier = Modifier.size(24.dp))
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun ProfileScreenPreview() {
    val context = LocalContext.current

    // 💡 Inisialisasi ViewModel khusus untuk Preview agar tidak error
    val authViewModel = remember {
        val database = AppDatabase.getDatabase(context)
        val authRepo = AuthRepository(database.userDao())
        val sharedPrefs = context.getSharedPreferences("preview_prefs", Context.MODE_PRIVATE)
        AuthViewModel(authRepo, sharedPrefs)
    }

    val sharedViewModel = remember {
        val database = AppDatabase.getDatabase(context)
        val activityRepo = ActivityRepository(database.activityDao())
        SharedViewModel(activityRepo)
    }

    SmartFitTheme {
        ProfileScreen(
            navController = rememberNavController(),
            viewModel = sharedViewModel,
            authViewModel = authViewModel
        )
    }
}
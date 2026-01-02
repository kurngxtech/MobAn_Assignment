package com.example.d1_jetpackcompose.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.composed
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
val InactiveTrackGrayProfile = Color(0xFFF5F5F5)

// UI State untuk Tema
enum class AppThemeMode { LIGHT, DARK, SYSTEM }

private fun Modifier.noRippleClickableProfile(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun ProfileScreen(
    navController: NavController, viewModel: SharedViewModel, authViewModel: AuthViewModel
) {
    val user by authViewModel.currentUser.collectAsState()

    // --- STATE DIALOG ---
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) } // 💡 Dialog Logout

    var selectedTheme by remember { mutableStateOf(AppThemeMode.LIGHT) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        // --- HEADER ---
        Text(
            text = "My Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- PROFILE PICTURE ---
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user?.profilePicturePath ?: R.drawable.profile_picture).crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.profile_picture),
                error = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = user?.username ?: "Guest",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(text = "Edit Profile", fontSize = 12.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(25.dp))

        // --- STATS ---
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(
                label = "Height",
                value = "${user?.height?.toInt() ?: 0} cm",
                modifier = Modifier.weight(1f)
            )
            ProfileStatCard(
                label = "Weight",
                value = "${user?.weight?.toInt() ?: 0} kg",
                modifier = Modifier.weight(1f)
            )
            ProfileStatCard(
                label = "Age", value = "${user?.age ?: 0} y", modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(
                label = "Gender", value = user?.gender ?: "-", modifier = Modifier.weight(1f)
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val bmi = user?.bmi ?: 0f
                    val bmiLabel = when {
                        bmi < 18.5 -> "Underweight"
                        bmi < 25.0 -> "Normal"
                        bmi < 30.0 -> "Overweight"
                        else -> "Obese"
                    }
                    val bmiColor = when {
                        bmi < 18.5 -> Color(0xFF1A237E)
                        bmi < 25.0 -> MaterialTheme.colorScheme.primary
                        else -> Color(0xFFE53935)
                    }
                    Text(
                        text = "BMI $bmiLabel",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { (bmi / 40f).coerceIn(0f, 1f) },
                            modifier = Modifier.size(60.dp),
                            color = bmiColor,
                            strokeWidth = 6.dp,
                            trackColor = Color(0xFFE0E0E0),
                            strokeCap = StrokeCap.Round,
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "18,5 - 24,9",
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = String.format("%.1f", bmi),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // --- ACCOUNT ---
        Text("Account", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        MenuContainer {
            MenuItem(R.drawable.user_logo, "Personal Info") {}
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(R.drawable.password_logo, "Change Password") {}
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- 🧩 SLIDING APPEARANCE SELECTOR ---
        Text("Theme", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        SlidingThemeSelector(
            selectedTheme = selectedTheme,
            onThemeSelected = { selectedTheme = it },

            )
        Spacer(modifier = Modifier.height(24.dp))

        // --- HELP ---
        Text("Help", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        MenuContainer {
            MenuItem(R.drawable.faq_logo, "FAQ") {}
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(
                iconId = 0,
                text = "Delete Account",
                isDestructive = true,
                useCustomImage = true,
                onClick = { showDeleteDialog = true })
            HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.1f))
            MenuItem(
                iconId = R.drawable.logout_logo,
                text = "Sign Out",
                isDestructive = true,
                onClick = {
                    // 💡 Memunculkan Popup Logout bukan langsung keluar
                    showLogoutDialog = true
                })
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    // --- POPUP DELETE ACCOUNT ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = {
                Text(
                    "This action is permanent. All your data will be lost.", color = Color.Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.deleteAccount {
                        navController.navigate(
                            AppRoutes.WELCOME
                        ) { popUpTo(0) { inclusive = true } }
                    }; showDeleteDialog = false
                }) { Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        "Cancel", color = Color.Gray
                    )
                }
            })
    }

    // --- 💡 POPUP LOGOUT (MIRIP DELETE) ---
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out?") },
            text = {
                Text(
                    "Are you sure you want to sign out?", color = Color.Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout()
                    authViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                    showLogoutDialog = false
                }) {
                    // Warna merah untuk aksi keluar
                    Text("Sign Out", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            })
    }
}

// ... (Sisa komponen SlidingThemeSelector, ProfileStatCard, MenuContainer, MenuItem dll tetap sama)
@Composable
fun SlidingThemeSelector(
    selectedTheme: AppThemeMode, onThemeSelected: (AppThemeMode) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Appearance",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Container Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(InactiveTrackGrayProfile)
                    .padding(4.dp)
            ) {
                // Konfigurasi Item (Text & Icon Placeholder)
                val items = listOf(
                    Triple("Light", AppThemeMode.LIGHT, R.drawable.light_mode),
                    Triple("Dark", AppThemeMode.DARK, R.drawable.dark_mode),
                    Triple("System", AppThemeMode.SYSTEM, R.drawable.settings_icon)
                )

                // Hitung Index Aktif
                val selectedIndex = when (selectedTheme) {
                    AppThemeMode.LIGHT -> 0
                    AppThemeMode.DARK -> 1
                    AppThemeMode.SYSTEM -> 2
                }

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val itemWidth = maxWidth / 3

                    // 1. ANIMASI SLIDING (HIJAU)
                    val indicatorOffset by animateDpAsState(
                        targetValue = itemWidth * selectedIndex,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "themeSlide"
                    )

                    // LAYER 1: INDICATOR
                    Box(
                        modifier = Modifier
                            .width(itemWidth)
                            .fillMaxHeight()
                            .offset(x = indicatorOffset)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary)
                    )

                    // LAYER 2: ITEM CONTENT (TEXT + ICON)
                    Row(modifier = Modifier.fillMaxSize()) {
                        items.forEachIndexed { index, (text, mode, iconRes) ->
                            Box(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .fillMaxHeight()
                                    .noRippleClickableProfile { onThemeSelected(mode) },
                                contentAlignment = Alignment.Center
                            ) {
                                val contentColor by animateColorAsState(
                                    targetValue = if (index == selectedIndex) Color.White else Color.Gray,
                                    animationSpec = tween(durationMillis = 200),
                                    label = "contentColor"
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Placeholder Icon (Bisa diganti nanti)
                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(contentColor),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = text,
                                        color = contentColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
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

@Composable
fun MenuItem(
    iconId: Int,
    text: String,
    isDestructive: Boolean = false,
    useCustomImage: Boolean = false,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .noRippleClickableProfile { onClick() }
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if (useCustomImage) {
            Image(
                painter = painterResource(id = R.drawable.delete_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = if (isDestructive) ColorFilter.tint(MaterialTheme.colorScheme.error) else null
            )
        } else {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            "Go",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun ProfileScreenPreview() {
    val context = LocalContext.current
    val authViewModel = remember {
        val database = AppDatabase.getDatabase(context)
        val authRepo = AuthRepository(database.userDao())
        val activityRepo = ActivityRepository(database.activityDao()) // Mock
        val sharedPrefs = context.getSharedPreferences("preview_prefs", Context.MODE_PRIVATE)
        AuthViewModel(authRepo, activityRepo, sharedPrefs)
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
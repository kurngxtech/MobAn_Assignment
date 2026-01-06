package com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.ChangePasswordScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.EditProfileScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.FAQScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.PersonalInfoScreen
import com.example.d1_jetpackcompose.ui.theme.LocalAppDimens
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.ThemeViewModel

// ... (Enum & Helper Extensions sama) ...
enum class AppThemeMode { LIGHT, DARK, SYSTEM }
enum class ProfilePanelState { NONE, EDIT_PROFILE, PERSONAL_INFO, CHANGE_PASSWORD, FAQ }

private fun Modifier.noRippleClickableProfile(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

@SuppressLint(
    "DefaultLocale",
    "UnusedBoxWithConstraintsScope",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel
) {
    val dimens = LocalAppDimens.current
    val isTablet = dimens.isTablet

    var panelState by remember { mutableStateOf(ProfilePanelState.NONE) }
    val showSplitView = panelState != ProfilePanelState.NONE
    val pageScrollState = rememberScrollState()
    val sideNavController = rememberNavController()
    val horizontalPadding = dimens.screenPadding + 5.dp

    LaunchedEffect(panelState) {
        if (panelState != ProfilePanelState.NONE) {
            val route = when (panelState) {
                ProfilePanelState.EDIT_PROFILE -> AppRoutes.EDIT_PROFILE
                ProfilePanelState.PERSONAL_INFO -> AppRoutes.PERSONAL_INFO
                ProfilePanelState.CHANGE_PASSWORD -> AppRoutes.CHANGE_PASSWORD
                ProfilePanelState.FAQ -> AppRoutes.FAQ
                else -> null
            }
            if (route != null) sideNavController.navigate(route) {
                popUpTo("blank") {
                    inclusive = false
                }; launchSingleTop = true
            }
        }
    }

    DisposableEffect(sideNavController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.route == "blank") panelState = ProfilePanelState.NONE
        }
        sideNavController.addOnDestinationChangedListener(listener)
        onDispose { sideNavController.removeOnDestinationChangedListener(listener) }
    }

    val profileTopBar: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimens.topPaddingScaffold) // Jarak dari atas layar
                .padding(horizontal = horizontalPadding) // Sejajar dengan konten (+5dp logic)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimens.paddingMedium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Profile",
                    fontSize = if (isTablet) dimens.textSizeHeadline else 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isTablet) {
            Row(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = profileTopBar,
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues) // Konten mulai setelah TopBar
                            .verticalScroll(pageScrollState),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        val contentModifier = if (showSplitView) {
                            Modifier
                                .fillMaxSize()
                                .padding(end = dimens.paddingMedium)
                        } else {
                            // PERBAIKAN: Center Alignment
                            Modifier
                                .fillMaxWidth(dimens.contentMaxWidthPercent)
                                .align(Alignment.Center)
                        }

                        Box(modifier = contentModifier) {
                            ProfilePageContent(
                                navController = navController,
                                themeViewModel = themeViewModel,
                                viewModel = viewModel,
                                authViewModel = authViewModel,
                                isTablet = true,
                                onNavigateRequest = { route ->
                                    when (route) {
                                        AppRoutes.EDIT_PROFILE -> panelState =
                                            ProfilePanelState.EDIT_PROFILE

                                        AppRoutes.PERSONAL_INFO -> panelState =
                                            ProfilePanelState.PERSONAL_INFO

                                        AppRoutes.CHANGE_PASSWORD -> panelState =
                                            ProfilePanelState.CHANGE_PASSWORD

                                        AppRoutes.FAQ -> panelState = ProfilePanelState.FAQ
                                    }
                                }
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showSplitView,
                    enter = slideInHorizontally { it } + fadeIn(),
                    exit = slideOutHorizontally { it } + fadeOut(),
                    modifier = Modifier
                        .width(450.dp)
                        .fillMaxHeight() // PERBAIKAN: Fill Max Height
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = dimens.screenPadding)
                            .padding(end = dimens.screenPadding)
                            .clip(RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        NavHost(navController = sideNavController, startDestination = "blank") {
                            composable("blank") { Box(modifier = Modifier.fillMaxSize()) }
                            composable(AppRoutes.EDIT_PROFILE) {
                                EditProfileScreen(
                                    navController = sideNavController,
                                    authViewModel = authViewModel
                                )
                            }
                            composable(AppRoutes.PERSONAL_INFO) {
                                PersonalInfoScreen(
                                    navController = sideNavController,
                                    authViewModel = authViewModel
                                )
                            }
                            composable(AppRoutes.CHANGE_PASSWORD) {
                                ChangePasswordScreen(
                                    navController = sideNavController,
                                    authViewModel = authViewModel
                                )
                            }
                            composable(AppRoutes.FAQ) { FAQScreen(navController = sideNavController) }
                        }
                    }
                }
            }
        } else {
            // HP Layout: Remove padding 20.dp here because it's now inside ProfilePageContent
            Scaffold(
                topBar = profileTopBar,
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxSize()
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(bottom = 20.dp)
                        .verticalScroll(pageScrollState)
                ) {
                    ProfilePageContent(
                        navController = navController,
                        viewModel = viewModel,
                        authViewModel = authViewModel,
                        themeViewModel = themeViewModel,
                        isTablet = false,
                        onNavigateRequest = { route -> navController.navigate(route) }
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun ProfilePageContent(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel,
    isTablet: Boolean,
    onNavigateRequest: (String) -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    val dimens = LocalAppDimens.current
    val selectedTheme by themeViewModel.themeMode.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // PERBAIKAN: Menambahkan Konsistensi Padding
    val horizontalPadding = dimens.screenPadding + 5.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = horizontalPadding,
                end = horizontalPadding,
                top = dimens.screenPadding,
                bottom = dimens.screenPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (isTablet) 140.dp else 120.dp)
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
            fontSize = if (isTablet) 28.sp else 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onNavigateRequest(AppRoutes.EDIT_PROFILE) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(text = "Edit Profile", fontSize = 12.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                label = "Age",
                value = "${user?.age ?: 0} y",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(
                label = "Gender",
                value = user?.gender ?: "-",
                modifier = Modifier.weight(1f)
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
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
                        bmi < 18.5 -> "Underweight"; bmi < 25.0 -> "Normal"; bmi < 30.0 -> "Overweight"; else -> "Obese"
                    }
                    val bmiColor = when {
                        bmi < 18.5 -> Color(0xFF1A237E); bmi < 25.0 -> MaterialTheme.colorScheme.primary; else -> Color(
                            0xFFE53935
                        )
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
                            trackColor = MaterialTheme.colorScheme.background,
                            strokeCap = StrokeCap.Round
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

        Text("Account", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        MenuContainer {
            MenuItem(
                R.drawable.user_logo,
                "Personal Info"
            ) { onNavigateRequest(AppRoutes.PERSONAL_INFO) }
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(
                R.drawable.password_logo,
                "Change Password"
            ) { onNavigateRequest(AppRoutes.CHANGE_PASSWORD) }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Theme", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        SlidingThemeSelector(
            selectedTheme = selectedTheme, // 💡 Use observed state
            onThemeSelected = { newMode ->
                themeViewModel.setThemeMode(newMode) // 💡 Trigger change
            },
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Help", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        MenuContainer {
            MenuItem(R.drawable.faq_logo, "FAQ") { onNavigateRequest(AppRoutes.FAQ) }
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
                onClick = { showLogoutDialog = true })
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Account?") },
                text = {
                    Text(
                        "This action is permanent. All your data will be lost.",
                        color = MaterialTheme.colorScheme.onSurface
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
                    TextButton(onClick = {
                        showDeleteDialog = false
                    }) { Text("Cancel", color = Color.Gray) }
                }
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Sign Out?") },
                text = {
                    Text(
                        "Are you sure you want to sign out?",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.logout(); authViewModel.logout(); navController.navigate(
                        AppRoutes.LOGIN
                    ) { popUpTo(0) { inclusive = true } }; showLogoutDialog = false
                    }) { Text("Sign Out", color = Color.Red, fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                    }) { Text("Cancel", color = Color.Gray) }
                }
            )
        }
    }
}

// =========================================================================
// EXISTING COMPONENTS (DIAMBIL DARI KODE ASLI - TIDAK DIUBAH)
// =========================================================================

@Composable
fun SlidingThemeSelector(
    selectedTheme: AppThemeMode, onThemeSelected: (AppThemeMode) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
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
                    .background(MaterialTheme.colorScheme.surface)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
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
    Row(
        modifier = Modifier
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
package com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel

import android.R.attr.bottom
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.AddExerciseScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.AddFoodScreen
import com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel.DetailLogScreen
import com.example.d1_jetpackcompose.ui.theme.LocalAppDimens
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- THEME SETUP ---
private val HistoryCardGray = Color(0xFFE8E8E8)

// Extension No Ripple
private fun Modifier.noRippleClickableActivity(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

// --- MODIFIER CUSTOM SHADOW ---
fun Modifier.customDropShadowActivity(
    color: Color = Color.Black.copy(alpha = 0.2f),
    borderRadius: Dp = 24.dp,
    blurRadius: Dp = 8.dp,
    offsetY: Dp = 4.dp
) = this.drawBehind {
    this.drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = android.graphics.Color.TRANSPARENT
        frameworkPaint.style = android.graphics.Paint.Style.FILL
        frameworkPaint.setShadowLayer(
            blurRadius.toPx(),
            0f,
            offsetY.toPx(),
            color.toArgb()
        )
        val outline = RoundedCornerShape(borderRadius).createOutline(size, layoutDirection, this)
        canvas.drawOutline(outline = outline, paint = paint)
    }
}

// Enum untuk Status Panel Tablet
enum class ActivityPanelState { NONE, ADD_EXERCISE, ADD_FOOD, DETAIL }

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ActivityLogScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    // 1. Deteksi Device
    val dimens = LocalAppDimens.current
    val isTablet = dimens.isTablet

    // 2. State untuk Tablet Logic
    var panelState by remember { mutableStateOf(ActivityPanelState.NONE) }
    var selectedActivityId by remember { mutableStateOf<Int?>(null) }
    val showSplitView = panelState != ActivityPanelState.NONE

    // State Scroll Global untuk masing-masing layout
    val pageScrollState = rememberScrollState()

    // 3. INTERNAL NAVIGATION CONTROLLER (Untuk Panel Kanan)
    val sideNavController = rememberNavController()

    // Logika Sinkronisasi State -> Navigasi
    LaunchedEffect(panelState) {
        if (panelState != ActivityPanelState.NONE) {
            val route = when (panelState) {
                ActivityPanelState.ADD_EXERCISE -> "add_exercise"
                ActivityPanelState.ADD_FOOD -> "add_food"
                ActivityPanelState.DETAIL -> "detail_log"
                else -> null
            }
            if (route != null) {
                sideNavController.navigate(route) {
                    popUpTo("blank") { inclusive = false }
                    launchSingleTop = true
                }
            }
        }
    }

    // Logika Mendeteksi "Back" (Dari Form -> Blank) -> Tutup Panel
    DisposableEffect(sideNavController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.route == "blank") {
                panelState = ActivityPanelState.NONE
            }
        }
        sideNavController.addOnDestinationChangedListener(listener)
        onDispose { sideNavController.removeOnDestinationChangedListener(listener) }
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isTablet) {
            // ==========================================
            // TABLET LAYOUT (SPLIT VIEW)
            // ==========================================
            Row(modifier = Modifier.fillMaxSize()) {

                // --- PANEL KIRI (LIST ACTIVITY) ---
                // 💡 TASK: Vertical Scroll ditaruh di sini agar seluruh area kiri bisa di-scroll
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(pageScrollState) // SCROLL GLOBAL AKTIF DI SINI
                ) {
                    val contentModifier = if (showSplitView) {
                        Modifier
                            .fillMaxSize()
                            .padding(end = dimens.paddingMedium) // Beri jarak dengan panel kanan
                    } else {
                        Modifier
                            .fillMaxWidth(dimens.contentMaxWidthPercent) // 60% Width
                            .align(Alignment.Center)
                    }

                    Box(modifier = contentModifier) {
                        ActivityLogContent(
                            viewModel = viewModel,
                            navController = navController,
                            onAddExercise = { panelState = ActivityPanelState.ADD_EXERCISE },
                            onAddFood = { panelState = ActivityPanelState.ADD_FOOD },
                            onDetailClick = { id ->
                                selectedActivityId = id
                                panelState = ActivityPanelState.DETAIL
                            },
                            isTablet = true
                        )
                    }
                }

                // --- PANEL KANAN (SLIDING FORM/DETAIL) ---
                AnimatedVisibility(
                    visible = showSplitView,
                    enter = slideInHorizontally { it } + fadeIn(),
                    exit = slideOutHorizontally { it } + fadeOut(),
                    modifier = Modifier
                        .width(450.dp)
                        .fillMaxHeight()
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
                            composable("add_exercise") {
                                AddExerciseScreen(
                                    navController = sideNavController,
                                    viewModel = viewModel
                                )
                            }
                            composable("add_food") {
                                AddFoodScreen(
                                    navController = sideNavController,
                                    viewModel = viewModel
                                )
                            }
                            composable("detail_log") {
                                if (selectedActivityId != null) {
                                    DetailLogScreen(
                                        navController = sideNavController,
                                        activityId = selectedActivityId!!,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // ==========================================
            // MOBILE LAYOUT (STANDARD)
            // ==========================================
            // 💡 TASK: Vertical Scroll ditaruh di container utama
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .verticalScroll(pageScrollState) // SCROLL GLOBAL AKTIF DI SINI
            ) {
                ActivityLogContent(
                    viewModel = viewModel,
                    navController = navController,
                    onAddExercise = { navController.navigate(AppRoutes.EXERCISE) },
                    onAddFood = { navController.navigate(AppRoutes.FOOD) },
                    onDetailClick = { id -> navController.navigate("detail_log/$id") },
                    isTablet = false
                )
            }
        }
    }
}

// =========================================================================
// REUSABLE CONTENT
// =========================================================================
@Composable
fun ActivityLogContent(
    viewModel: SharedViewModel,
    navController: NavController,
    onAddExercise: () -> Unit,
    onAddFood: () -> Unit,
    onDetailClick: (Int) -> Unit,
    isTablet: Boolean
) {
    val activityList by viewModel.activityLogList.collectAsState()
    val currentPeriod by viewModel.selectedPeriod.collectAsState()
    val dimens = LocalAppDimens.current

    // 💡 TASK: Column ini tidak lagi memiliki .verticalScroll(), hanya layouting
    Column(
        modifier = Modifier
            .fillMaxWidth() // Gunakan fillMaxWidth agar aman di dalam scrollable parent
            .padding(if (isTablet) dimens.screenPadding else 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isTablet) Spacer(modifier = Modifier.height(60.dp))

        // --- HEADER ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Activities",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = if (isTablet) dimens.textSizeHeadline else 32.sp,
            )
            Text(
                text = "You can choose either edit or add new activity",
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                fontSize = if (isTablet) dimens.textSizeBody else 15.sp,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        // --- TOP CARDS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val cardModifier = if (isTablet) {
                Modifier
                    .weight(1f)
                    .height(170.dp) // Fixed Height Tablet
            } else {
                Modifier
                    .weight(1f)
                    .aspectRatio(1f) // Square Mobile
            }

            TopActionCard(
                title = "Add Exercise",
                subtitle = "Running or Walking",
                modifier = cardModifier,
                iconRes = R.drawable.walk_icon,
                onClick = onAddExercise
            )
            TopActionCard(
                title = "Add Food",
                subtitle = "Input meals and calories",
                modifier = cardModifier,
                iconRes = R.drawable.add_food_icon,
                onClick = onAddFood
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION TITLE ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "History Activities",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = if (isTablet) dimens.textSizeTitle else 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            PeriodSelector(
                selectedPeriod = currentPeriod,
                onSelect = { viewModel.setTimePeriod(it) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- HISTORY LIST CARD ---
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                // Logika Tinggi (Tablet vs HP)
                .then(
                    if (isTablet) {
                        Modifier.wrapContentHeight()
                    } else {
                        Modifier.height(450.dp) // Sedikit dipertinggi agar lebih lega
                    }
                )
        ) {
            // Container Utama di dalam Card Putih
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // PERBAIKAN: Berikan padding rata di semua sisi (atas, bawah, kiri, kanan)
                    // Ini memberikan "margin" agar shadow item tidak terpotong sisi card putih
                    .padding(24.dp)
            ) {
                // Logika Scroll Internal (Hanya HP)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .then(
                            if (!isTablet) {
                                Modifier.verticalScroll(rememberScrollState())
                            } else {
                                Modifier
                            }
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar item lebih renggang
                ) {
                    if (activityList.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "No activities found\n Add any activities you want.",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        activityList.forEach { activity ->
                            val dateString = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(activity.timestamp))
                            val typeLabel = if (activity.type == ActivityType.FOOD) "Intake" else "Burned"
                            val icon = if (activity.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon

                            // Item History
                            HistoryItem(
                                title = activity.title,
                                subtitle = "$dateString | ${activity.calories} cal $typeLabel",
                                iconRes = icon,
                                modifier = Modifier
                                    // Pastikan modifier item mengisi ruang yang disediakan padding
                                    .fillMaxWidth()
                                    .noRippleClickableActivity {
                                        onDetailClick(activity.id)
                                    }
                            )
                        }

                        // Tambahan Spacer di paling bawah list agar item terakhir shadow-nya tidak kepotong bawah
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TopActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .noRippleClickableActivity(onClick = onClick)
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 32.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
            .background(Color.White, RoundedCornerShape(32.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center
            ) {
                if (iconRes != null) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 24.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
            .background(HistoryCardGray, RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconRes != null) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = title,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = subtitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.right_arrow_icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
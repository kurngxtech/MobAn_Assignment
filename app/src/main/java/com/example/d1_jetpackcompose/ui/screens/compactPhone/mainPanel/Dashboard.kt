package com.example.d1_jetpackcompose.ui.screens.compactPhone.mainPanel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.local.UserEntity
import com.example.d1_jetpackcompose.data.remote.model.HealthTip
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.data.repository.TipsRepository
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.LocalAppDimens
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.CategoryFilter
import com.example.d1_jetpackcompose.ui.viewModel.DashboardStats
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.TimePeriod
import com.example.d1_jetpackcompose.ui.viewModel.TipsViewModel
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

private val HistoryCardGray = Color(0xFFE8E8E8)

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

fun Modifier.customDropShadow(
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
        frameworkPaint.setShadowLayer(blurRadius.toPx(), 0f, offsetY.toPx(), color.toArgb())
        val outline = RoundedCornerShape(borderRadius).createOutline(size, layoutDirection, this)
        canvas.drawOutline(outline = outline, paint = paint)
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    tipViewModel: TipsViewModel
) {
    val dimens = LocalAppDimens.current
    val isTablet = dimens.isTablet

    LaunchedEffect(Unit) { tipViewModel.fetchTips() }
    val stats by viewModel.dashboardStats.collectAsState()
    val currentPeriod by viewModel.selectedPeriod.collectAsState()
    val currentCategory by viewModel.selectedCategory.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val personalizedTips by tipViewModel.personalizedTips.collectAsState()
    val isTipsLoading by tipViewModel.isLoading.collectAsState()
    val pageScrollStateDashboard = rememberScrollState()

    var showSplitView by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<HealthTip?>(null) }

    // Data User untuk TopBar
    val username = currentUser?.username ?: "Guest"
    val profilePath = currentUser?.profilePicturePath

    // Padding Konsisten
    val startPadding = dimens.startScaffoldPadding
    val horizontalPadding = dimens.screenPadding + 5.dp

    // Komponen TopBar (Reusable untuk HP & Tablet)
    val dashboardTopBar: @Composable () -> Unit = {
        if (isTablet) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimens.topPaddingScaffold) // Padding atas agar tidak terlalu mepet status bar (atau inset)
                    .padding(start = startPadding)
            ) {
                HeaderProfileSection(name = username, profilePath = profilePath)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimens.screenPadding) // Padding atas agar tidak terlalu mepet status bar (atau inset)
                    .padding(horizontal = horizontalPadding) // Padding horizontal agar sejajar dengan konten bawah
            ) {
                HeaderProfileSection(name = username, profilePath = profilePath)
            }
        }
    }

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        if (isTablet) {
            // --- TABLET LAYOUT ---
            Row(modifier = Modifier.fillMaxSize()) {
                // PANEL KIRI (SCROLLABLE DENGAN SCAFFOLD)
                // Kita bungkus Panel Kiri dengan Scaffold agar TopBar menempel di panel ini
                Scaffold(
                    topBar = dashboardTopBar,
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues) // Padding dari Scaffold (TopBar height)
                            .verticalScroll(pageScrollStateDashboard),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        val contentModifier = if (showSplitView) {
                            Modifier
                                .fillMaxSize()
                                .padding(end = dimens.paddingMedium)
                        } else {
                            // PERBAIKAN: Gunakan Center Alignment tegas
                            Modifier
                                .fillMaxWidth(dimens.contentMaxWidthPercent)
                                .align(Alignment.Center)
                        }

                        Box(modifier = contentModifier) {
                            DashboardContent(
                                navController = navController,
                                viewModel = viewModel,
                                authViewModel = authViewModel,
                                stats = stats,
                                currentPeriod = currentPeriod,
                                currentCategory = currentCategory,
                                currentUser = currentUser,
                                personalizedTips = personalizedTips,
                                isTipsLoading = isTipsLoading,
                                onTipsClick = {
                                    showSplitView = true
                                    selectedArticle = null
                                }
                            )
                        }
                    }
                }

                // PANEL KANAN
                AnimatedVisibility(
                    visible = showSplitView,
                    enter = slideInHorizontally { it } + fadeIn(),
                    exit = slideOutHorizontally { it } + fadeOut(),
                    modifier = Modifier
                        .width(420.dp)
                        .fillMaxHeight() // Full Height
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = dimens.screenPadding)
                            .padding(end = dimens.screenPadding)
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(24.dp)
                            )
                    ) {
                        if (selectedArticle == null) {
                            TabletTipsListPanel(
                                tips = personalizedTips,
                                onClose = { showSplitView = false },
                                onArticleClick = { tip -> selectedArticle = tip }
                            )
                        } else {
                            TabletTipDetailPanel(
                                tip = selectedArticle!!,
                                onBack = { selectedArticle = null }
                            )
                        }
                    }
                }
            }
        } else {
            // --- HP LAYOUT (SCROLLABLE DENGAN SCAFFOLD) ---
            Scaffold(
                topBar = dashboardTopBar,
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxSize()
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues) // Penting: Menggeser konten ke bawah TopBar
                        .padding(bottom = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    DashboardContent(
                        navController = navController,
                        viewModel = viewModel,
                        authViewModel = authViewModel,
                        stats = stats,
                        currentPeriod = currentPeriod,
                        currentCategory = currentCategory,
                        currentUser = currentUser,
                        personalizedTips = personalizedTips,
                        isTipsLoading = isTipsLoading,
                        onTipsClick = { navController.navigate(AppRoutes.TIPS_LIST) }
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardContent(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    stats: DashboardStats,
    currentPeriod: TimePeriod,
    currentCategory: CategoryFilter,
    currentUser: UserEntity?,
    personalizedTips: List<HealthTip>,
    isTipsLoading: Boolean,
    onTipsClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val dailyGoal = currentUser?.dailyStepsGoal ?: 5000

    // Padding horizontal (tapi tanpa topBar padding logic di sini karena sudah di handle Scaffold)
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
            )
    ) {
        // 💡 PERUBAHAN: HeaderProfileSection dihapus dari sini karena sudah pindah ke TopBar Scaffold
        // 💡 PERUBAHAN: Spacer 40.dp dihapus karena Scaffold menangani status bar

        val currentSteps = (stats.totalDistance * 1300).toInt()
        DailyGoalCard(
            cardColor = MaterialTheme.colorScheme.onBackground,
            current = currentSteps,
            total = dailyGoal,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(dimens.paddingMedium))

        if (dimens.isTablet) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)) {
                    PeriodSelector(
                        selectedPeriod = currentPeriod,
                        onSelect = { newPeriod -> viewModel.setTimePeriod(newPeriod) }
                    )
                    Spacer(modifier = Modifier.height(dimens.paddingSmall))
                    DailySummaryGrid(
                        cardColor = MaterialTheme.colorScheme.onBackground,
                        distance = String.format("%.1f km", stats.totalDistance),
                        intake = "${stats.totalCaloriesIntake} kcal",
                        time = "${stats.totalDuration} min",
                        burned = "${stats.totalCaloriesBurned} kcal"
                    )
                }
                Spacer(modifier = Modifier.width(dimens.paddingMedium))
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)) {
                    CategoryFilterSelector(
                        selectedFilter = currentCategory,
                        onSelect = { newCategory -> viewModel.setCategoryFilter(newCategory) }
                    )
                    Spacer(modifier = Modifier.height(dimens.paddingSmall))
                    HistorySection(
                        navController = navController,
                        recentActivities = stats.recentActivities,
                        cardColor = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                PeriodSelector(selectedPeriod = currentPeriod, onSelect = { newPeriod -> viewModel.setTimePeriod(newPeriod) })
                Spacer(modifier = Modifier.height(dimens.paddingSmall))
                DailySummaryGrid(
                    cardColor = MaterialTheme.colorScheme.onBackground,
                    distance = String.format("%.1f km", stats.totalDistance),
                    intake = "${stats.totalCaloriesIntake} kcal",
                    time = "${stats.totalDuration} min",
                    burned = "${stats.totalCaloriesBurned} kcal"
                )
                Spacer(modifier = Modifier.height(dimens.paddingMedium))
                CategoryFilterSelector(selectedFilter = currentCategory, onSelect = { newCategory -> viewModel.setCategoryFilter(newCategory) })
                Spacer(modifier = Modifier.height(dimens.paddingSmall))
                HistorySection(
                    navController = navController,
                    recentActivities = stats.recentActivities,
                    cardColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.paddingMedium))
        OnlineTipsCard(
            underlineColor = MaterialTheme.colorScheme.primary,
            cardColor = MaterialTheme.colorScheme.onBackground,
            tips = personalizedTips,
            isLoading = isTipsLoading,
            onClick = onTipsClick
        )
    }
}

// =========================================================================
// TABLET SIDE PANELS
// =========================================================================

// UI List untuk Panel Tablet
@Composable
fun TabletTipsListPanel(
    tips: List<HealthTip>,
    onClose: () -> Unit,
    onArticleClick: (HealthTip) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
            }
            Text(
                "Recommended for You",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // List Content (Diadaptasi dari OnlineTipsListScreen)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(tips) { tip ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onArticleClick(tip) },
                    colors = CardDefaults.cardColors(containerColor = Color.White) // Background Card Putih
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(tip.thumbnailUrl)
                                .crossfade(true)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            onState = { state: AsyncImagePainter.State ->
                                if (state is AsyncImagePainter.State.Error) {
                                    Log.e(
                                        "COIL_ERROR",
                                        "Gagal muat: ${state.result.throwable.message}"
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                tip.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = tip.category.uppercase(),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// UI Detail untuk Panel Tablet
@Composable
fun TabletTipDetailPanel(tip: HealthTip, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Article",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Content (Diadaptasi dari TipDetailScreen)
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .customDropShadow(
                    color = Color.Black.copy(alpha = 0.15f),
                    borderRadius = 32.dp,
                    blurRadius = 5.dp,
                    offsetY = 6.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. IMAGE CIRCLE
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(tip.thumbnailUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 2. TITLE & CATEGORY
                Text(
                    text = tip.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = tip.category.uppercase(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 3. DETAIL POINTS
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                    // Description
                    SectionTitle(title = "Description")
                    Text(
                        text = tip.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Guide
                    SectionTitle(title = if (tip.category.contains("Meal")) "Nutritional Facts & Guide" else "Steps & Benefits")
                    Text(
                        text = tip.content,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Estimated Impact
                    SectionTitle(title = "Estimated Impact")
                    val impactText =
                        if (tip.category.contains("Meal")) "~300-500 Calories per serving" else "~50-100 Calories burned per session"
                    Text(text = impactText, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

// =========================================================================
// EXISTING COMPONENTS (UPDATED WITH DIMENS)
// =========================================================================

@Composable
fun HeaderProfileSection(name: String, profilePath: String?) {
    val dimens = LocalAppDimens.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimens.paddingMedium)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePath ?: R.drawable.profile_picture)
                .crossfade(true).build(),
            placeholder = painterResource(id = R.drawable.profile_picture),
            error = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Profile",
            modifier = Modifier
                .size(dimens.profilePicSize) // Responsive Size
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(dimens.paddingMedium))
        Column {
            Text(
                "Hi $name !",
                fontSize = dimens.textSizeHeadline, // Responsive Font
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Let's start your journey today",
                fontSize = dimens.textSizeBody,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun HistorySection(
    navController: NavController,
    recentActivities: List<ActivityEntity>,
    cardColor: Color
) {
    val dimens = LocalAppDimens.current
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .customDropShadow(borderRadius = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.paddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "History Activities",
                    fontSize = dimens.textSizeTitle,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "View Detail",
                    fontSize = dimens.textSizeSmall,
                    color = Color.Gray,
                    modifier = Modifier.noRippleClickable {
                        navController.navigate(AppRoutes.ACTIVITY) {
                            popUpTo(AppRoutes.DASHBOARD) { saveState = true }; launchSingleTop =
                            true; restoreState = true
                        }
                    })
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (recentActivities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No activities found\n Add any activities you want.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = dimens.textSizeBody
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    recentActivities.forEach { activity ->
                        val icon =
                            if (activity.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon
                        val typeLabel =
                            if (activity.type == ActivityType.FOOD) "Intake" else "Burned"
                        Box(modifier = Modifier.noRippleClickable { navController.navigate("detail_log/${activity.id}") }) {
                            HistoryItemDashboard(
                                title = activity.title,
                                subtitle = "${activity.calories} kcal | $typeLabel",
                                iconRes = icon
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailySummaryGrid(
    cardColor: Color,
    distance: String,
    intake: String,
    time: String,
    burned: String
) {
    val dimens = LocalAppDimens.current
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .customDropShadow(borderRadius = 24.dp)
    ) {
        Column(modifier = Modifier.padding(dimens.paddingMedium)) {
            Text(
                "Daily Summary",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = dimens.textSizeTitle
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryItem(label = "Distance", value = distance, modifier = Modifier.weight(1f))
                SummaryItem(
                    label = "Calories Intake",
                    value = intake,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryItem(label = "Time Usage", value = time, modifier = Modifier.weight(1f))
                SummaryItem(
                    label = "Calories Burned",
                    value = burned,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, modifier: Modifier = Modifier) {
    val dimens = LocalAppDimens.current
    Column(modifier = modifier) {
        Text(text = label, fontSize = dimens.textSizeSmall, color = Color.Gray)
        Text(
            text = value,
            fontSize = dimens.textSizeTitle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DailyGoalCard(cardColor: Color, current: Int, total: Int, color: Color) {
    val dimens = LocalAppDimens.current
    val progressValue =
        if (total > 0) (current.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.dailyGoalHeight) // Responsive Height
            .customDropShadow(borderRadius = 32.dp)
    ) {
        Row(
            modifier = Modifier.padding(dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.daily_goal_icon),
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSizeSmall)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Daily Goal",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = dimens.textSizeTitle
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$current",
                        fontSize = dimens.textSizeHeadline,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " / $total",
                        fontSize = dimens.textSizeBody,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier.size(80.dp),
                    color = color,
                    strokeWidth = 8.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${(progressValue * 100).toInt()} %",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = dimens.textSizeSmall
                )
            }
        }
    }
}

@Composable
fun OnlineTipsCard(
    underlineColor: Color,
    cardColor: Color,
    tips: List<HealthTip>,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 20.dp)
            .customDropShadow(borderRadius = 24.dp)
            .noRippleClickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Online Tips",
                    fontFamily = robotoFontFamily,
                    fontSize = dimens.textSizeTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(R.drawable.arrow_icon),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(underlineColor)
            )
            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp), contentAlignment = Alignment.CenterStart
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center), color = underlineColor
                    )
                } else if (tips.isEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(underlineColor.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("💡", fontSize = 24.sp) }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                "No activity logged today",
                                fontWeight = FontWeight.Bold,
                                fontSize = dimens.textSizeBody,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Log your workout to get personalized tips!",
                                fontSize = dimens.textSizeSmall,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    val displayTip = tips.first()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(displayTip.thumbnailUrl).crossfade(true).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                text = displayTip.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = dimens.textSizeBody,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = displayTip.description,
                                fontSize = dimens.textSizeSmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- SELECTORS ---

@Composable
fun SlidingSelector(
    items: List<String>,
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
        modifier = modifier.height(50.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            val itemWidth = maxWidth / items.size
            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * selectedIndex,
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                label = ""
            )
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .fillMaxHeight()
                    .offset(x = indicatorOffset)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary)
            )
            Row(modifier = Modifier.fillMaxSize()) {
                items.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .width(itemWidth)
                            .fillMaxHeight()
                            .noRippleClickable { onIndexSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        val textColor by animateColorAsState(
                            if (index == selectedIndex) Color.White else Color.Gray,
                            label = ""
                        )
                        Text(
                            text = text,
                            color = textColor,
                            fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodSelector(selectedPeriod: TimePeriod, onSelect: (TimePeriod) -> Unit) {
    SlidingSelector(
        items = listOf("Daily", "Weekly"),
        selectedIndex = if (selectedPeriod == TimePeriod.DAILY) 0 else 1,
        onIndexSelected = { if (it == 0) onSelect(TimePeriod.DAILY) else onSelect(TimePeriod.WEEKLY) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CategoryFilterSelector(selectedFilter: CategoryFilter, onSelect: (CategoryFilter) -> Unit) {
    SlidingSelector(
        items = listOf("All", "Exercise", "Foods"),
        selectedIndex = when (selectedFilter) {
            CategoryFilter.ALL -> 0; CategoryFilter.EXERCISE -> 1; CategoryFilter.FOOD -> 2
        },
        onIndexSelected = {
            when (it) {
                0 -> onSelect(CategoryFilter.ALL); 1 -> onSelect(CategoryFilter.EXERCISE); 2 -> onSelect(
                CategoryFilter.FOOD
            )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun HistoryItemDashboard(title: String, subtitle: String, iconRes: Int? = null) {
    val dimens = LocalAppDimens.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .customDropShadow(borderRadius = 24.dp)
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
                    if (iconRes != null) Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSizeSmall),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = dimens.textSizeBody
                    )
                    Text(
                        text = subtitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = dimens.textSizeSmall
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

// --- PREVIEW ---
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, heightDp = 1500)
@Composable
fun DashboardScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val database = AppDatabase.getDatabase(context)
    val activityRepo = ActivityRepository(database.activityDao())
    val authRepo = AuthRepository(database.userDao())
    val tipRepo = TipsRepository()
    val sharedPrefs = context.getSharedPreferences("preview_prefs", Context.MODE_PRIVATE)

    SmartFitTheme {
        DashboardScreen(
            navController,
            SharedViewModel(activityRepo),
            AuthViewModel(authRepo, activityRepo, sharedPrefs),
            TipsViewModel(tipRepo, activityRepo)
        )
    }
}
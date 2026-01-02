package com.example.d1_jetpackcompose.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.data.local.AppDatabase
import com.example.d1_jetpackcompose.data.remote.model.HealthTip
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.AuthRepository
import com.example.d1_jetpackcompose.data.repository.TipsRepository
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.CategoryFilter
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.TimePeriod
import com.example.d1_jetpackcompose.ui.viewModel.TipsViewModel
import kotlin.collections.first
import kotlin.collections.isNotEmpty

private val HistoryCardGray = Color(0xFFE8E8E8)

// --- EXTENSION: NO RIPPLE CLICK ---
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

// --- 💡 ADDED: CUSTOM SHADOW EXTENSION (Sama seperti di ActivityLog) ---
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

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel,
    tipViewModel: TipsViewModel
) {
    LaunchedEffect(Unit) {
        tipViewModel.fetchTips()
    }

    val stats by viewModel.dashboardStats.collectAsState()
    val currentPeriod by viewModel.selectedPeriod.collectAsState()
    val currentCategory by viewModel.selectedCategory.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val personalizedTips by tipViewModel.personalizedTips.collectAsState()
    val isTipsLoading by tipViewModel.isLoading.collectAsState()

    val username = currentUser?.username ?: "Guest"
    val dailyGoal = currentUser?.dailyStepsGoal ?: 5000
    val profilePath = currentUser?.profilePicturePath
    val showTips by tipViewModel.showTipsCard.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        HeaderProfileSection(name = username, profilePath = profilePath)
        Spacer(modifier = Modifier.height(10.dp))

        val currentSteps = (stats.totalDistance * 1300).toInt()
        DailyGoalCard(
            cardColor = MaterialTheme.colorScheme.onBackground,
            current = currentSteps,
            total = dailyGoal,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Period Selector (Sliding Animation)
        PeriodSelector(
            selectedPeriod = currentPeriod,
            onSelect = { newPeriod -> viewModel.setTimePeriod(newPeriod) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        DailySummaryGrid(
            cardColor = MaterialTheme.colorScheme.onBackground,
            distance = String.format("%.1f km", stats.totalDistance),
            intake = "${stats.totalCaloriesIntake} kcal",
            time = "${stats.totalDuration} min",
            burned = "${stats.totalCaloriesBurned} kcal"
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Category Filter (Sliding Animation)
        CategoryFilterSelector(
            selectedFilter = currentCategory,
            onSelect = { newCategory -> viewModel.setCategoryFilter(newCategory) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        HistorySection(
            navController = navController,
            recentActivities = stats.recentActivities,
            cardColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(25.dp))

        OnlineTipsCard(
            underlineColor = MaterialTheme.colorScheme.primary, // Ganti sesuai tema Anda
            cardColor = MaterialTheme.colorScheme.onBackground,
            tips = personalizedTips,
            isLoading = isTipsLoading,
            onClick = {
                // Hanya navigasi jika tips tidak kosong
                if (personalizedTips.isNotEmpty()) {
                    navController.navigate(AppRoutes.TIPS_LIST)
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun OnlineTipsCard(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Lampu / Tips
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                // Ganti dengan Icon yang sesuai, misal R.drawable.ic_bulb atau Icons.Default.Info
                Text("💡", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "New Insights Available!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Based on your activity today.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }

            // Panah
            Text(
                "→",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// --- SLIDING SELECTOR ENGINE ---
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
            val maxWidth = maxWidth
            val itemWidth = maxWidth / items.size

            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * selectedIndex,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "indicatorSlide"
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
                            targetValue = if (index == selectedIndex) Color.White else Color.Gray,
                            animationSpec = tween(durationMillis = 200),
                            label = "textColor"
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
    val items = listOf("Daily", "Weekly")
    val selectedIndex = if (selectedPeriod == TimePeriod.DAILY) 0 else 1
    SlidingSelector(
        items = items,
        selectedIndex = selectedIndex,
        onIndexSelected = { if (it == 0) onSelect(TimePeriod.DAILY) else onSelect(TimePeriod.WEEKLY) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CategoryFilterSelector(selectedFilter: CategoryFilter, onSelect: (CategoryFilter) -> Unit) {
    val items = listOf("All", "Exercise", "Foods")
    val selectedIndex = when (selectedFilter) {
        CategoryFilter.ALL -> 0
        CategoryFilter.EXERCISE -> 1
        CategoryFilter.FOOD -> 2
    }
    SlidingSelector(
        items = items,
        selectedIndex = selectedIndex,
        onIndexSelected = {
            when (it) {
                0 -> onSelect(CategoryFilter.ALL)
                1 -> onSelect(CategoryFilter.EXERCISE)
                2 -> onSelect(CategoryFilter.FOOD)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

// --- COMPONENTS ---

@Composable
fun HeaderProfileSection(name: String, profilePath: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePath ?: R.drawable.profile_picture)
                .crossfade(true).build(),
            placeholder = painterResource(id = R.drawable.profile_picture),
            error = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Profile",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                "Hi $name !",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text("Let's start your journey today", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun HistorySection(
    navController: NavController,
    recentActivities: List<ActivityEntity>,
    cardColor: Color
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 32.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "History Activities",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "View Detail",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.noRippleClickable {
                        navController.navigate(AppRoutes.ACTIVITY) {
                            popUpTo(AppRoutes.DASHBOARD) {
                                saveState = true
                            }; launchSingleTop = true; restoreState = true
                        }
                    })
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (recentActivities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No activities found\n Add any activities you want.",
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 32.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Daily Summary",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
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
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DailyGoalCard(cardColor: Color, current: Int, total: Int, color: Color) {
    val progressValue =
        if (total > 0) (current.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f
    val percentageInt = (progressValue * 100).toInt()
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 32.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.daily_goal_icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Daily Goal",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$current",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " / $total",
                        fontSize = 18.sp,
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
                    text = "$percentageInt %",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
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
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Ubah ke wrap agar fleksibel
            .padding(bottom = 20.dp) // Beri jarak bawah
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 32.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
            .noRippleClickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(vertical = 15.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // --- HEADER (Tetap seperti aslinya) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Online Tips",
                    fontFamily = robotoFontFamily,
                    fontSize = 20.sp,
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

            // --- KONTEN DINAMIS (Teaser Section) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        color = underlineColor
                    )
                } else if (tips.isEmpty()) {
                    // TAMPILAN SAAT BELUM ADA ACTIVITY LOG (TEASER)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(underlineColor.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💡", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                "No activity logged today",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Log your workout to get personalized tips!",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    // TAMPILAN SAAT TIPS SUDAH MUNCUL (RELEVAN)
                    val displayTip = tips.first()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(displayTip.thumbnailUrl)
                                .crossfade(true)
                                .build(),
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
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = displayTip.description,
                                fontSize = 12.sp,
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

@Composable
fun HistoryItemDashboard(title: String, subtitle: String, iconRes: Int? = null) {
    // 💡 APPLIED SHADOW HERE
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .customDropShadow(
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
                    if (iconRes != null) Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
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
package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.CategoryFilter
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.TimePeriod
import coil.compose.AsyncImage
import coil.request.ImageRequest


private val HistoryCardGray = Color(0xFFE8E8E8)

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel
) {

    val stats by viewModel.dashboardStats.collectAsState()
    val currentPeriod by viewModel.selectedPeriod.collectAsState()
    val currentCategory by viewModel.selectedCategory.collectAsState()

    // Ambil Objek User Lengkap
    val currentUser by authViewModel.currentUser.collectAsState()

    // Ambil username & Goal dari currentUser
    val username = currentUser?.username ?: "Guest"
    val dailyGoal = currentUser?.dailyStepsGoal ?: 5000
    val profilePath = currentUser?.profilePicturePath

    // Main Container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Header Profile (Nama & Foto)
        HeaderProfileSection(name = username, profilePath = profilePath)

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Daily Goal Card
        val currentSteps = (stats.totalDistance * 1300).toInt()

        DailyGoalCard(
            cardColor = MaterialTheme.colorScheme.onBackground,
            current = currentSteps,
            total = dailyGoal,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 3. PERIOD SELECTOR
        PeriodSelector(
            selectedPeriod = currentPeriod,
            onSelect = { newPeriod -> viewModel.setTimePeriod(newPeriod) },
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Summary Grid
        DailySummaryGrid(
            cardColor = MaterialTheme.colorScheme.onBackground,
            distance = String.format("%.1f km", stats.totalDistance),
            intake = "${stats.totalCaloriesIntake} kcal",
            time = "${stats.totalDuration} min",
            burned = "${stats.totalCaloriesBurned} kcal"
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 5. CATEGORY FILTER
        CategoryFilterSelector(
            selectedFilter = currentCategory,
            onSelect = { newCategory -> viewModel.setCategoryFilter(newCategory) },
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 6. History Section
        HistorySection(
            navController = navController,
            recentActivities = stats.recentActivities,
            cardColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(25.dp))

        OnlineTipsCard(
            underlineColor = MaterialTheme.colorScheme.primary,
            cardColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun HeaderProfileSection(name: String, profilePath: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto Profil Dinamis
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePath ?: R.drawable.profile_picture)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.profile_picture),
            error = painterResource(id = R.drawable.profile_picture),
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 💡 PERBAIKAN: Menampilkan Nickname & Kalimat Sambutan
        Column {
            Text(
                text = "Hi $name !",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Let's start your journey today",
                fontSize = 14.sp,
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
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "History Activities",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "View Detail",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.ACTIVITY) {
                            popUpTo(AppRoutes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (recentActivities.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Text("No activities found\n Add any activities you want.", color = Color.Gray, textAlign = TextAlign.Center)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    recentActivities.forEach { activity ->
                        val icon = if (activity.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon
                        val typeLabel = if (activity.type == ActivityType.FOOD) "Intake" else "Burned"
                        HistoryItemDashboard(title = activity.title, subtitle = "${activity.calories} kcal | $typeLabel", iconRes = icon)
                    }
                }
            }
        }
    }
}

// Sisa Komponen (DailySummaryGrid, DailyGoalCard, dll) tetap sama sesuai kebutuhan UI Anda.
@Composable
fun DailySummaryGrid(cardColor: Color, distance: String, intake: String, time: String, burned: String) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = cardColor), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Daily Summary", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryItem(label = "Distance", value = distance, modifier = Modifier.weight(1f))
                SummaryItem(label = "Calories Intake", value = intake, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryItem(label = "Time Usage", value = time, modifier = Modifier.weight(1f))
                SummaryItem(label = "Calories Burned", value = burned, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun DailyGoalCard(cardColor: Color, current: Int, total: Int, color: Color) {
    val progressValue = if (total > 0) (current.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f
    val percentageInt = (progressValue * 100).toInt()
    Card(shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = cardColor), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.daily_goal_icon), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Daily Goal", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "$current", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = " / $total", fontSize = 18.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                }
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(progress = { progressValue }, modifier = Modifier.size(80.dp), color = color, strokeWidth = 8.dp, trackColor = Color(0xFFE0E0E0), strokeCap = StrokeCap.Round)
                Text(text = "$percentageInt %", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun PeriodSelector(selectedPeriod: TimePeriod, onSelect: (TimePeriod) -> Unit, activeColor: Color, inactiveColor: Color) {
    Card(shape = RoundedCornerShape(50), colors = CardDefaults.cardColors(containerColor = inactiveColor), modifier = Modifier.fillMaxWidth().height(50.dp)) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(25.dp)).background(Color.White)) {
            Row(modifier = Modifier.fillMaxSize()) {
                SelectorItem(text = "Daily", isActive = selectedPeriod == TimePeriod.DAILY, activeColor = activeColor, modifier = Modifier.weight(1f), onClick = { onSelect(TimePeriod.DAILY) })
                SelectorItem(text = "Weekly", isActive = selectedPeriod == TimePeriod.WEEKLY, activeColor = activeColor, modifier = Modifier.weight(1f), onClick = { onSelect(TimePeriod.WEEKLY) })
            }
        }
    }
}

@Composable
fun CategoryFilterSelector(selectedFilter: CategoryFilter, onSelect: (CategoryFilter) -> Unit, activeColor: Color, inactiveColor: Color) {
    Card(shape = RoundedCornerShape(50), colors = CardDefaults.cardColors(containerColor = inactiveColor), modifier = Modifier.fillMaxWidth().height(50.dp)) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(25.dp)).background(Color.White)) {
            Row(modifier = Modifier.fillMaxSize()) {
                SelectorItem(text = "All", isActive = selectedFilter == CategoryFilter.ALL, activeColor = activeColor, modifier = Modifier.weight(1f), onClick = { onSelect(CategoryFilter.ALL) })
                SelectorItem(text = "Exercise", isActive = selectedFilter == CategoryFilter.EXERCISE, activeColor = activeColor, modifier = Modifier.weight(1f), onClick = { onSelect(CategoryFilter.EXERCISE) })
                SelectorItem(text = "Foods", isActive = selectedFilter == CategoryFilter.FOOD, activeColor = activeColor, modifier = Modifier.weight(1f), onClick = { onSelect(CategoryFilter.FOOD) })
            }
        }
    }
}

@Composable
fun OnlineTipsCard(underlineColor: Color, cardColor: Color) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = cardColor), modifier = Modifier.fillMaxWidth().height(120.dp).clickable {}) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Online Tips", fontFamily = robotoFontFamily, fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
                Image(painter = painterResource(R.drawable.arrow_icon), contentDescription = null)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(underlineColor))
        }
    }
}

@Composable
fun HistoryItemDashboard(title: String, subtitle: String, iconRes: Int? = null) {
    Box(modifier = Modifier.fillMaxWidth().background(HistoryCardGray, RoundedCornerShape(24.dp))) {
        Row(modifier = Modifier.padding(12.dp).padding(horizontal = 8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onSurface), contentAlignment = Alignment.Center) {
                    if (iconRes != null) Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(Color.White))
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    Text(text = subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
            Image(painter = painterResource(id = R.drawable.right_arrow_icon), contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface), modifier = Modifier.size(15.dp))
        }
    }
}

@Composable
fun SelectorItem(text: String, isActive: Boolean, activeColor: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier = modifier.fillMaxHeight().padding(4.dp).clip(RoundedCornerShape(25.dp)).background(if (isActive) activeColor else Color.Transparent).clickable { onClick() }, contentAlignment = Alignment.Center) {
        Text(text = text, color = if (isActive) Color.White else Color.Gray, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp)
    }
}
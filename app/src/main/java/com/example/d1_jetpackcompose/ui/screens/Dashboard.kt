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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import com.example.d1_jetpackcompose.ui.viewModel.TimePeriod
import com.example.d1_jetpackcompose.ui.viewModel.CategoryFilter


private val HistoryCardGray = Color(0xFFE8E8E8)

@Composable
// 1. FUNGSI HALAMAN UTAMA
fun DashboardScreen(
    navController: NavController, // Butuh NavController untuk klik detail
    viewModel: SharedViewModel
) {

    val stats by viewModel.dashboardStats.collectAsState()
    val currentPeriod by viewModel.selectedPeriod.collectAsState()
    val currentCategory by viewModel.selectedCategory.collectAsState()
    // Main Container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Header Profile
        HeaderProfileSection(name = "Jamal")

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Daily Goal Card (Zero State)
        DailyGoalCard(
            cardColor = MaterialTheme.colorScheme.onBackground,
            current = stats.totalCaloriesBurned,
            total = 10000,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 2. PERIOD SELECTOR (INTERAKTIF)
        PeriodSelector(
            selectedPeriod = currentPeriod,
            onSelect = { newPeriod -> viewModel.setTimePeriod(newPeriod) }, // Update ViewModel
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Summary Grid (Zero State)
        DailySummaryGrid(
            cardColor = MaterialTheme.colorScheme.onBackground,
            distance = "${stats.totalDistance} km",
            intake = "${stats.totalCaloriesIntake} kcal",
            time = "${stats.totalDuration} min",
            burned = "${stats.totalCaloriesBurned} kcal"
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 3. CATEGORY FILTER (INTERAKTIF)
        CategoryFilterSelector(
            selectedFilter = currentCategory,
            onSelect = { newCategory -> viewModel.setCategoryFilter(newCategory) }, // Update ViewModel
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 6. History Section (Empty/Zero State)
        if (stats.recentActivities.isEmpty()) {
            HistorySectionEmptyState(MaterialTheme.colorScheme.onBackground)
        } else {
            // KARTU DINAMIS (Mengikuti jumlah konten)
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight() // 💡 1. Biarkan tingginya menyesuaikan isi
                // Jangan gunakan .height(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth() // 💡 2. Jangan fillMaxSize, cukup fillMaxWidth
                        .padding(vertical = 20.dp)
                        .padding(horizontal = 20.dp), // Gabungkan padding di sini
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 💡 3. Hapus .verticalScroll() di sini.
                    // Karena DashboardScreen INDUK-nya sudah bisa discroll.

                    stats.recentActivities.forEach { activity ->
                        val icon = if (activity.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon
                        val typeLabel = if (activity.type == ActivityType.FOOD) "Intake" else "Burned"

                        HistoryItemDashboard(
                            title = activity.title,
                            subtitle = "${activity.calories} kcal | $typeLabel",
                            iconRes = icon
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        OnlineTipsCard(
            underlineColor = MaterialTheme.colorScheme.primary,
            cardColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

// 2. KOMPONEN DAILY SUMMARY (Gunakan ini untuk memperbaiki "Parameter never used")
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
        colors = CardDefaults.cardColors(
            containerColor = cardColor // Gunakan parameter di sini
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Daily Summary",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // Parameter 'distance' dan 'intake' sekarang digunakan di sini
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

// 3. KOMPONEN ITEM KECIL (Helper)
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

// 4. FIX DEPRECATED PROGRESS INDICATOR
@Composable
fun DailyGoalCard(cardColor: Color, current: Int, total: Int, color: Color) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor // Gunakan parameter di sini
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.daily_goal_icon),
                        contentDescription = "daily goal icon",
                        modifier = Modifier
                            .size(20.dp)
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
                // Perbaikan: Menggunakan parameter lambda () -> Float
                CircularProgressIndicator(
                    progress = { 0f }, // Tetap 0f karena progress 0
                    modifier = Modifier.size(80.dp),
                    color = color,
                    strokeWidth = 8.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round
                )
                Text(
                    "0 %",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun HeaderProfileSection(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp) // Beri jarak dari Status Bar
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- FOTO PROFIL ---
        // Ganti R.drawable.profile_placeholder dengan resource foto Anda
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape), // Membuat gambar jadi bulat sempurna
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // --- TEKS GREETING ---
        Column {
            Text(
                text = "Hi $name !",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface // Warna hijau gelap sesuai referensi
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
fun PeriodSelector(
    selectedPeriod: TimePeriod,
    onSelect: (TimePeriod) -> Unit,
    activeColor: Color,
    inactiveColor: Color
) {
    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = inactiveColor),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // DAILY BUTTON
                SelectorItem(
                    text = "Daily",
                    isActive = selectedPeriod == TimePeriod.DAILY,
                    activeColor = activeColor,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(TimePeriod.DAILY) }
                )

                // WEEKLY BUTTON
                SelectorItem(
                    text = "Weekly",
                    isActive = selectedPeriod == TimePeriod.WEEKLY,
                    activeColor = activeColor,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(TimePeriod.WEEKLY) }
                )
            }
        }
    }
}

@Composable
fun CategoryFilterSelector(
    selectedFilter: CategoryFilter,
    onSelect: (CategoryFilter) -> Unit,
    activeColor: Color,
    inactiveColor: Color
) {
    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = inactiveColor),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // ALL BUTTON
                SelectorItem(
                    text = "All",
                    isActive = selectedFilter == CategoryFilter.ALL,
                    activeColor = activeColor,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(CategoryFilter.ALL) }
                )

                // EXERCISE BUTTON
                SelectorItem(
                    text = "Exercise",
                    isActive = selectedFilter == CategoryFilter.EXERCISE,
                    activeColor = activeColor,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(CategoryFilter.EXERCISE) }
                )

                // FOODS BUTTON
                SelectorItem(
                    text = "Foods",
                    isActive = selectedFilter == CategoryFilter.FOOD,
                    activeColor = activeColor,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(CategoryFilter.FOOD) }
                )
            }
        }
    }
}

@Composable
fun HistorySectionEmptyState(cardColor: Color) {
    Column {
        // Kartu placeholder karena progress 0
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor // Gunakan parameter di sini
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        modifier = Modifier.clickable { /* Handle click */ }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Belum ada data history", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun OnlineTipsCard(underlineColor: Color, cardColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor // Gunakan parameter di sini
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable(
                        onClick = {}
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                        .drawBehind {
                            val strokeWidth =
                                2.dp.toPx() // Ketebalan garis
                            val y =
                                size.height - (strokeWidth / 2) // Posisi Y di bagian paling bawah

                            // Gambar garis dari kiri ke kanan di posisi Y tersebut
                            drawLine(
                                color = underlineColor, // Anda bisa gunakan warna dari theme Anda
                                start = Offset(
                                    0f,
                                    y
                                ),
                                end = Offset(
                                    size.width,
                                    y
                                ),
                                strokeWidth = strokeWidth
                            )
                        },
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Online Tips",
                            fontFamily = robotoFontFamily,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Image(
                            painter = painterResource(R.drawable.arrow_icon),
                            contentDescription = "SmartFit Logo",
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(15.dp)
                ) {

                }
            }
        }
    }
}

@Composable
fun HistoryItemDashboard(
    title: String,
    subtitle: String,
    iconRes: Int? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 24.dp, // Sesuai dengan shape background
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
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
                            colorFilter = ColorFilter.tint(Color.White) // 💡 Ikon jadi putih agar kontras
                        )
                    }
                }

                Spacer(modifier = Modifier.size(10.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

            }

            Image(
                painter = painterResource(id = R.drawable.right_arrow_icon),
                contentDescription = title,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .size(15.dp)
                    .fillMaxWidth()
            )
        }
    }
}

// Helper Item untuk menghindari duplikasi kode
@Composable
fun SelectorItem(
    text: String,
    isActive: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(if (isActive) activeColor else Color.Transparent) // Logic Warna Aktif
            .clickable { onClick() }, // Logic Klik
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isActive) Color.White else Color.Gray, // Logic Warna Text
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

//@Preview(
//    showBackground = true,
//    name = "Scrollable Profile",
//    heightDp = 1000
//)
//@Composable
//private fun DashboardScreenPrev() {
//    SmartFitTheme {
//        DashboardScreen()
//    }
//}
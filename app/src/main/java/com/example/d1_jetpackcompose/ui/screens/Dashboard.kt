package com.example.d1_jetpackcompose.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DashboardScreen()
                }
            }
        }
    }
}

@Composable
// 1. FUNGSI HALAMAN UTAMA
fun DashboardScreen() {

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
            current = 0,
            total = 10000,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 3. PERIOD SELECTOR (Daily vs Weekly) - BARU
        PeriodSelector(
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Summary Grid (Zero State)
        DailySummaryGrid(
            cardColor = MaterialTheme.colorScheme.onBackground,
            distance = "0 km",
            intake = "0 kcal",
            time = "0 min",
            burned = "0 kcal"
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 5. CATEGORY FILTER (All / Exercise / Foods) - BARU
        CategoryFilterSelector(
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 6. History Section (Empty/Zero State)
        HistorySectionEmptyState(cardColor = MaterialTheme.colorScheme.onBackground)

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
            Text("Daily Summary", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
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
                    Text("Daily Goal", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp)
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
                Text("0 %", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
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
fun PeriodSelector(activeColor: Color, inactiveColor: Color) {
    Card(
        shape = RoundedCornerShape(50), // Bentuk Pill/Capsule penuh
        colors = CardDefaults.cardColors(
            containerColor = inactiveColor // Gunakan parameter di sini
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // Tinggi fix sesuai visual
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Daily", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Weekly", color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun CategoryFilterSelector(activeColor: Color, inactiveColor: Color) {
    Card(
        shape = RoundedCornerShape(50), // Bentuk Pill/Capsule penuh
        colors = CardDefaults.cardColors(
            containerColor = inactiveColor // Gunakan parameter di sini
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // Tinggi fix sesuai visual
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Daily", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Weekly", color = Color.Gray, fontWeight = FontWeight.Medium)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Foods",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
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

@Preview(
    showBackground = true,
    name = "Scrollable Profile",
    heightDp = 1000
)
@Composable
private fun DashboardScreenPrev() {
    SmartFitTheme {
        DashboardScreen()
    }
}
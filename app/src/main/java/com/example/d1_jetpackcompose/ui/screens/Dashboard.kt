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
import com.example.d1_jetpackcompose.ui.theme.SmartFitColors
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily
import com.example.d1_jetpackcompose.ui.theme.smartFitShape

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
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Header Profile
        HeaderProfileSection(name = "Jamal")

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Daily Goal Card (Zero State)
        DailyGoalCard(current = 0, total = 10000, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(25.dp))

        // 3. PERIOD SELECTOR (Daily vs Weekly) - BARU
        PeriodSelector(activeColor = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Summary Grid (Zero State)
        DailySummaryGrid(
            distance = "0 km",
            intake = "0 kcal",
            time = "0 min",
            burned = "0 kcal"
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 5. CATEGORY FILTER (All / Exercise / Foods) - BARU
        CategoryFilterSelector(activeColor = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(10.dp))

        // 6. History Section (Empty/Zero State)
        HistorySectionEmptyState()

        Spacer(modifier = Modifier.height(25.dp))

        OnlineTipsCard(underlineColor = MaterialTheme.colorScheme.primary)
    }
}

// 2. KOMPONEN DAILY SUMMARY (Gunakan ini untuk memperbaiki "Parameter never used")
@Composable
fun DailySummaryGrid(distance: String, intake: String, time: String, burned: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Daily Summary", fontWeight = FontWeight.Bold, color = Color(0xFF3E4E35))
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
            color = Color(0xFF3E4E35)
        )
    }
}

// 4. FIX DEPRECATED PROGRESS INDICATOR
@Composable
fun DailyGoalCard(current: Int, total: Int, color: Color) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Daily Goal", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "$current / $total", fontSize = 28.sp, fontWeight = FontWeight.Bold)
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
                Text("0 %", fontWeight = FontWeight.Bold, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HeaderProfileSection(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
                color = Color(0xFF3E4E35) // Warna hijau gelap sesuai referensi
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
fun PeriodSelector(activeColor: Color) {
    Card(
        shape = RoundedCornerShape(50), // Bentuk Pill/Capsule penuh
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp) // Tinggi fix sesuai visual
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol Kiri (Active: Daily Summary)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(activeColor), // Warna Hijau Aktif
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Daily Summary",
                    color = Color.White, // Teks Putih karena background hijau
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            // Tombol Kanan (Inactive: Weekly Summary)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color.Transparent), // Transparan/Putih
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Weekly Summary",
                    color = Color(0xFF3E4E35), // Warna teks hijau tua/gelap
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun CategoryFilterSelector(activeColor: Color) {
    Card(
        shape = RoundedCornerShape(50), // Bentuk Pill
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Option 1: All (Active)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(activeColor),
                contentAlignment = Alignment.Center
            ) {
                Text("All", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }

            // Option 2: Exercise (Inactive)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Exercise",
                    color = Color(0xFF3E4E35),
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            // Option 3: Foods (Inactive)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Foods",
                    color = Color(0xFF3E4E35),
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun HistorySectionEmptyState() {
    Column {
        // Kartu placeholder karena progress 0
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White), // Warna abu-abu sesuai style inactive
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
                        color = Color(0xFF3E4E35)
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
fun OnlineTipsCard(underlineColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                            fontSize = 24.sp
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
    heightDp = 1200
)
@Composable
private fun DashboardScreenPrev() {
    SmartFitTheme {
        DashboardScreen()

    }
}
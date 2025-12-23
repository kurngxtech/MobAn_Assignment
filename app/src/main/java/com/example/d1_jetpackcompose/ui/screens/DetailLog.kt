package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailLogScreen(
    navController: NavController,
    activityId: Int, // 💡 Menerima ID dari Navigasi
    viewModel: SharedViewModel // 💡 Menerima ViewModel
) {
    // 💡 1. LOAD DATA SECARA OTOMATIS SAAT SCREEN DIBUKA
    LaunchedEffect(activityId) {
        viewModel.loadActivityById(activityId)
    }

    // 💡 2. OBSERVE DATA TERPILIH DARI DATABASE
    val activityData by viewModel.selectedActivity.collectAsState()

    // State lokal untuk handle loading state agar tidak crash saat data null
    if (activityData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        val data = activityData!! // Memastikan data tidak null setelah loading

        // Format Tanggal
        val dateString = SimpleDateFormat("EEEE dd MMMM yyyy HH:mm a", Locale.getDefault())
            .format(Date(data.timestamp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- BUTTON BACK ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 8.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }

            // --- HEADER TITLE ---
            Text(
                text = "Detail Log",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- HERO ICON & TITLE (DINAMIS) ---
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center
            ) {
                val icon = if (data.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Activity Icon",
                    modifier = Modifier.size(60.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = data.title, // 💡 Nama dinamis dari DB
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateString, // 💡 Waktu dinamis dari DB
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- DATA CARD (DINAMIS) ---
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomDetailItem(label = "Categories", value = if (data.type == ActivityType.FOOD) "Meal" else "Exercise")
                    Spacer(modifier = Modifier.height(20.dp))

                    // Tampilkan Distance & Duration hanya jika tipenya EXERCISE
                    if (data.type == ActivityType.EXERCISE) {
                        CustomDetailItem(label = "Distance", value = "${data.distance} km")
                        Spacer(modifier = Modifier.height(20.dp))
                        CustomDetailItem(label = "Duration", value = "${data.duration} min")
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    CustomDetailItem(
                        label = if (data.type == ActivityType.FOOD) "Calories Intake" else "Calories Burned",
                        value = "${data.calories} kcal"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- ACTION BUTTONS ---

            // 💡 TOMBOL EDIT (UPDATE LOGIC)
            Button(
                onClick = {
                    // Contoh implementasi Update: bisa arahkan ke form edit
                    // atau update langsung state tertentu di sini
                    // viewModel.updateActivity(data.copy(title = "Edited Title"))
                },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Edit Activities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(5.dp))

            // 💡 TOMBOL DELETE (DELETE LOGIC)
            Button(
                onClick = {
                    viewModel.deleteActivity(data) // 💡 Hapus dari Room Database
                    navController.popBackStack()   // 💡 Kembali ke Activity Log
                },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Merah
            ) {
                Text(text = "Delete Activities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CustomDetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailLogScreenPrev() {
    SmartFitTheme {
        // Sediakan NavController dan ViewModel untuk preview
        DetailLogScreen(
            navController = rememberNavController(),
            viewModel = viewModel(),
            activityId = 0// Cukup panggil viewModel() untuk membuat instance baru khusus preview
        )
    }
}
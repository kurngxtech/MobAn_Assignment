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
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R // Pastikan import R sesuai package kamu
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

// import com.example.d1_jetpackcompose.navigation.AppRoutes // Uncomment jika diperlukan

@Composable
fun DetailLogScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. BUTTON BACK (Sesuai Syntax Request) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        // LOGIC NAVIGASI
                        navController.navigate(AppRoutes.ACTIVITY) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                    }
                    .padding(vertical = 8.dp),
            ) {
                // Wadah Image untuk Icon Arrow
                Image(
                    painter = painterResource(id = R.drawable.back_arrow), // Ganti ID icon back kamu
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
            }
        }

        // --- 2. HEADER TITLE ---
        Text(
            text = "Detail Log",
            color = MaterialTheme.colorScheme.onSurface, // Hijau Tua
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. HERO ICON & TITLE ---
        // Lingkaran Icon Besar
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface), // Hijau Tua
            contentAlignment = Alignment.Center
        ) {
            // Placeholder Image Icon Utama (Stickman)
//            Image(
//                painter = painterResource(id = R.drawable.ic_walking_stickman), // Ganti dengan icon walking kamu
//                contentDescription = "Activity Icon",
//                modifier = Modifier.size(60.dp),
//                // colorFilter = ColorFilter.tint(Color.White) // Uncomment jika icon perlu ditint putih
//            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title: Walking
        Text(
            text = "Walking",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp
        )

        // Subtitle: Date
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Sunday 20 November 2025 08.30 am",
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. DATA CARD ---
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
                // Categories
                CustomDetailItem(label = "Categories", value = "Walking")
                Spacer(modifier = Modifier.height(20.dp))

                // Distance
                CustomDetailItem(label = "Distance", value = "15.5 km")
                Spacer(modifier = Modifier.height(20.dp))

                // Duration
                CustomDetailItem(label = "Duration", value = "97 min")
                Spacer(modifier = Modifier.height(20.dp))

                // Calories Burned
                CustomDetailItem(label = "Calories Burned", value = "89 cal")
                Spacer(modifier = Modifier.height(20.dp))

                // Calories Intake
                CustomDetailItem(label = "Calories Intake", value = "10 cal")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 5. ACTION BUTTONS ---

        // Button Edit (Hijau Muda / Primary)
        Button(
            onClick = { /* TODO: Edit Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon Edit
//                Image(
//                    painter = painterResource(id = R.drawable.ic_edit), // Ganti dengan icon pensil/edit
//                    contentDescription = null,
//                    modifier = Modifier.size(18.dp),
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Activities",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Button Delete (Merah / SurfaceVariant)
        Button(
            onClick = { /* TODO: Delete Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant // Warna Merah sesuai request
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon Delete/Trash
//                Image(
//                    painter = painterResource(id = R.drawable.ic_delete), // Ganti dengan icon sampah/delete
//                    contentDescription = null,
//                    modifier = Modifier.size(18.dp),
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete Activities",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// --- REUSABLE COMPONENT FOR DETAIL ITEM ---
// Bentuknya mirip input form tapi statis (Text saja)
@Composable
fun CustomDetailItem(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface // Text utama bold & gelap
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Garis Bawah
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.5f)
        )
    }
}

@Preview
@Composable
private fun DetailLogScreenPrev() {
    SmartFitTheme {
        DetailLogScreen(navController = rememberNavController())
    }
}
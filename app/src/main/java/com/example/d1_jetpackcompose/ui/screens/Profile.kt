package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel

val CardWhite = Color.White

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    authViewModel: AuthViewModel
) {
    // 1. OBSERVASI DATA USER REALTIME
    val user by authViewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER ---
        Text(
            text = "My Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- PROFILE IMAGE & NAME ---
        Box(modifier = Modifier.size(120.dp).clip(CircleShape)) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // NAMA DINAMIS
        Text(
            text = user?.username ?: "Guest",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface
            ),
            // --- TAMBAHKAN LINE INI ---
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,     // Elevasi saat diam (sama dengan Card)
                pressedElevation = 8.dp,     // Elevasi saat ditekan (opsional, agar lebih interaktif)
                hoveredElevation = 6.dp,
                focusedElevation = 6.dp
            ),
            // ---------------------------
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(text = "Edit Profile", fontSize = 12.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- STATS CARDS DINAMIS ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // HEIGHT
            ProfileStatCard(
                label = "Height",
                value = "${user?.height?.toInt() ?: 0} cm",
                modifier = Modifier.weight(1f)
            )
            // WEIGHT
            ProfileStatCard(
                label = "Weight",
                value = "${user?.weight?.toInt() ?: 0} kg",
                modifier = Modifier.weight(1f)
            )
            // AGE
            ProfileStatCard(
                label = "Age",
                value = "${user?.age ?: 0} y",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // GENDER
            ProfileStatCard(
                label = "Gender",
                value = user?.gender ?: "-",
                modifier = Modifier.weight(1f)
            )

            // BMI CARD
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier.weight(1f).height(110.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val bmi = user?.bmi ?: 0f
                    // Logic Kategori BMI Sederhana
                    val bmiLabel = when {
                        bmi < 18.5 -> "Underweight"
                        bmi < 25.0 -> "Normal"
                        bmi < 30.0 -> "Overweight"
                        else -> "Obese"
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
                            progress = { (bmi / 40f).coerceIn(0f, 1f) }, // Visualisasi Progress BMI
                            modifier = Modifier.size(60.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 6.dp,
                            trackColor = Color(0xFFE0E0E0),
                            strokeCap = StrokeCap.Round,
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("18,5 - 24,9", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurface)
                            // Angka BMI Dinamis
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

        // --- ACCOUNT & HELP (TETAP SAMA) ---
        Text("Account", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        MenuContainer {
            MenuItem(R.drawable.user_logo, "Personal Info") {}
            Divider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(R.drawable.password_logo, "Change Password") {}
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Help", Modifier.fillMaxWidth(), color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        MenuContainer {
            MenuItem(R.drawable.faq_logo, "FAQ") {}
            Divider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)

            MenuItem(
                iconId = R.drawable.logout_logo,
                text = "Sign Out",
                isDestructive = true,
                onClick = {
                    viewModel.logout()
                    authViewModel.logout()
                    navController.navigate(AppRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MenuContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Composable
fun MenuItem(iconId: Int, text: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconId), contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text, fontSize = 16.sp, fontWeight = FontWeight.Medium,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Default.KeyboardArrowRight, "Go", tint = Color.Gray, modifier = Modifier.size(24.dp))
    }
}
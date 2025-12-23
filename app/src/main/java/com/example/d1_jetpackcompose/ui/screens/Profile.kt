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
import com.example.d1_jetpackcompose.R // Sesuaikan package R Anda
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

// Gunakan warna custom jika belum ada di theme, atau mapping ke MaterialTheme
val CardWhite = Color.White

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Background Abu muda
            .padding(vertical = 24.dp, horizontal = 20.dp)
            .padding(top = 46.dp) // Padding standar
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. HEADER / PAGE TITLE ---
        Text(
            text = "My Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface, // Hijau Tua/Hitam
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. PROFILE SECTION ---
        // Foto Profil
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture), // Pastikan ada dummy image
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Nickname
        Text(
            text = "Jamal",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- 3. PRIMARY ACTION BUTTON (Edit Profile) ---
        Button(
            onClick = { /* TODO: Navigate to Edit Profile */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface // Warna Hijau Tua (Sesuai gambar tombol gelap)
                // ATAU gunakan MaterialTheme.colorScheme.primary jika ingin Hijau Muda
                // Berdasarkan gambar "My Profile", tombolnya terlihat hijau gelap/onSurface
            ),
            shape = RoundedCornerShape(50), // Pill shape
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- 4. INFORMATION CARDS SECTION ---

        // ROW 1: Height, Weight, Age
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card 1: Height
            ProfileStatCard(
                label = "Height",
                value = "190 cm",
                modifier = Modifier.weight(1f)
            )
            // Card 2: Weight
            ProfileStatCard(
                label = "Weight",
                value = "70 kg",
                modifier = Modifier.weight(1f)
            )
            // Card 3: Age
            ProfileStatCard(
                label = "Age",
                value = "25 y",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ROW 2: Gender & BMI
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card: Gender
            ProfileStatCard(
                label = "Gender",
                value = "Male",
                modifier = Modifier.weight(1f),
                isCenterContent = true
            )

            // Card: BMI (Special layout)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp) // Samakan tinggi dengan stat card
            ) {
                // Layout khusus BMI
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "BMI Normal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // BMI CIRCULAR INDICATOR
                    // Meniru style Dashboard Daily Goal
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { 0.7f }, // Contoh progress BMI
                            modifier = Modifier.size(60.dp),
                            color = MaterialTheme.colorScheme.primary, // Hijau
                            strokeWidth = 6.dp,
                            trackColor = Color(0xFFE0E0E0),
                            strokeCap = StrokeCap.Round,
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "18,5 - 24,9", // Range BMI
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "BMI",
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

        // --- 5. ACCOUNT & HELP SECTION ---

        // Label Section
        Text(
            text = "Account",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Account Block Group
        MenuContainer {
            MenuItem(
                iconId = R.drawable.user_logo, // Ganti icon personal info
                text = "Personal Info",
                onClick = { /* Navigate */ }
            )
            Divider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(
                iconId = R.drawable.password_logo, // Ganti icon password
                text = "Change Password",
                onClick = { /* Navigate */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Label Section
        Text(
            text = "Help",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Help Block Group
        MenuContainer {
            MenuItem(
                iconId = R.drawable.faq_logo, // Ganti icon FAQ
                text = "FAQ",
                onClick = { /* Navigate */ }
            )
            Divider(color = Color.Gray.copy(alpha = 0.1f), thickness = 1.dp)
            MenuItem(
                iconId = R.drawable.logout_logo, // Ganti icon logout
                text = "Sign Out",
                isDestructive = true, // Opsional: Beri warna merah jika perlu
                onClick = { /* Logout Logic */ }
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

// --- REUSABLE COMPONENTS ---

@Composable
fun ProfileStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isCenterContent: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), // Shadow halus
        modifier = Modifier
            .then(modifier)
            .height(110.dp) // Tinggi fix agar seragam
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 20.sp, // Ukuran Value Besar
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp, // Ukuran Label Kecil
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MenuContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite), // Putih sesuai light theme
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), // Flat atau slight shadow
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
fun MenuItem(
    iconId: Int,
    text: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Menu (Kiri)
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text Menu
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // Chevron Arrow (Kanan)
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(
    showBackground = true,
    name = "Profile Page Preview",
    heightDp = 1000 // Mengatur tinggi agar semua konten scrollable terlihat
)
@Composable
fun PreviewProfileScreen() {
    // Menggunakan Theme aplikasi agar warna MaterialTheme muncul sesuai desain
    SmartFitTheme {
        // NavController dummy untuk kebutuhan preview saja
        val navController = rememberNavController()

        ProfileScreen(navController = navController)
    }
}
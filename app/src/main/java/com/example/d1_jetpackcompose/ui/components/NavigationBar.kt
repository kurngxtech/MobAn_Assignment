package com.example.d1_jetpackcompose.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.ui.theme.robotoFontFamily

@Composable
fun BubbleNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Modifier di sini TIDAK boleh memiliki background warna apa pun
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 24.dp) // Jarak melayang dari pinggir dan bawah
    ) {
        Card(
            shape = RoundedCornerShape(50.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Hanya Card yang berwarna putih
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ... NavbarItem tetap sama seperti sebelumnya
                NavbarItem(R.drawable.home_icon, "Home", true) { /* nav */ }
                NavbarItem(R.drawable.activity_log_logo, "Activity", false) { /* nav */ }
                NavbarItem(R.drawable.profile_logo, "Profile", false) { /* nav */ }
            }
        }
    }
}

// Helper Component agar kode lebih bersih & reusable
@Composable
fun NavbarItem(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    // Warna aktif sesuai referensi (Hijau) vs Abu-abu
    val contentColor = if (isActive) Color(0xFF4F6A4E) else Color.Gray
    val fontColor = if (isActive) Color(0xFF4F6A4E) else Color.Gray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp) // Touch target yang nyaman
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(contentColor)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontFamily = robotoFontFamily,
            color = fontColor,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun BubbleNavigationBarPrev() {
    SmartFitTheme {
        val navController = rememberNavController()
        // Sekarang teruskan navController palsu tersebut
        BubbleNavigationBar(navController = navController)
    }
}
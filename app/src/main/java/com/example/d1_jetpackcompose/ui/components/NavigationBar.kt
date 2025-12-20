package com.example.d1_jetpackcompose.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.currentBackStackEntryAsState
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
    // Logic pendeteksi route aktif untuk menentukan parameter isActive secara dinamis
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 24.dp)
    ) {
        Card(
            shape = RoundedCornerShape(50.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp) // Sedikit ditambah tingginya agar pill & text memiliki ruang napas
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ITEM: HOME
                NavbarItem(
                    iconRes = R.drawable.home_icon,
                    label = "Home",
                    isActive = currentRoute == AppRoutes.DASHBOARD
                ) {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }

                // ITEM: ACTIVITY
                NavbarItem(
                    iconRes = R.drawable.activity_log_logo,
                    label = "Activity",
                    isActive = currentRoute == AppRoutes.ACTIVITY
                ) {
                    navController.navigate(AppRoutes.ACTIVITY) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }

                // ITEM: PROFILE
                NavbarItem(
                    iconRes = R.drawable.profile_logo,
                    label = "Profile",
                    isActive = currentRoute == AppRoutes.PROFILE
                ) {
                    navController.navigate(AppRoutes.PROFILE) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            }
        }
    }
}

@Composable
fun NavbarItem(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    // Animasi warna untuk transisi yang smooth
    val backgroundPillColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "pillAnimation"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isActive) Color.White else Color.Gray,
        label = "iconAnimation"
    )
    val textColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray,
        label = "textAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        // --- CONTAINER SHAPE LONJONG (PILL) UNTUK ICON ---
        Box(
            modifier = Modifier
                .width(64.dp) // Lebar pill sesuai Material 3
                .height(32.dp) // Tinggi pill
                .clip(RoundedCornerShape(50)) // Membuat bentuk lonjong sempurna
                .background(backgroundPillColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(iconTint)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // --- LABEL TEXT (DI LUAR SHAPE) ---
        Text(
            text = label,
            fontSize = 11.sp,
            fontFamily = robotoFontFamily,
            color = textColor,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BubbleNavigationBarPrev() {
    SmartFitTheme {
        val navController = rememberNavController()
        // Sekarang teruskan navController palsu tersebut
        BubbleNavigationBar(navController = navController)
    }
}
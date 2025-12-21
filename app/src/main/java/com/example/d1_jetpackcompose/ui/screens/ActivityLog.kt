package com.example.d1_jetpackcompose.ui.screens

import android.R.attr.onClick
import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes

// --- THEME SETUP ---
private val HistoryCardGray = Color(0xFFE8E8E8)

// --- MODIFIER CUSTOM SHADOW (FIXED STROKE) ---
fun Modifier.customDropShadowActivity(
    color: Color = Color.Black.copy(alpha = 0.2f),
    borderRadius: Dp = 24.dp,
    blurRadius: Dp = 8.dp,
    offsetY: Dp = 4.dp
) = this.drawBehind {
    this.drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()

        // PERBAIKAN: Set warna transparan agar tidak muncul stroke di pinggir card
        frameworkPaint.color = android.graphics.Color.TRANSPARENT
        frameworkPaint.style = android.graphics.Paint.Style.FILL

        frameworkPaint.setShadowLayer(
            blurRadius.toPx(),
            0f,
            offsetY.toPx(),
            color.toArgb()
        )

        val outline = RoundedCornerShape(borderRadius).createOutline(size, layoutDirection, this)
        canvas.drawOutline(outline = outline, paint = paint)
    }
}

@Composable
fun ActivityLogScreen(navController: NavController) {
    // 1. HAPUS verticalScroll di root Column agar page tidak ikut bergeser
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER (Static) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Activities",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
            )
            Text(
                text = "You can choose either edit or add new activity",
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        // --- TOP CARDS (Static) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopActionCard(
                title = "Add Exercise",
                subtitle = "Running or Walking",
                modifier = Modifier.weight(1f),
                onClick = {
                    navController.navigate(AppRoutes.EXERCISE) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            )
            TopActionCard(
                title = "Add Food",
                subtitle = "Input meals and calories",
                modifier = Modifier.weight(1f),
                onClick = {
                    navController.navigate(AppRoutes.FOOD) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION TITLE & TABS (Static) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "History Activities",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
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

        Spacer(modifier = Modifier.height(10.dp))

        // --- 4. MAIN CONTENT CARD (Fixed Height & Internal Scroll) ---
        // --- 4. Main Content (Daftar History dengan Pembatas Statis) ---
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Card tetap diam pada ketinggian ini
        ) {
            // --- PEMBATAS STATIS (Warna Putih mengikuti Card) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 20.dp), // Area "Pembatas" Atas & Bawah
            ) {
                // --- AREA KONTEN (Hanya bagian ini yang scrollable) ---
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Masukkan daftar HistoryItem kamu di sini
                    HistoryItem(title = "Lawar Plek", subtitle = "Des 18, 20.00 | 80 cal intake")
                    HistoryItem(title = "Walking", subtitle = "Des 18, 20.00 | 80 cal intake")
                    HistoryItem(title = "Walking", subtitle = "Des 18, 20.00 | 80 cal intake")
                    HistoryItem(title = "Walking", subtitle = "Des 18, 20.00 | 80 cal intake")
                    HistoryItem(title = "Walking", subtitle = "Des 18, 20.00 | 80 cal intake")
                }
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
fun TopActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    onClick: () -> Unit
) {
    // Menggunakan Box + background agar custom shadow lebih presisi daripada Card
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .aspectRatio(1f)
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 32.dp,
                blurRadius = 5.dp,
                offsetY = 6.dp
            )
            .background(Color.White, RoundedCornerShape(32.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center
            ) {
                if (iconRes != null) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    backgroundColor: Color,
    leftIconRes: Int? = null,
    rightIconRes: Int? = null
) {
    Button(
        onClick = { /* TODO */ },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (leftIconRes != null) {
                    Image(
                        painter = painterResource(id = leftIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            if (rightIconRes != null) {
                Image(
                    painter = painterResource(id = rightIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}

@Composable
fun HistoryItem(
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
                            modifier = Modifier.size(24.dp)
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

@Preview(showBackground = true, heightDp = 1920)
@Composable
fun PreviewActivityLogRevision() {
    SmartFitTheme {
        ActivityLogScreen(navController = rememberNavController())
    }
}
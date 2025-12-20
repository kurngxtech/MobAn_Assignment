package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.R


// --- COLORS ---
private val ResetRed = Color(0xFF8D3B3B)

fun Modifier.customDropShadowExercise(
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
fun ExercisePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. MAIN STATS CARD
        MainStatsCard()

        // 2. DAILY GOAL CARD
        DailyGoalCard()
    }
}

@Composable
fun MainStatsCard() {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Mengikuti flat design di gambar
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(25.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Sisi Kiri: Angka-angka
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatLabelValue(label = "Step Count", value = "0")
                    StatLabelValue(label = "Calorie Burned", value = "0", unit = "cal")
                    StatLabelValue(label = "Distance", value = "0", unit = "km")
                }

                // Sisi Kanan: Grey Card untuk Stickman
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(200.dp)
                        .background(
                            color = Color(0xFFE9E9E9), // Abu-abu terang sesuai gambar
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.walk_icon),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp) // Sesuaikan size
                    )
                }
            }

            // Buttons Section
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { /* Action Start */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .customDropShadowExercise(
                            color = Color.Black.copy(alpha = 0.15f),
                            borderRadius = 24.dp, // Sesuai dengan shape background
                            blurRadius = 5.dp,
                            offsetY = 6.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Start your Exercise",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = { /* Action Reset */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .customDropShadowExercise(
                            color = Color.Black.copy(alpha = 0.15f),
                            borderRadius = 24.dp, // Sesuai dengan shape background
                            blurRadius = 5.dp,
                            offsetY = 6.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ResetRed
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Reset",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun StatLabelValue(label: String, value: String, unit: String? = null) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF6B7280), // Warna teks label agak abu
            fontWeight = FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (unit != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = unit,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun DailyGoalCard() {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(28.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // SPACE UNTUK ICON SEPATU/GOAL
                    Box(
                        modifier = Modifier.size(24.dp), // Ukuran icon sesuai gambar
                        contentAlignment = Alignment.Center
                    ) {
                        // SILAKAN PANGGIL IMAGE ICON DISINI
                        Image(
                            painter = painterResource(id = R.drawable.daily_goal_icon),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Daily Goal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "5.000",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " / 10.000",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            // Progress Circle
            Box(contentAlignment = Alignment.Center) {
                // Background Track
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(85.dp),
                    color = Color(0xFFE9E9E9),
                    strokeWidth = 8.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                // Active Progress
                CircularProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier.size(85.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Text(
                    text = "50 %",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Preview()
@Composable
fun PreviewDashboardContentOnly() {
    SmartFitTheme {
        ExercisePage()
    }
}
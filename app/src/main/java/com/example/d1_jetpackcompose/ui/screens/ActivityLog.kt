package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.composed

// --- THEME SETUP ---
private val HistoryCardGray = Color(0xFFE8E8E8)

// Extension No Ripple
private fun Modifier.noRippleClickableActivity(onClick: () -> Unit): Modifier = composed {
    this.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
}

// --- MODIFIER CUSTOM SHADOW ---
fun Modifier.customDropShadowActivity(
    color: Color = Color.Black.copy(alpha = 0.2f),
    borderRadius: Dp = 24.dp,
    blurRadius: Dp = 8.dp,
    offsetY: Dp = 4.dp
) = this.drawBehind {
    this.drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
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
fun ActivityLogScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val activityList by viewModel.activityLogList.collectAsState()
    val currentPeriod by viewModel.selectedPeriod.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // --- HEADER ---
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
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        // --- TOP CARDS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopActionCard(
                title = "Add Exercise",
                subtitle = "Running or Walking",
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.walk_icon,
                onClick = { navController.navigate(AppRoutes.EXERCISE) }
            )
            TopActionCard(
                title = "Add Food",
                subtitle = "Input meals and calories",
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.add_food_icon,
                onClick = { navController.navigate(AppRoutes.FOOD) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION TITLE & TABS ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "History Activities",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Period Selector (Sliding Animation from Dashboard.kt)
            PeriodSelector(
                selectedPeriod = currentPeriod,
                onSelect = { viewModel.setTimePeriod(it) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- MAIN CONTENT CARD ---
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (activityList.isEmpty()) {
                        Column(modifier = Modifier.fillMaxSize().height(300.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(
                                "No activities found\n Add any activities you want.",
                                color = Color.Gray,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        activityList.forEach { activity ->
                            val dateString = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(activity.timestamp))
                            val typeLabel = if (activity.type == ActivityType.FOOD) "Intake" else "Burned"
                            val icon = if (activity.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon

                            HistoryItem(
                                title = activity.title,
                                subtitle = "$dateString | ${activity.calories} cal $typeLabel",
                                iconRes = icon,
                                modifier = Modifier.noRippleClickableActivity {
                                    navController.navigate("detail_log/${activity.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .noRippleClickableActivity(onClick = onClick)
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
            Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(text = subtitle, color = Color.Gray, fontSize = 11.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 14.sp)
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
                        modifier = Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    modifier: Modifier = Modifier
) {
    // 💡 APPLIED SHADOW HERE
    Box(
        modifier = modifier
            .fillMaxWidth()
            .customDropShadowActivity(
                color = Color.Black.copy(alpha = 0.15f), // Match TopActionCard
                borderRadius = 24.dp,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
            Image(
                painter = painterResource(id = R.drawable.right_arrow_icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
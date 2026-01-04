package com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R

// Data Class untuk FAQ
data class FaqItemData(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(navController: NavController) {
    // Data sesuai gambar
    val faqList = listOf(
        FaqItemData(
            "What is this app used for ?",
            "This app helps you track your daily steps, distance, and calories burned to support a healthier lifestyle."
        ),
        FaqItemData(
            "Is this app free to use ?",
            "Yes, the app is free to download and use. Some features may require a premium subscription."
        ),
        FaqItemData(
            "Do I need an account to use the app ?",
            "Yes, you need to create an account to use the app and save your step tracking data."
        ),
        FaqItemData(
            "How do I edit my profile information ?",
            "Go to Profile > Edit Profile, then update your information and save the changes."
        ),
        FaqItemData(
            "How does the app count my steps ?",
            "The app tracks your steps through manual input, where you enter the number of steps you walk each day."
        ),
        FaqItemData(
            "Can I track steps without internet connection ?",
            "Yes, steps are tracked offline and will sync once you're connected to the internet."
        ),
        FaqItemData(
            "How are calories burned calculated ?",
            "Calories are estimated based on your steps, distance, and personal data such as height and weight."
        ),
        FaqItemData(
            "Is the calorie calculation accurate ?",
            "The calculation is an estimate and may vary depending on activity intensity and individual body factors."
        ),
        FaqItemData(
            "Can I set a daily step goal ?",
            "Yes, you can set your daily step goal during the initial survey or update it in your Profile settings."
        ),
        FaqItemData(
            "Where can I see my progress history ?",
            "Your daily, weekly, and monthly progress can be viewed in the Activity Log page."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "FAQ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            // CARD PUTIH BESAR SEBAGAI ALAS (Background List)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    // Sesuai request: Warna Putih Card Besar = onBackground
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 700.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(faqList) { item ->
                        FaqItemCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun FaqItemCard(item: FaqItemData) {
    var isExpanded by remember { mutableStateOf(false) }

    // Card Abu-abu untuk setiap Item
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            // Sesuai request: Warna Abu-abu = onSurfaceVariant
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Pertanyaan
                Text(
                    text = item.question,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    // Sesuai request: Warna Hijau Text = onSurface
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Icon Panah (Dynamic)
                Image(
                    painter = painterResource(
                        id = if (isExpanded) R.drawable.arrowhead_up else R.drawable.arrowhead_down
                    ),
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Jawaban (Foldable content)
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.answer,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}
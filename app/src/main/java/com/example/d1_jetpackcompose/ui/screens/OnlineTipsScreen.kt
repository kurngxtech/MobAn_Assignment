package com.example.d1_jetpackcompose.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.d1_jetpackcompose.ui.viewModel.TipsViewModel

// --- EXTENSION SHADOW (Disalin dari Dashboard agar konsisten) ---
fun Modifier.customDropShadowTips(
    color: Color = Color.Black.copy(alpha = 0.15f), // Intensitas sama dengan dashboard
    borderRadius: Dp = 32.dp,
    blurRadius: Dp = 5.dp,
    offsetY: Dp = 6.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineTipsListScreen(navController: NavController, viewModel: TipsViewModel) {
    val tips by viewModel.personalizedTips.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommended for You", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tips) { tip ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tip_detail/${tip.id}") },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(tip.thumbnailUrl)
                                .crossfade(true)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            onState = { state: AsyncImagePainter.State ->
                                if (state is AsyncImagePainter.State.Error) {
                                    Log.e("COIL_ERROR", "Gagal muat: ${state.result.throwable.message}")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(tip.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = tip.category.uppercase(),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipDetailScreen(navController: NavController, viewModel: TipsViewModel, tipId: Int) {
    val tip = viewModel.getTipById(tipId)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Background utama aplikasi
        topBar = {
            TopAppBar(
                title = { Text("Article", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        if (tip != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // --- CARD UTAMA (Layout seperti referensi) ---
                Card(
                    shape = RoundedCornerShape(32.dp),
                    // Menggunakan warna kartu yang sama dengan Dashboard (onBackground pada tema Anda)
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp) // Margin kiri-kanan agar tidak menempel layar
                        .customDropShadowTips(
                            color = Color.Black.copy(alpha = 0.15f),
                            borderRadius = 32.dp,
                            blurRadius = 5.dp,
                            offsetY = 6.dp
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp), // Padding dalam kartu
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // 1. IMAGE CIRCLE (Task 1)
                        Box(
                            modifier = Modifier
                                .size(140.dp) // Ukuran lingkaran
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant), // Placeholder bg
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(tip.thumbnailUrl)
                                    .crossfade(true)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 2. TITLE & CATEGORY
                        Text(
                            text = tip.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Badge Kategori
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tip.category.uppercase(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // 3. DETAIL POINTS (Task 2)
                        // Kita strukturkan konten agar terlihat seperti poin-poin

                        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {

                            // Point A: Description
                            SectionTitle(title = "Description")
                            Text(
                                text = tip.description,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Point B: Full Guide / Content
                            SectionTitle(title = if (tip.category.contains("Meal")) "Nutritional Facts & Guide" else "Steps & Benefits")
                            Text(
                                text = tip.content,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Point C: Estimated Impact (Mock Data Visual)
                            // Ini tambahan agar layout terlihat penuh seperti referensi
                            SectionTitle(title = "Estimated Impact")
                            val impactText = if (tip.category.contains("Meal")) "~300-500 Calories per serving" else "~50-100 Calories burned per session"
                            Text(
                                text = impactText,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp)) // Jarak bawah agar bisa discroll lebih lega
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
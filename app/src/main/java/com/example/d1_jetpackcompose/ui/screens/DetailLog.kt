package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailLogScreen(
    navController: NavController,
    activityId: Int,
    viewModel: SharedViewModel
) {
    // 1. Load Data
    LaunchedEffect(activityId) {
        viewModel.loadActivityById(activityId)
    }

    val activityData by viewModel.selectedActivity.collectAsState()

    // --- STATES UTAMA ---
    var isEditMode by remember { mutableStateOf(false) } // Status Mode Edit
    var showEditConfirmation by remember { mutableStateOf(false) } // Pop-up Edit
    var showDeleteConfirmation by remember { mutableStateOf(false) } // Pop-up Delete

    // --- FORM STATES (Untuk Edit) ---
    var title by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    // Sinkronisasi data DB ke Form State saat data masuk
    LaunchedEffect(activityData) {
        activityData?.let {
            title = it.title
            calories = it.calories.toString()
            distance = it.distance.toString()
            duration = it.duration.toString()
        }
    }

    // --- POP-UP DIALOGS ---

    // 1. Dialog Konfirmasi Mulai Edit
    if (showEditConfirmation) {
        AlertDialog(
            onDismissRequest = { showEditConfirmation = false },
            containerColor = Color.White,
            title = { Text(text = "Edit Activity", fontWeight = FontWeight.Bold) },
            text = { Text("Do you want to edit this activity log?", color = Color.Black) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEditConfirmation = false
                        isEditMode = true // ✅ AKTIFKAN FORM
                    }
                ) {
                    Text("Yes, Edit", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditConfirmation = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // 2. Dialog Konfirmasi Delete
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            containerColor = Color.White,
            title = { Text(text = "Delete Activity", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this activity? This action cannot be undone.", color = Color.Black) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        activityData?.let { viewModel.deleteActivity(it) } // ✅ HAPUS DATA
                        navController.popBackStack()
                    }
                ) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // --- UI CONTENT ---
    if (activityData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        val data = activityData!!
        val dateString = SimpleDateFormat("EEEE dd MMMM yyyy HH:mm a", Locale.getDefault())
            .format(Date(data.timestamp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Back Button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 8.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }

            Text(
                text = "Detail Log",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Icon
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center
            ) {
                val icon = if (data.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Activity Icon",
                    modifier = Modifier.size(60.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- JUDUL ACTIVITY (Bisa Diedit) ---
            if (isEditMode) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
                // Indikator garis bawah saat edit
                HorizontalDivider(modifier = Modifier.width(120.dp).padding(top = 4.dp), color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            } else {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dateString, color = Color.Gray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(32.dp))

            // --- FORM CARD ---
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
                    // Kategori (Statis)
                    EditableDetailItem(
                        label = "Categories",
                        value = if (data.type == ActivityType.FOOD) "Meal" else "Exercise",
                        isEditable = false,
                        onValueChange = {}
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    if (data.type == ActivityType.EXERCISE) {
                        EditableDetailItem(
                            label = "Distance",
                            value = distance,
                            suffix = " km", // Satuan otomatis
                            isEditable = isEditMode,
                            onValueChange = { distance = it },
                            keyboardType = KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        EditableDetailItem(
                            label = "Duration",
                            value = duration,
                            suffix = " min", // Satuan otomatis
                            isEditable = isEditMode,
                            onValueChange = { duration = it },
                            keyboardType = KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    EditableDetailItem(
                        label = if (data.type == ActivityType.FOOD) "Calories Intake" else "Calories Burned",
                        value = calories,
                        suffix = " kcal", // Satuan otomatis
                        isEditable = isEditMode,
                        onValueChange = { calories = it },
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- TOMBOL AKSI (DINAMIS) ---
            if (isEditMode) {
                // TAMPILAN MODE EDIT: Tombol Save (Hijau)
                Button(
                    onClick = {
                        // SIMPAN PERUBAHAN
                        val updatedItem = data.copy(
                            title = title,
                            calories = calories.toIntOrNull() ?: 0,
                            distance = distance.toDoubleOrNull() ?: 0.0,
                            duration = duration.toIntOrNull() ?: 0
                        )
                        viewModel.updateActivity(updatedItem)
                        isEditMode = false // ✅ KEMBALI NON-AKTIF
                    },
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                // TAMPILAN NORMAL: Edit & Delete
                Button(
                    onClick = { showEditConfirmation = true }, // 💡 PANGGIL POP-UP
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Edit Activities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { showDeleteConfirmation = true }, // 💡 PANGGIL POP-UP
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Merah
                ) {
                    Text(text = "Delete Activities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- KOMPONEN EDITABLE (Custom) ---
@Composable
fun EditableDetailItem(
    label: String,
    value: String,
    suffix: String = "",
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        if (isEditable) {
            // MODE EDIT: Form Input
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    modifier = Modifier.weight(1f),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
                // Satuan tetap terlihat tapi statis
                if (suffix.isNotEmpty()) {
                    Text(text = suffix, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
            }
        } else {
            // MODE READ-ONLY: Text Biasa
            Text(
                text = value + suffix,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f))
    }
}
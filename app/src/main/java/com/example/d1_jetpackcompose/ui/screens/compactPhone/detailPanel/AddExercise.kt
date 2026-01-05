package com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.ui.theme.LocalAppDimens
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(navController: NavController, viewModel: SharedViewModel) {
    // 1. Deteksi Tablet untuk Mode Compact
    val isTablet = LocalAppDimens.current.isTablet

    // --- DIMENSI ADAPTIF (Compact untuk Tablet agar Fit-to-Screen) ---
    val topPadding = if (isTablet) 100.dp else 100.dp
    val headerSpacer = if (isTablet) 12.dp else 24.dp
    val cardVerticalPadding = if (isTablet) 16.dp else 32.dp
    val sectionSpacer = if (isTablet) 16.dp else 32.dp
    val elementSpacer = if (isTablet) 10.dp else 20.dp
    val iconContainerSize = if (isTablet) 80.dp else 110.dp
    val iconSize = if (isTablet) 40.dp else 60.dp
    val titleSize = if (isTablet) 22.sp else 28.sp

    // State form
    var exerciseName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Exercise",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    }
                    .verticalScroll(rememberScrollState()) // Scroll tetap ada sbg safety net, tapi seharusnya tidak terpakai
                    .padding(horizontal = 24.dp)
                    .padding(top = topPadding), // 💡 Padding Atas Dikurangi di Tablet
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(headerSpacer))

                // 2. Main Form Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 24.dp,
                                vertical = cardVerticalPadding
                            ), // 💡 Padding Card Dikurangi
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- ICON SECTION (Lebih Kecil di Tablet) ---
                        Box(
                            modifier = Modifier
                                .size(iconContainerSize)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.walk_icon),
                                contentDescription = "Exercise Icon",
                                modifier = Modifier.size(iconSize),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }

                        Spacer(modifier = Modifier.height(if (isTablet) 8.dp else 16.dp))

                        Text(
                            text = "Exercise Data",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = if (isTablet) 18.sp else 20.sp
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "Choose your exercise",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(sectionSpacer))

                        // --- FORM INPUTS ---
                        CustomUnderlinedInput(
                            label = "Exercise Name",
                            value = exerciseName,
                            onValueChange = { exerciseName = it }
                        )

                        Spacer(modifier = Modifier.height(elementSpacer))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Exercise Category", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            CategoryRadioButton(
                                text = "Walking",
                                selected = selectedCategory == "Walking",
                                onSelect = { selectedCategory = "Walking" }
                            )
                            CategoryRadioButton(
                                text = "Running",
                                selected = selectedCategory == "Running",
                                onSelect = { selectedCategory = "Running" }
                            )
                        }

                        Spacer(modifier = Modifier.height(elementSpacer))

                        CustomUnderlinedInput(
                            label = "Distance (km)",
                            value = distance,
                            onValueChange = { distance = it },
                            keyboardType = KeyboardType.Number
                        )

                        Spacer(modifier = Modifier.height(elementSpacer))

                        CustomUnderlinedInput(
                            label = "Duration (min)",
                            value = duration,
                            onValueChange = { duration = it },
                            keyboardType = KeyboardType.Number
                        )

                        Spacer(modifier = Modifier.height(elementSpacer))

                        CustomUnderlinedInput(
                            label = "Calories Burned",
                            value = caloriesBurned,
                            onValueChange = { caloriesBurned = it },
                            keyboardType = KeyboardType.Number
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (isTablet) 20.dp else 20.dp))

                // 3. Bottom Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (exerciseName.isNotEmpty()) {
                            viewModel.addActivity(
                                title = exerciseName,
                                type = ActivityType.EXERCISE,
                                calories = caloriesBurned.toIntOrNull() ?: 0,
                                distance = distance.toDoubleOrNull() ?: 0.0,
                                duration = duration.toIntOrNull() ?: 0
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "Add Activity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(if (isTablet) 16.dp else 32.dp))
            }
        }

    }


}

// --- CUSTOM COMPONENTS (Sama) ---
@Composable
fun CustomUnderlinedInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 16.sp, // Sedikit lebih kecil agar aman
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f))
    }
}

@Composable
fun CategoryRadioButton(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 2.dp)
    ) {
        RadioButton(
            selected = selected, onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onSurface,
                unselectedColor = Color.Gray
            ),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}
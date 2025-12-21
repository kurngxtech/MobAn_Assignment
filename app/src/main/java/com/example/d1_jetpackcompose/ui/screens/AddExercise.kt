package com.example.d1_jetpackcompose.ui.screens

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes


// Tambahkan parameter navController agar navigasi bisa jalan
@Composable
fun AddExerciseScreen(navController: NavController) {
    // State untuk form input
    var exerciseName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var caloriesIntake by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp), // Padding atas disesuaikan untuk tombol back
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- TOMBOL BACK (POJOK KIRI ATAS) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        // LOGIC NAVIGASI SESUAI PERMINTAAN
                        navController.navigate(AppRoutes.ACTIVITY) { // Ganti AppRoutes.ACTIVITY jika sudah ada object-nya
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                    }
                    .padding(vertical = 8.dp),
            ) {
                // Wadah Image untuk Icon Arrow
                Image(
                    painter = painterResource(id = R.drawable.back_arrow), // Ganti dengan ID icon-mu
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
            }
        }

        // 1. Header Page Title
        Text(
            text = "Add Exercise",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        // 2. Main Form Card
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- ICON SECTION ---
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center
                ) {
                    // Image Placeholder
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_run_stickman), // Ganti sesuai asset
//                        contentDescription = "Exercise Icon",
//                        modifier = Modifier.size(60.dp)
//                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Exercise Data",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "Choose your exercise",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- FORM INPUTS ---
                CustomUnderlinedInput(
                    label = "Exercise Name",
                    value = exerciseName,
                    onValueChange = { exerciseName = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Exercise Category", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
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

                Spacer(modifier = Modifier.height(20.dp))

                CustomUnderlinedInput(
                    label = "Distance (km)",
                    value = distance,
                    onValueChange = { distance = it },
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomUnderlinedInput(
                    label = "Duration (min)",
                    value = duration,
                    onValueChange = { duration = it },
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomUnderlinedInput(
                    label = "Calories Burned",
                    value = caloriesBurned,
                    onValueChange = { caloriesBurned = it },
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomUnderlinedInput(
                    label = "Calories Intake",
                    value = caloriesIntake,
                    onValueChange = { caloriesIntake = it },
                    keyboardType = KeyboardType.Number
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Bottom Button
        Button(
            onClick = { /* TODO: Save Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Add Activity",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
// --- CUSTOM COMPONENTS ---

@Composable
fun CustomUnderlinedInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Gray, // Label kecil warna abu
            fontSize = 12.sp
        )

        // BasicTextField memungkinkan kita mengatur layout tanpa box bawaan Material
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface // Text utama bold & gelap
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Jarak antara text dan garis
        )

        // Garis Bawah (Underline)
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun CategoryRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 6.dp)
    ) {
        // Custom Radio Button Visual
        // Kita menggunakan RadioButton bawaan tapi mengatur warnanya
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onSurface, // Hijau tua saat aktif
                unselectedColor = Color.Gray
            ),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun PreviewAddExercise() {
    SmartFitTheme {
        AddExerciseScreen(navController = rememberNavController())
    }
}
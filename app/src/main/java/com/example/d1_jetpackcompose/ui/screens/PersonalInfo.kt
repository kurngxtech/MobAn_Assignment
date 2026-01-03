package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Personal Info",
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
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Container untuk informasi user
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Username", value = user?.username ?: "-")
                    DividerRow()
                    InfoRow(label = "Email", value = user?.email ?: "-")
                }
            }

            Text(
                "Physical Stats",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Gender", value = user?.gender ?: "-")
                    DividerRow()
                    InfoRow(label = "Age", value = "${user?.age ?: 0} years")
                    DividerRow()
                    InfoRow(label = "Height", value = "${user?.height?.toInt() ?: 0} cm")
                    DividerRow()
                    InfoRow(label = "Weight", value = "${user?.weight?.toInt() ?: 0} kg")
                    DividerRow()

                    val bmi = user?.bmi ?: 0f
                    val bmiLabel = when {
                        bmi < 18.5 -> "Underweight"
                        bmi < 25.0 -> "Normal"
                        bmi < 30.0 -> "Overweight"
                        else -> "Obese"
                    }
                    InfoRow(label = "BMI", value = "${String.format("%.1f", bmi)} ($bmiLabel)")
                }
            }

            Text(
                "Goals & Activity",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Activity Level", value = user?.activityLevel ?: "-")
                    DividerRow()
                    InfoRow(label = "Primary Goal", value = user?.goal ?: "-")
                    DividerRow()
                    InfoRow(label = "Daily Steps Goal", value = "${user?.dailyStepsGoal ?: 5000}")

                    // 💡 UPDATE: Menambahkan Preferred Workout di sini
                    DividerRow()
                    InfoRow(label = "Preferred Workout", value = user?.preferredWorkout ?: "-")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun DividerRow() {
    HorizontalDivider(
        color = Color.Gray.copy(alpha = 0.1f),
        thickness = 1.dp
    )
}
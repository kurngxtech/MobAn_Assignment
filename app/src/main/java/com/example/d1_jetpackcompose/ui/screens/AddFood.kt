package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel

@Composable
fun AddFoodScreen(navController: NavController, viewModel: SharedViewModel) {
    // State untuk form input
    var foodName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var totalCalories by remember { mutableStateOf("") }

    // 💡 ADDED: Focus Manager
    val focusManager = LocalFocusManager.current

    // 💡 MODIFIED: Root Column dengan pointerInput
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. BUTTON BACK ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        navController.popBackStack()
                    }
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

        // --- 2. HEADER TITLE ---
        Text(
            text = "Add Meal",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. MAIN FORM CARD ---
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
                    Image(
                        painter = painterResource(id = R.drawable.add_food_icon),
                        contentDescription = "Food Icon",
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title & Subtitle
                Text(
                    text = "Food Meal",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "Add your meal data",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- FORM INPUTS ---

                // 1. Food Name
                CustomUnderlinedInputFood(
                    label = "Meal Name",
                    value = foodName,
                    onValueChange = { foodName = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Meal Category (Radio Buttons)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Meal Category",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val categories = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

                    categories.forEach { category ->
                        CategoryRadioButtonFood(
                            text = category,
                            selected = selectedCategory == category,
                            onSelect = { selectedCategory = category }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Total Calories
                CustomUnderlinedInputFood(
                    label = "Total Calories",
                    value = totalCalories,
                    onValueChange = { totalCalories = it },
                    keyboardType = KeyboardType.Number
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. BOTTOM BUTTON ---
        Button(
            onClick = {
                focusManager.clearFocus() // Tutup keyboard saat simpan
                if (foodName.isNotEmpty()) {
                    viewModel.addActivity(
                        title = foodName,
                        type = ActivityType.FOOD,
                        calories = totalCalories.toIntOrNull() ?: 0
                    )
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Add Meal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// --- REUSABLE COMPONENTS ---

@Composable
fun CustomUnderlinedInputFood(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun CategoryRadioButtonFood(
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
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onSurface,
                unselectedColor = Color.Gray
            ),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) MaterialTheme.colorScheme.onSurface else Color.Gray
        )
    }
}
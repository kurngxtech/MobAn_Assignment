package com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel

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
import com.example.d1_jetpackcompose.ui.theme.LocalAppDimens
import com.example.d1_jetpackcompose.ui.viewModel.SharedViewModel

@Composable
fun AddFoodScreen(navController: NavController, viewModel: SharedViewModel) {
    // 1. Deteksi Tablet
    val isTablet = LocalAppDimens.current.isTablet

    // --- DIMENSI ADAPTIF ---
    val topPadding = if (isTablet) 16.dp else 56.dp
    val headerSpacer = if (isTablet) 12.dp else 24.dp
    val cardVerticalPadding = if (isTablet) 16.dp else 32.dp
    val sectionSpacer = if (isTablet) 16.dp else 32.dp
    val elementSpacer = if (isTablet) 10.dp else 20.dp
    val iconContainerSize = if (isTablet) 80.dp else 110.dp
    val iconSize = if (isTablet) 40.dp else 60.dp
    val titleSize = if (isTablet) 22.sp else 28.sp

    var foodName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var totalCalories by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = topPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- BUTTON BACK ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
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

        // --- HEADER ---
        Text(
            text = "Add Meal",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = titleSize
        )
        Spacer(modifier = Modifier.height(headerSpacer))

        // --- MAIN FORM CARD ---
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = cardVerticalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- ICON ---
                Box(
                    modifier = Modifier
                        .size(iconContainerSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.add_food_icon),
                        contentDescription = "Food Icon",
                        modifier = Modifier.size(iconSize),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(if (isTablet) 8.dp else 16.dp))

                Text(
                    text = "Food Meal",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = if (isTablet) 18.sp else 20.sp
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Add your meal data",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(sectionSpacer))

                // --- FORM INPUTS ---
                CustomUnderlinedInputFood(
                    label = "Meal Name",
                    value = foodName,
                    onValueChange = { foodName = it }
                )

                Spacer(modifier = Modifier.height(elementSpacer))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Meal Category", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    val categories = listOf("Breakfast", "Lunch", "Dinner", "Snacks")
                    categories.forEach { category ->
                        CategoryRadioButtonFood(
                            text = category,
                            selected = selectedCategory == category,
                            onSelect = { selectedCategory = category }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(elementSpacer))

                CustomUnderlinedInputFood(
                    label = "Total Calories",
                    value = totalCalories,
                    onValueChange = { totalCalories = it },
                    keyboardType = KeyboardType.Number
                )
            }
        }

        Spacer(modifier = Modifier.height(if (isTablet) 12.dp else 20.dp))

        // --- BOTTOM BUTTON ---
        Button(
            onClick = {
                focusManager.clearFocus()
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

        Spacer(modifier = Modifier.height(if (isTablet) 16.dp else 32.dp))
    }
}

// --- COMPONENTS ---
@Composable
fun CustomUnderlinedInputFood(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f))
    }
}

@Composable
fun CategoryRadioButtonFood(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { onSelect() }.padding(vertical = 2.dp)
    ) {
        RadioButton(
            selected = selected, onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.onSurface, unselectedColor = Color.Gray),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (selected) MaterialTheme.colorScheme.onSurface else Color.Gray)
    }
}
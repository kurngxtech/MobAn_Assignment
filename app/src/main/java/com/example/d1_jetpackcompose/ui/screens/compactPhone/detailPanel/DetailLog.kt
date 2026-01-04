package com.example.d1_jetpackcompose.ui.screens.compactPhone.detailPanel

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.d1_jetpackcompose.ui.theme.LocalAppDimens
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
    // 1. Deteksi Tablet
    val isTablet = LocalAppDimens.current.isTablet

    // --- DIMENSI ADAPTIF (Compact) ---
    val topPadding = if (isTablet) 16.dp else 56.dp
    val headerSpacer = if (isTablet) 12.dp else 24.dp
    val iconContainerSize = if (isTablet) 80.dp else 110.dp
    val iconSize = if (isTablet) 40.dp else 60.dp
    val cardVerticalPadding = if (isTablet) 16.dp else 30.dp
    val cardSpacer = if (isTablet) 16.dp else 32.dp
    val elementSpacer = if (isTablet) 10.dp else 20.dp
    val titleSize = if (isTablet) 22.sp else 28.sp
    val editTitleSize = if (isTablet) 20.sp else 24.sp

    LaunchedEffect(activityId) {
        viewModel.loadActivityById(activityId)
    }

    val activityData by viewModel.selectedActivity.collectAsState()
    val focusManager = LocalFocusManager.current

    var isEditMode by remember { mutableStateOf(false) }
    var showEditConfirmation by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    LaunchedEffect(activityData) {
        activityData?.let {
            title = it.title
            calories = it.calories.toString()
            distance = it.distance.toString()
            duration = it.duration.toString()
        }
    }

    // --- DIALOGS (Tetap Sama) ---
    if (showEditConfirmation) {
        AlertDialog(
            onDismissRequest = { showEditConfirmation = false },
            containerColor = Color.White,
            title = { Text(text = "Edit Activity", fontWeight = FontWeight.Bold) },
            text = { Text("Do you want to edit this activity log?", color = Color.Black) },
            confirmButton = {
                TextButton(onClick = { showEditConfirmation = false; isEditMode = true }) {
                    Text(
                        "Yes, Edit",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditConfirmation = false }) {
                    Text(
                        "Cancel",
                        color = Color.Gray
                    )
                }
            }
        )
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            containerColor = Color.White,
            title = { Text(text = "Delete Activity", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this activity?", color = Color.Black) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation =
                        false; activityData?.let { viewModel.deleteActivity(it) }; navController.popBackStack()
                }) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(
                        "Cancel",
                        color = Color.Gray
                    )
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
        val dateString = SimpleDateFormat(
            "EEEE dd MMMM yyyy HH:mm a",
            Locale.getDefault()
        ).format(Date(data.timestamp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = topPadding),
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }

            Text(
                text = "Detail Log",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = titleSize
            )
            Spacer(modifier = Modifier.height(headerSpacer))

            // Icon
            Box(
                modifier = Modifier
                    .size(iconContainerSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center
            ) {
                val icon =
                    if (data.type == ActivityType.FOOD) R.drawable.add_food_icon else R.drawable.walk_icon
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Activity Icon",
                    modifier = Modifier.size(iconSize),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(if (isTablet) 8.dp else 16.dp))

            // Title
            if (isEditMode) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        fontSize = editTitleSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            } else {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = editTitleSize,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dateString, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(cardSpacer))

            // Card Form
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
                    EditableDetailItem(
                        label = "Categories",
                        value = if (data.type == ActivityType.FOOD) "Meal" else "Exercise",
                        isEditable = false,
                        onValueChange = {})
                    Spacer(modifier = Modifier.height(elementSpacer))

                    if (data.type == ActivityType.EXERCISE) {
                        EditableDetailItem(
                            label = "Distance",
                            value = distance,
                            suffix = " km",
                            isEditable = isEditMode,
                            onValueChange = { distance = it },
                            keyboardType = KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.height(elementSpacer))
                        EditableDetailItem(
                            label = "Duration",
                            value = duration,
                            suffix = " min",
                            isEditable = isEditMode,
                            onValueChange = { duration = it },
                            keyboardType = KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.height(elementSpacer))
                    }
                    EditableDetailItem(
                        label = if (data.type == ActivityType.FOOD) "Calories Intake" else "Calories Burned",
                        value = calories,
                        suffix = " kcal",
                        isEditable = isEditMode,
                        onValueChange = { calories = it },
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isTablet) 12.dp else 20.dp))

            // Buttons
            if (isEditMode) {
                Button(
                    onClick = {
                        focusManager.clearFocus();
                        val updatedItem = data.copy(
                            title = title,
                            calories = calories.toIntOrNull() ?: 0,
                            distance = distance.toDoubleOrNull() ?: 0.0,
                            duration = duration.toIntOrNull() ?: 0
                        ); viewModel.updateActivity(updatedItem); isEditMode = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Save Changes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = { showEditConfirmation = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Edit Activities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "Delete Activities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(if (isTablet) 16.dp else 32.dp))
        }
    }
}

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
        Spacer(modifier = Modifier.height(4.dp))
        if (isEditable) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    modifier = Modifier.weight(1f),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
                if (suffix.isNotEmpty()) Text(
                    text = suffix,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        } else {
            Text(
                text = value + suffix,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f))
    }
}
package com.example.d1_jetpackcompose.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

// --- DATA MODELS ---

data class UserSurveyData(
    var gender: String = "",
    var age: String = "",
    var height: String = "", // in cm
    var weight: String = "", // in kg
    var targetWeight: String = "",
    var goal: String = "", // Lose Weight, etc.
    var activityLevel: String = "",
    var workoutPreference: String = "",
    var dailySteps: String = "5000",
    var bmi: Float = 0f
)

enum class SurveyStep {
    GENDER,
    BODY_STATS,
    GOAL,
    ACTIVITY,
    WORKOUT_PREF,
    STEP_GOAL
}

// --- COLORS (MATCHING LOGIN PAGE) ---
val SurveyInputGray = Color(0xFFD1D1D1) // Sesuai request sebelumnya

// --- MAIN SCREEN ---

@Composable
fun SurveyScreen(navController: NavController) {
    // State Management
    var currentStep by remember { mutableStateOf(SurveyStep.GENDER) }
    val surveyData = remember { mutableStateOf(UserSurveyData()) }

    // Logic Progress Bar (0.0 - 1.0)
    val totalSteps = SurveyStep.values().size
    val progress = (currentStep.ordinal + 1).toFloat() / totalSteps.toFloat()

    // Animasi Slide Logic
    val transitionSpec: AnimatedContentTransitionScope<SurveyStep>.() -> ContentTransform = {
        val direction = if (targetState.ordinal > initialState.ordinal) {
            // Maju: Masuk dari kanan, keluar ke kiri
            AnimatedContentTransitionScope.SlideDirection.Left
        } else {
            // Mundur: Masuk dari kiri, keluar ke kanan
            AnimatedContentTransitionScope.SlideDirection.Right
        }
        slideIntoContainer(direction, animationSpec = tween(500)) togetherWith
                slideOutOfContainer(direction, animationSpec = tween(500))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .padding(top = 40.dp) // Safe Area
    ) {
        // 1. TOP BAR: Back Button & Progress
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (currentStep.ordinal > 0) {
                IconButton(onClick = {
                    val prevOrdinal = currentStep.ordinal - 1
                    currentStep = SurveyStep.values()[prevOrdinal]
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp)) // Placeholder agar layout tidak geser
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )

            Spacer(modifier = Modifier.size(48.dp)) // Balance layout
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. DYNAMIC CONTENT (ANIMATED)
        Box(modifier = Modifier.weight(1f)) {
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = transitionSpec,
                label = "SurveyAnimation"
            ) { step ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (step) {
                        SurveyStep.GENDER -> StepGender(surveyData)
                        SurveyStep.BODY_STATS -> StepBodyStats(surveyData)
                        SurveyStep.GOAL -> StepGoal(surveyData)
                        SurveyStep.ACTIVITY -> StepActivity(surveyData)
                        SurveyStep.WORKOUT_PREF -> StepWorkout(surveyData)
                        SurveyStep.STEP_GOAL -> StepSteps(surveyData)
                    }
                }
            }
        }

        // 3. BOTTOM BUTTON
        Button(
            onClick = {
                if (currentStep == SurveyStep.STEP_GOAL) {
                    // --- FINAL SUBMISSION ---
                    // 1. Hitung BMI Terakhir
                    calculateBMI(surveyData.value)

                    // 2. Simpan ke Database/Pref (Simulasi)
                    // viewModel.saveProfile(surveyData.value)

                    // 3. Navigate to Dashboard (Pop Survey agar tidak bisa back)
                    navController.navigate("dashboard_route") {
                        popUpTo("survey_route") { inclusive = true }
                    }
                } else {
                    // Next Step
                    val nextOrdinal = currentStep.ordinal + 1
                    currentStep = SurveyStep.values()[nextOrdinal]
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = if (currentStep == SurveyStep.STEP_GOAL) "Finish" else "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// --- LOGIC CALCULATE BMI ---
fun calculateBMI(data: UserSurveyData) {
    try {
        val heightM = data.height.toFloat() / 100f
        val weightKg = data.weight.toFloat()
        if (heightM > 0) {
            val bmi = weightKg / (heightM * heightM)
            data.bmi = bmi
        }
    } catch (e: Exception) {
        data.bmi = 0f
    }
}

// --- SUB-SCREENS (STEPS) ---

@Composable
fun StepGender(data: MutableState<UserSurveyData>) {
    StepHeader(
        title = "What's your gender?",
        subtitle = "This will help us tailor your workout to match your metabolic rate perfectly."
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GenderCard(
            label = "Male",
            isSelected = data.value.gender == "Male",
            modifier = Modifier.weight(1f),
            onClick = { data.value = data.value.copy(gender = "Male") }
        )
        GenderCard(
            label = "Female",
            isSelected = data.value.gender == "Female",
            modifier = Modifier.weight(1f),
            onClick = { data.value = data.value.copy(gender = "Female") }
        )
    }
}

@Composable
fun StepBodyStats(data: MutableState<UserSurveyData>) {
    StepHeader(title = "Body Matrix", subtitle = "Help us calculate your BMI and daily needs.")

    SurveyInput(label = "Age (years)", value = data.value.age, onValueChange = { data.value = data.value.copy(age = it) }, isNumber = true)
    SurveyInput(label = "Height (cm)", value = data.value.height, onValueChange = { data.value = data.value.copy(height = it) }, isNumber = true)
    SurveyInput(label = "Current Weight (kg)", value = data.value.weight, onValueChange = { data.value = data.value.copy(weight = it) }, isNumber = true)
    SurveyInput(label = "Target Weight (kg)", value = data.value.targetWeight, onValueChange = { data.value = data.value.copy(targetWeight = it) }, isNumber = true)
}

@Composable
fun StepGoal(data: MutableState<UserSurveyData>) {
    StepHeader(title = "What's your main goal?", subtitle = "We will prioritize workouts based on this.")

    val goals = listOf("Lose Weight", "Gain Muscle", "Maintain Health")
    goals.forEach { goal ->
        SelectionCard(
            text = goal,
            isSelected = data.value.goal == goal,
            onClick = { data.value = data.value.copy(goal = goal) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun StepActivity(data: MutableState<UserSurveyData>) {
    StepHeader(title = "Physical Activity Level", subtitle = "How active are you currently?")

    val levels = listOf(
        "Sedentary" to "Little or no exercise",
        "Moderate" to "Exercise 3-4 times/week",
        "Active" to "Daily exercise or intense job"
    )

    levels.forEach { (level, desc) ->
        SelectionCard(
            text = level,
            subtext = desc,
            isSelected = data.value.activityLevel == level,
            onClick = { data.value = data.value.copy(activityLevel = level) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun StepWorkout(data: MutableState<UserSurveyData>) {
    StepHeader(title = "Preferred Workout", subtitle = "What kind of exercise do you enjoy?")

    val prefs = listOf("Cardio", "Strength Training")
    prefs.forEach { pref ->
        SelectionCard(
            text = pref,
            isSelected = data.value.workoutPreference == pref,
            onClick = { data.value = data.value.copy(workoutPreference = pref) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun StepSteps(data: MutableState<UserSurveyData>) {
    StepHeader(title = "Daily Step Goal", subtitle = "Set a daily target to stay active.")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5E9), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "💡 5,000 steps is the recommended minimum to maintain basic health and reduce sedentary risks.",
            fontSize = 12.sp,
            color = Color(0xFF2E7D32)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    SurveyInput(
        label = "Steps per day",
        value = data.value.dailySteps,
        onValueChange = { data.value = data.value.copy(dailySteps = it) },
        isNumber = true
    )
}


// --- REUSABLE COMPONENTS ---

@Composable
fun StepHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SurveyInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isNumber: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))

        // Menggunakan Style yang SAMA dengan Login Page (InputGray)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.onSurfaceVariant) // Warna D1D1D1
                .padding(16.dp)
        )
    }
}

@Composable
fun GenderCard(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .aspectRatio(0.7f) // Tinggi proporsional
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder Icon (Bisa diganti Image)
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = if(label == "Male") "♂" else "♀", fontSize = 40.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
fun SelectionCard(
    text: String,
    subtext: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
            if (subtext != null) {
                Text(
                    text = subtext,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
                )
            }
        }

        if (isSelected) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
        }
    }
}
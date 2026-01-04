package com.example.d1_jetpackcompose.ui.screens.compactPhone.surveyScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.viewModel.AuthViewModel
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.composed
import androidx.compose.ui.text.TextStyle

// --- DATA MODELS ---
data class UserSurveyData(
    var gender: String = "",
    var age: String = "",
    var height: String = "",
    var weight: String = "",
    var targetWeight: String = "",
    var goal: String = "",
    var activityLevel: String = "",
    var workoutPreference: String = "",
    var dailySteps: String = "5000",
    var bmi: Float = 0f
)

enum class SurveyStep { GENDER, BODY_STATS, GOAL, ACTIVITY, WORKOUT_PREF, STEP_GOAL }

val SurveyInputGray = Color(0xFFD1D1D1)

private fun Modifier.noRippleClickableSurvey(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

@Composable
fun SurveyScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var currentStep by remember { mutableStateOf(SurveyStep.GENDER) }
    val surveyData = remember { mutableStateOf(UserSurveyData()) }

    // 💡 ADDED: Focus Manager
    val focusManager = LocalFocusManager.current

    val totalSteps = SurveyStep.values().size
    val progress = (currentStep.ordinal + 1).toFloat() / totalSteps.toFloat()

    val transitionSpec: AnimatedContentTransitionScope<SurveyStep>.() -> ContentTransform = {
        val direction = if (targetState.ordinal > initialState.ordinal) {
            AnimatedContentTransitionScope.SlideDirection.Left
        } else {
            AnimatedContentTransitionScope.SlideDirection.Right
        }
        slideIntoContainer(direction, animationSpec = tween(500)) togetherWith
                slideOutOfContainer(direction, animationSpec = tween(500))
    }

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
            .padding(24.dp)
            .padding(top = 40.dp)
    ) {
        // TOP BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (currentStep.ordinal > 0) {
                IconButton(onClick = {
                    val prevOrdinal = currentStep.ordinal - 1
                    currentStep = SurveyStep.values()[prevOrdinal]
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // CONTENT
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

        // BUTTON
        Button(
            onClick = {
                // 💡 Tutup keyboard saat tombol diklik
                focusManager.clearFocus()

                if (currentStep == SurveyStep.STEP_GOAL) {
                    // --- LOGIC FINISH ---
                    calculateBMI(surveyData.value)

                    // 1. Simpan ke Database
                    authViewModel.saveUserProfile(surveyData.value)

                    // 2. Pindah ke Dashboard
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
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
        Spacer(modifier = Modifier.height(40.dp))
    }
}

// --- HELPER FUNCTIONS ---
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

@Composable
fun StepGender(data: MutableState<UserSurveyData>) {
    StepHeader(
        "What's your gender?",
        "This will help us tailor your workout to match your metabolic rate perfectly."
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        GenderCard("Male", data.value.gender == "Male", Modifier.weight(1f)) {
            data.value = data.value.copy(gender = "Male")
        }
        GenderCard("Female", data.value.gender == "Female", Modifier.weight(1f)) {
            data.value = data.value.copy(gender = "Female")
        }
    }
}

@Composable
fun StepBodyStats(data: MutableState<UserSurveyData>) {
    StepHeader("Body Matrix", "Help us calculate your BMI and daily needs.")
    SurveyInput("Age (years)", data.value.age, { data.value = data.value.copy(age = it) }, true)
    SurveyInput(
        "Height (cm)",
        data.value.height,
        { data.value = data.value.copy(height = it) },
        true
    )
    SurveyInput(
        "Current Weight (kg)",
        data.value.weight,
        { data.value = data.value.copy(weight = it) },
        true
    )
    SurveyInput(
        "Target Weight (kg)",
        data.value.targetWeight,
        { data.value = data.value.copy(targetWeight = it) },
        true
    )
}

@Composable
fun StepGoal(data: MutableState<UserSurveyData>) {
    StepHeader("What's your main goal?", "We will prioritize workouts based on this.")
    listOf("Lose Weight", "Gain Muscle", "Maintain Health").forEach { goal ->
        SelectionCard(goal, isSelected = data.value.goal == goal) {
            data.value = data.value.copy(goal = goal)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun StepActivity(data: MutableState<UserSurveyData>) {
    StepHeader("Physical Activity Level", "How active are you currently?")
    listOf(
        "Sedentary" to "Little or no exercise",
        "Moderate" to "Exercise 3-4 times/week",
        "Active" to "Daily exercise or intense job"
    ).forEach { (l, d) ->
        SelectionCard(l, d, data.value.activityLevel == l) {
            data.value = data.value.copy(activityLevel = l)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun StepWorkout(data: MutableState<UserSurveyData>) {
    StepHeader("Preferred Workout", "What kind of exercise do you enjoy?")
    listOf("Cardio", "Strength Training").forEach { pref ->
        SelectionCard(pref, isSelected = data.value.workoutPreference == pref) {
            data.value = data.value.copy(workoutPreference = pref)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun StepSteps(data: MutableState<UserSurveyData>) {
    StepHeader("Daily Step Goal", "Set a daily target to stay active.")
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5E9), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            "💡 5,000 steps is the recommended minimum.",
            fontSize = 12.sp,
            color = Color(0xFF2E7D32)
        )
    }
    Spacer(Modifier.height(24.dp))
    SurveyInput(
        "Steps per day",
        data.value.dailySteps,
        { data.value = data.value.copy(dailySteps = it) },
        true
    )
}

@Composable
fun StepHeader(title: String, subtitle: String) {
    Column(Modifier
        .fillMaxWidth()
        .padding(bottom = 32.dp)) {
        Text(
            title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(subtitle, fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun SurveyInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isNumber: Boolean = false
) {
    Column(Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        Text(
            label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BasicTextField(
            value = value, onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurveyInputGray)
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
        modifier
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White)
            .border(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(80.dp)
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) { Text(if (label == "Male") "♂" else "♀", fontSize = 40.sp, color = Color.Gray) }
        Spacer(Modifier.height(16.dp))
        Text(
            label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
fun SelectionCard(text: String, subtext: String? = null, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(20.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
            if (subtext != null) Text(
                subtext,
                fontSize = 12.sp,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
            )
        }
        if (isSelected) Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White)
    }
}
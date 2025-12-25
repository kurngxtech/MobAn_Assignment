package com.example.d1_jetpackcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,

    // --- TAMBAHAN FIELD DARI SURVEY ---
    val gender: String = "-",
    val age: Int = 0,
    val height: Float = 0f, // dalam cm
    val weight: Float = 0f, // dalam kg
    val bmi: Float = 0f,
    val activityLevel: String = "",
    val goal: String = "",
    val dailyStepsGoal: Int = 5000
)
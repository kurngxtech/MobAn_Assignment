package com.example.d1_jetpackcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ActivityType { FOOD, EXERCISE }

@Entity(tableName = "activity_table")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID Unik untuk Edit/Delete
    val type: ActivityType,
    val title: String,          // Nama Makanan / Latihan
    val timestamp: Long,        // Waktu pembuatan

    // Data Dinamis (Bisa 0 atau null jika tidak relevan dengan tipenya)
    val calories: Int = 0,      // Intake (Food) atau Burned (Exercise)
    val distance: Double = 0.0, // Exercise Only
    val duration: Int = 0       // Exercise Only
)
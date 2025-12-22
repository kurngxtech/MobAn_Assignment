package com.example.d1_jetpackcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// 1. Definisikan Data Class untuk menampung hasil hitungan
data class DashboardStats(
    val totalCaloriesIntake: Int = 0,
    val totalCaloriesBurned: Int = 0,
    val totalDistance: Double = 0.0,
    val totalDuration: Int = 0,
    val recentActivities: List<ActivityEntity> = emptyList()
)

class SharedViewModel(private val repository: ActivityRepository) : ViewModel() {

    // Source of Truth
    val allActivities: StateFlow<List<ActivityEntity>> = repository.allActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 💡 SOLUSI ERROR: Logika penghitung otomatis untuk Dashboard
    val dashboardStats: StateFlow<DashboardStats> = allActivities.map { list ->
        DashboardStats(
            totalCaloriesIntake = list.filter { it.type == ActivityType.FOOD }.sumOf { it.calories },
            totalCaloriesBurned = list.filter { it.type == ActivityType.EXERCISE }.sumOf { it.calories },
            totalDistance = list.filter { it.type == ActivityType.EXERCISE }.sumOf { it.distance },
            totalDuration = list.filter { it.type == ActivityType.EXERCISE }.sumOf { it.duration },
            recentActivities = list.take(3) // Ambil 3 data terbaru untuk Dashboard
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())

    // State untuk Detail Log
    private val _selectedActivity = MutableStateFlow<ActivityEntity?>(null)
    val selectedActivity: StateFlow<ActivityEntity?> = _selectedActivity.asStateFlow()

    // --- CRUD ACTIONS ---
    fun addActivity(title: String, type: ActivityType, calories: Int, distance: Double = 0.0, duration: Int = 0) {
        viewModelScope.launch {
            repository.insert(ActivityEntity(
                type = type, title = title, timestamp = System.currentTimeMillis(),
                calories = calories, distance = distance, duration = duration
            ))
        }
    }

    fun loadActivityById(id: Int) {
        viewModelScope.launch {
            repository.getById(id).collect { _selectedActivity.value = it }
        }
    }

    fun updateActivity(activity: ActivityEntity) {
        viewModelScope.launch { repository.update(activity) }
    }

    fun deleteActivity(activity: ActivityEntity) {
        viewModelScope.launch {
            repository.delete(activity)
            _selectedActivity.value = null
        }
    }
}

class SharedViewModelFactory(private val repository: ActivityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
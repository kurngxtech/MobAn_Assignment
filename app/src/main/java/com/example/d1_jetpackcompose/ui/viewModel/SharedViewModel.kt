package com.example.d1_jetpackcompose.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

// 1. Enum untuk mendefinisikan pilihan filter
enum class TimePeriod { DAILY, WEEKLY }
enum class CategoryFilter { ALL, EXERCISE, FOOD }

// 2. Data Class untuk menampung hasil hitungan Dashboard
data class DashboardStats(
    val totalCaloriesIntake: Int = 0,
    val totalCaloriesBurned: Int = 0,
    val totalDistance: Double = 0.0,
    val totalDuration: Int = 0,
    val recentActivities: List<ActivityEntity> = emptyList()
)

class SharedViewModel(private val repository: ActivityRepository) : ViewModel() {

    // --- STATE UTAMA (Raw Data dari Database) ---
    private val _allActivities = repository.allActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- STATE FILTER (Pilihan User) ---
    private val _selectedPeriod = MutableStateFlow(TimePeriod.DAILY)
    val selectedPeriod = _selectedPeriod.asStateFlow()

    private val _selectedCategory = MutableStateFlow(CategoryFilter.ALL)
    val selectedCategory = _selectedCategory.asStateFlow()

    // --- LOGIKA FILTERING OTOMATIS (Reactive Layer) ---
    private val filteredActivities = combine(
        _allActivities,
        _selectedPeriod,
        _selectedCategory
    ) { list, period, category ->
        list.filter { activity ->
            // Filter Berdasarkan Waktu
            val matchesTime = when (period) {
                TimePeriod.DAILY -> isSameDay(activity.timestamp, System.currentTimeMillis())
                TimePeriod.WEEKLY -> isSameWeek(activity.timestamp, System.currentTimeMillis())
            }

            // Filter Berdasarkan Kategori
            val matchesCategory = when (category) {
                CategoryFilter.ALL -> true
                CategoryFilter.EXERCISE -> activity.type == ActivityType.EXERCISE
                CategoryFilter.FOOD -> activity.type == ActivityType.FOOD
            }

            matchesTime && matchesCategory
        }
    }

    // --- OUTPUT STATE (Diconsume oleh UI) ---

    val dashboardStats: StateFlow<DashboardStats> = filteredActivities.map { list ->
        DashboardStats(
            totalCaloriesIntake = list.filter { it.type == ActivityType.FOOD }.sumOf { it.calories },
            totalCaloriesBurned = list.filter { it.type == ActivityType.EXERCISE }.sumOf { it.calories },
            totalDistance = list.filter { it.type == ActivityType.EXERCISE }.sumOf { it.distance },
            totalDuration = list.filter { it.type == ActivityType.EXERCISE }.sumOf { it.duration },
            recentActivities = list.take(3)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())

    val activityLogList: StateFlow<List<ActivityEntity>> = filteredActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedActivity = MutableStateFlow<ActivityEntity?>(null)
    val selectedActivity: StateFlow<ActivityEntity?> = _selectedActivity.asStateFlow()


    // --- INIT DATA DUMMY ---
    init {
        insertDummyData()
    }

    private fun insertDummyData() {
        viewModelScope.launch {
            // Menggunakan first() untuk mengecek database saat ini
            val currentData = _allActivities.value
            if (currentData.isEmpty()) {
                Log.d("SharedViewModel", "Database kosong, memasukkan data dummy...")
                val calendar = Calendar.getInstance()
                val today = calendar.timeInMillis

                calendar.add(Calendar.DAY_OF_YEAR, -1)
                val yesterday = calendar.timeInMillis

                val dummyList = listOf(
                    ActivityEntity(
                        title = "Morning Run",
                        type = ActivityType.EXERCISE,
                        calories = 250,
                        distance = 3.5,
                        duration = 30,
                        timestamp = today
                    ),
                    ActivityEntity(
                        title = "Healthy Breakfast",
                        type = ActivityType.FOOD,
                        calories = 450,
                        timestamp = today
                    ),
                    ActivityEntity(
                        title = "Cycling",
                        type = ActivityType.EXERCISE,
                        calories = 400,
                        distance = 10.5,
                        duration = 45,
                        timestamp = yesterday
                    )
                )
                dummyList.forEach { repository.insert(it) }
            } else {
                Log.d("SharedViewModel", "Data sudah ada: ${currentData.size} item")
            }
        }
    }

    // --- ACTIONS ---

    fun setTimePeriod(period: TimePeriod) { _selectedPeriod.value = period }
    fun setCategoryFilter(category: CategoryFilter) { _selectedCategory.value = category }

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

    // --- DATE HELPERS ---
    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isSameWeek(t1: Long, t2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
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
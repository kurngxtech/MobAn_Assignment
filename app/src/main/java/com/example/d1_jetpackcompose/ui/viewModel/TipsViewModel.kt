package com.example.d1_jetpackcompose.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.d1_jetpackcompose.data.local.ActivityType
import com.example.d1_jetpackcompose.data.remote.model.HealthTip
import com.example.d1_jetpackcompose.data.repository.ActivityRepository
import com.example.d1_jetpackcompose.data.repository.TipsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TipsViewModel(
    private val repository: TipsRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private var allRemoteTips: List<HealthTip> = emptyList()

    // State yang dicari oleh Dashboard.kt
    private val _personalizedTips = MutableStateFlow<List<HealthTip>>(emptyList())
    val personalizedTips: StateFlow<List<HealthTip>> = _personalizedTips.asStateFlow()

    private val _showTipsCard = MutableStateFlow(true)
    val showTipsCard: StateFlow<Boolean> = _showTipsCard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchTips()
    }

    private fun isToday(timestamp: Long): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp)) == sdf.format(Date())
    }

    fun fetchTips() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Fetch data API sekali saja (cache memory)
                if (allRemoteTips.isEmpty()) {
                    allRemoteTips = repository.getOnlineTips()
                }

                // 2. Ambil Aktivitas Harian User
                val activities = activityRepository.allActivities.first()
                val todayActivities = activities.filter { isToday(it.timestamp) }

                // --- 💡 PERBAIKAN LOGIKA DISINI ---

                if (todayActivities.isEmpty()) {
                    // SKENARIO 0: Belum ada aktivitas -> KOSONGKAN LIST
                    // Ini akan memicu UI Dashboard menampilkan state "No activity logged today"
                    _personalizedTips.value = emptyList()
                } else {
                    val finalRecommendations = mutableListOf<HealthTip>()

                    // Hitung jumlah aktivitas spesifik
                    val exerciseCount = todayActivities.count { it.type == ActivityType.EXERCISE }
                    val mealCount = todayActivities.count { it.type == ActivityType.FOOD }

                    // A. Logika Exercise
                    if (exerciseCount > 0) {
                        val exerciseTips = allRemoteTips.filter { it.category == "Exercise Tips" }
                        val limit = if (exerciseCount == 1) 2 else 4
                        finalRecommendations.addAll(exerciseTips.shuffled().take(limit))
                    }

                    // B. Logika Meal
                    if (mealCount > 0) {
                        val mealTips = allRemoteTips.filter { it.category == "Meal Tips" }
                        val limit = if (mealCount == 1) 2 else 4
                        finalRecommendations.addAll(mealTips.shuffled().take(limit))
                    }

                    // 3. Finalisasi Data (Max 5 items)
                    _personalizedTips.value = finalRecommendations
                        .distinct()
                        .shuffled()
                        .take(5)
                }

            } catch (e: Exception) {
                Log.e("TIPS_ERROR", "Error: ${e.message}")
                _personalizedTips.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTipById(id: Int): HealthTip? {
        return allRemoteTips.find { it.id == id }
    }
}

class TipViewModelFactory(
    private val repository: TipsRepository,
    private val activityRepository: ActivityRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TipsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TipsViewModel(repository, activityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
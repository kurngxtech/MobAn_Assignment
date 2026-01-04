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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TipsViewModel(
    private val repository: TipsRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    // 1. Simpan data API di StateFlow agar bisa dipantau
    private val _remoteTips = MutableStateFlow<List<HealthTip>>(emptyList())

    // 2. Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 3. REACTIVE STREAM (SOLUSI UTAMA)
    // Menggabungkan stream Database (Realtime) & API.
    // Setiap kali user menambah Activity, blok ini otomatis jalan ulang!
    val personalizedTips: StateFlow<List<HealthTip>> = combine(
        activityRepository.allActivities, // Stream dari Database (Live)
        _remoteTips                       // Stream dari API
    ) { activities, tips ->

        // Jika data API belum masuk, return kosong
        if (tips.isEmpty()) {
            emptyList()
        } else {
            // Filter aktivitas HARI INI
            val todayActivities = activities.filter { isToday(it.timestamp) }

            if (todayActivities.isEmpty()) {
                // Return kosong -> UI akan tampilkan "No activity logged today"
                emptyList()
            } else {
                val finalRecommendations = mutableListOf<HealthTip>()

                val exerciseCount = todayActivities.count { it.type == ActivityType.EXERCISE }
                val mealCount = todayActivities.count { it.type == ActivityType.FOOD }

                // Logika Exercise
                if (exerciseCount > 0) {
                    val exerciseTips = tips.filter { it.category == "Exercise Tips" }
                    val limit = if (exerciseCount == 1) 2 else 4
                    finalRecommendations.addAll(exerciseTips.shuffled().take(limit))
                }

                // Logika Meal
                if (mealCount > 0) {
                    val mealTips = tips.filter { it.category == "Meal Tips" }
                    val limit = if (mealCount == 1) 2 else 4
                    finalRecommendations.addAll(mealTips.shuffled().take(limit))
                }

                // Ambil max 5 dan acak
                finalRecommendations
                    .distinct()
                    .shuffled()
                    .take(5)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchTips()
    }

    private fun isToday(timestamp: Long): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp)) == sdf.format(Date())
    }

    fun fetchTips() {
        viewModelScope.launch {
            // Cek cache dulu, kalau kosong baru request API
            if (_remoteTips.value.isEmpty()) {
                _isLoading.value = true
                try {
                    val data = repository.getOnlineTips()
                    _remoteTips.value = data
                    Log.d("TIPS_VM", "API Fetch Success: ${data.size} items")
                } catch (e: Exception) {
                    Log.e("TIPS_VM", "API Error: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun getTipById(id: Int): HealthTip? {
        return _remoteTips.value.find { it.id == id }
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
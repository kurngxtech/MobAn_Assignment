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
    private val activityRepository: ActivityRepository // 💡 Tambahkan ini
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
                // Fetch dari API
                if (allRemoteTips.isEmpty()) {
                    allRemoteTips = repository.getOnlineTips()
                }

                // Cek Aktivitas Harian
                val activities = activityRepository.allActivities.first()
                val todayActivities = activities.filter { isToday(it.timestamp) }

                if (todayActivities.isEmpty()) {
                    _personalizedTips.value = emptyList()
                } else {
                    val matched = mutableListOf<HealthTip>()

                    // 💡 Cek apakah ada aktivitas olahraga (Exercise)
                    val hasExercise = todayActivities.any { it.type == ActivityType.EXERCISE }

                    // 💡 Cek apakah ada aktivitas makan (Food)
                    val hasMeal = todayActivities.any { it.type == ActivityType.FOOD }

                    val filteredList = allRemoteTips.filter { tip ->
                        (hasExercise && tip.category == "Exercise Tips") ||
                                (hasMeal && tip.category == "Meal Tips")
                    }

                    if (matched.isEmpty()) {
                        // Jika tidak ada aktivitas, tampilkan semua secara acak
                        _personalizedTips.value = allRemoteTips.shuffled()
                    } else {
                        // 💡 SOLUSI: Tambahkan .shuffled() sebelum dikirim ke UI
                        _personalizedTips.value = matched.distinct().shuffled()
                    }

                    _personalizedTips.value = filteredList
                }
            } catch (e: Exception) {
                Log.e("TIPS_ERROR", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTipById(id: Int): HealthTip? {
        return allRemoteTips.find { it.id == id }
    }
}

// 💡 Update Factory agar menerima dua parameter
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
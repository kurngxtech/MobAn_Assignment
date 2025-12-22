package com.example.d1_jetpackcompose.data.repository

import com.example.d1_jetpackcompose.data.local.ActivityDao
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val activityDao: ActivityDao) {

    // Mengambil semua data history secara realtime
    val allActivities: Flow<List<ActivityEntity>> = activityDao.getAllActivities()

    // Mengambil satu data berdasarkan ID untuk halaman DetailLog
    fun getById(id: Int): Flow<ActivityEntity?> {
        return activityDao.getActivityById(id)
    }

    // Fungsi suspend untuk operasi tulis (dijalankan di background thread oleh ViewModel)
    suspend fun insert(activity: ActivityEntity) {
        activityDao.insertActivity(activity)
    }

    suspend fun update(activity: ActivityEntity) {
        activityDao.updateActivity(activity)
    }

    suspend fun delete(activity: ActivityEntity) {
        activityDao.deleteActivity(activity)
    }
}
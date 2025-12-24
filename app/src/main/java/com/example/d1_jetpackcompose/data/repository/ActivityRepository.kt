package com.example.d1_jetpackcompose.data.repository

import com.example.d1_jetpackcompose.data.local.ActivityDao
import com.example.d1_jetpackcompose.data.local.ActivityEntity
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val activityDao: ActivityDao) {

    val allActivities: Flow<List<ActivityEntity>> = activityDao.getAllActivities()

    fun getById(id: Int): Flow<ActivityEntity?> {
        return activityDao.getActivityById(id)
    }

    suspend fun insert(activity: ActivityEntity) {
        activityDao.insertActivity(activity)
    }

    suspend fun update(activity: ActivityEntity) {
        activityDao.updateActivity(activity)
    }

    suspend fun delete(activity: ActivityEntity) {
        activityDao.deleteActivity(activity)
    }

    // --- MODIFIKASI: Fungsi ini sekarang menghapus isi database ---
    suspend fun clearUserData() {
        activityDao.deleteAllActivities()
    }
}
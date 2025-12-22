package com.example.d1_jetpackcompose.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    // CREATE: Insert data baru
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    // READ: Ambil semua data (Flow = Realtime Update)
    @Query("SELECT * FROM activity_table ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<ActivityEntity>>

    // READ: Ambil satu data spesifik untuk DetailLog
    @Query("SELECT * FROM activity_table WHERE id = :id")
    fun getActivityById(id: Int): Flow<ActivityEntity?>

    // UPDATE: Update data yang sudah ada
    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    // DELETE: Hapus data
    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)
}
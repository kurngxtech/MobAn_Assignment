package com.example.d1_jetpackcompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 💡 UPDATE: Versi dinaikkan ke 5 karena ada perubahan di UserEntity (tambah preferredWorkout)
@Database(entities = [ActivityEntity::class, UserEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartfit_database"
                )
                    // Reset database jika ada perubahan versi (Data user akan terhapus, perlu login/register ulang)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.d1_jetpackcompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// TAMBAHKAN UserEntity ke dalam entities
@Database(entities = [ActivityEntity::class, UserEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao // TAMBAHKAN INI

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Saat merubah schema (menambah UserEntity), kita perlu fallbackToDestructiveMigration
                // atau menaikkan version number.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartfit_database"
                )
                    .fallbackToDestructiveMigration() // Hati-hati, ini akan reset data lama jika versi naik
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
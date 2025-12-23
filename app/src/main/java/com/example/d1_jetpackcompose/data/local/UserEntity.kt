package com.example.d1_jetpackcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String // Di aplikasi riil, ini harus di-hash. Untuk prototype, kita simpan string.
)
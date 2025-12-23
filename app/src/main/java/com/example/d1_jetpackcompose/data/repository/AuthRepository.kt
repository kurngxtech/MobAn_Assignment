package com.example.d1_jetpackcompose.data.repository

import com.example.d1_jetpackcompose.data.local.UserDao
import com.example.d1_jetpackcompose.data.local.UserEntity

class AuthRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity): Boolean {
        // Cek apakah email sudah ada
        val existingUser = userDao.getUserByEmail(user.email)
        if (existingUser != null) {
            return false // Email sudah terdaftar
        }
        userDao.registerUser(user)
        return true
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }
}
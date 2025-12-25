package com.example.d1_jetpackcompose.data.repository

import com.example.d1_jetpackcompose.data.local.UserDao
import com.example.d1_jetpackcompose.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity): Boolean {
        val existingUser = userDao.getUserByEmail(user.email)
        if (existingUser != null) {
            return false
        }
        userDao.registerUser(user)
        return true
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    // --- BARU ---
    suspend fun updateUserProfile(user: UserEntity) {
        userDao.updateUser(user)
    }

    fun getCurrentUserFlow(username: String): Flow<UserEntity?> {
        return userDao.getUserByUsernameFlow(username)
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }
}
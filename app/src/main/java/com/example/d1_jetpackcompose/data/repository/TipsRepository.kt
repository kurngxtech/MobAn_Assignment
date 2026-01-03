package com.example.d1_jetpackcompose.data.repository

import android.util.Log
import com.example.d1_jetpackcompose.data.remote.ApiService
import com.example.d1_jetpackcompose.data.remote.RetrofitClient
import com.example.d1_jetpackcompose.data.remote.model.HealthTip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Menggunakan konstruktor default agar mudah dipanggil
class TipsRepository(private val api: ApiService = RetrofitClient.instance) {

    suspend fun getOnlineTips(): List<HealthTip> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getHealthTips()
                val data = response.tips
                Log.d("API_SUCCESS", "Data loaded: ${data.size} items")
                data
            } catch (e: Exception) {
                Log.e("API_ERROR", "Gagal: ${e.message}")
                emptyList()
            }
        }
    }
}
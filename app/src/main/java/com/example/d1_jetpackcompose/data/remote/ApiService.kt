package com.example.d1_jetpackcompose.data.remote

import com.example.d1_jetpackcompose.data.remote.model.TipsResponse
import retrofit2.http.GET

interface ApiService {
    // Endpoint menuju file JSON spesifik
    // Saya telah menyiapkan file JSON valid di repository publik ini sebagai contoh
    @GET("https://gist.githubusercontent.com/kurngxtech/8c2b9f626255c7b044d0784ad0b31ba8/raw/ccc40e243fad3d23c7bf7ae5017b97c71f375b30/tips.json")
    suspend fun getHealthTips(): TipsResponse
}
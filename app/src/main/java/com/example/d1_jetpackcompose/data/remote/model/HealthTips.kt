package com.example.d1_jetpackcompose.data.remote.model

import com.google.gson.annotations.SerializedName

// Model ini mencocokkan struktur JSON dari API
data class HealthTip(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)
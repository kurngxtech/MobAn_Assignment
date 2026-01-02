package com.example.d1_jetpackcompose.data.remote.model

import com.google.gson.annotations.SerializedName

data class TipsResponse(
    @SerializedName("tips") val tips: List<HealthTip>
)

data class HealthTip(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String, // run, walk, heavy_meal, etc
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("content") val content: String // Artikel Lengkap
)
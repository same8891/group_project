package com.example.groupproject.data.model

import com.google.firebase.Timestamp

data class Review(
    val reviewId: String = "",
    val description: String = "",
    val destination: String = "",
    val photos: List<String> = emptyList(),
    val rating: Int = 0,
    val timestamp: Timestamp? = null,
    val likes: Int = 0,
    val userId: String = ""
)
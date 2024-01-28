package com.example.groupproject.data.model

data class Review(
    val reviewId: String,
    val description: String,
    val destination: String,
    val photos: List<String>,
    val isPublic: Boolean,
    val rating: Int,
    val timestamp: String, // You might want to use a Date type here
    val title: String,
    val userId: String
)
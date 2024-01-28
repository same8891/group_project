package com.example.groupproject.data.model

data class User(
    val userId: String,
    val currency: List<String>,
    val displayName: String,
    val email: String,
    val password: String,
    val saves: List<String>,
    val trips: List<Trip>,
    val profile: List<Profile>,
    val reviews: List<Review>
)

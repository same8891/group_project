package com.example.groupproject.data.model

data class User(
    val userId: String = "",
    val currency: List<String> = emptyList(),
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val saves: List<String> = emptyList(),
    val trips: List<Trip> = emptyList(),
    val profile: List<Profile> = emptyList(),
    val reviews: List<Review> = emptyList()
)

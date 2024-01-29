package com.example.groupproject.data.model

data class User(
    val currency: String = "",
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val saves: List<String> = emptyList(),

    val trips: List<Trip> = emptyList(),
    val profile: List<Profile> = emptyList(),
    val reviews: List<Review> = emptyList()
)

package com.example.groupproject.data.model

data class User(
    val currency: String = "",
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val saves: List<String> = emptyList(),

    var trips: List<Trip> = emptyList(),
    var profile: List<Profile> = emptyList(),
    var reviews: List<Review> = emptyList()
)

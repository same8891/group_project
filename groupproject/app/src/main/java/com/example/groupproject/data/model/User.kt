package com.example.groupproject.data.model

data class User(
    var userId: String = "",
    val currency: String = "",
    var displayName: String = "",
    val email: String = "",
    val password: String = "",
    val saves: List<String> = emptyList(),
    var trips: List<Trip> = emptyList(),
    var profile: List<Profile> = emptyList(),
    var reviews: MutableList<Review> = mutableListOf()
)

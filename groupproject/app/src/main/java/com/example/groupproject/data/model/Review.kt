package com.example.groupproject.data.model

import com.google.firebase.Timestamp

data class Review(
    var reviewId: String = "",
    var description: String = "",
    val destination: String = "",
    var isPublic: Boolean = true,
    val photos: List<String> = emptyList(),
    var rating: Int = 0,
    val timestamp: Timestamp? = null,
    val likes: Int = 0,
    val userId: String = "",
    var title:String = ""
)
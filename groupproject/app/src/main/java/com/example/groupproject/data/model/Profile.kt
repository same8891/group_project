package com.example.groupproject.data.model

data class Profile(
    val profileId: String = "",
    val aboutYou: String = "",
    val fullName: String = "",
    val joinDate: String = "",
    val location: String = "",
    val photoImage: String = "",
    val uploadImages: List<String> = emptyList()
)
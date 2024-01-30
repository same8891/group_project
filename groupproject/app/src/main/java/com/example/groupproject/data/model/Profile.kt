package com.example.groupproject.data.model

data class Profile(
    var profileId: String = "",
    val aboutYou: String = "",
    var fullName: String = "",
    val joinDate: String = "",
    val location: String = "",
    val photoImage: String = "",
    val uploadImages: MutableList<String> = mutableListOf(),
)
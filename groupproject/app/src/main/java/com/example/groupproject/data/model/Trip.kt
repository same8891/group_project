package com.example.groupproject.data.model

data class Trip(
    val collaborators: List<String>,
    val description: String,
    val destinationList: List<String>,
    val duration: String,
    val isPrivate: Boolean,
    val title: String
)
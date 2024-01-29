package com.example.groupproject.data.model

data class Trip(
    val tripId: String = "",
    val collaborators: List<String> = emptyList(),
    val description: String = "",
    val destinationList: List<String> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val isPrivate: Boolean = false,
    val title: String = ""
)
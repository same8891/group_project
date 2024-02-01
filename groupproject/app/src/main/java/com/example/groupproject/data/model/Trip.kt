package com.example.groupproject.data.model

data class Trip(
    var tripId: String = "",
    val title: String = "",
    val numberOfPeople: Int=0,
    val description: String = "",
    var destinationList: MutableList<String> = ArrayList(),
    val startDate: String = "",
    val endDate: String = "",
    val isPrivate: Boolean = false
)
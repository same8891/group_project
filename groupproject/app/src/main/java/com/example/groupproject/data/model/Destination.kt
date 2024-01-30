package com.example.groupproject.data.model

data class Destination(
    val activityList: List<String> = emptyList(),
    val description: String = "",
    val images: List<String> = emptyList(),
    val likes: Int = 0,
    val localCurrencies: List<String> = emptyList(),
    val localLanguage: List<String> = emptyList(),
    val location: String = "",
    val name: String = "",
    val price: Price = Price(),
    var reviews: List<Review> = emptyList(),
    var tags: List<String> = emptyList()
)

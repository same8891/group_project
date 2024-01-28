package com.example.groupproject.data.model

data class Destination(
    val destinationId: String = "",
    val activityList: List<String> = emptyList(),
    val ageRecommendation: String = "",
    val description: String = "",
    val favoriteSpots: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val likes: Int = 0,
    val localCurrencies: List<String> = emptyList(),
    val localLanguages: List<String> = emptyList(),
    val location: String = "",
    val name: String = "",
    val ownerOrganization: String = "",
    val price: Price = Price(),
    val reviews: List<Review> = emptyList()
)
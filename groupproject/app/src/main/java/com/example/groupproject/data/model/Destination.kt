package com.example.groupproject.data.model

data class Destination(
    val destinationId: String,
    val activityList: List<String>,
    val ageRecommendation: String,
    val description: String,
    val favoriteSpots: List<String>,
    val images: List<String>,
    val likes: Int,
    val localCurrencies: List<String>,
    val localLanguages: List<String>,
    val location: String,
    val name: String,
    val ownerOrganization: String,
    val price: Price,
    val reviews: List<Review>
)
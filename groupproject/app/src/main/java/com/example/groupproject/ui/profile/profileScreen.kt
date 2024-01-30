package com.example.groupproject.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.groupproject.R
import com.example.groupproject.data.model.User
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.Review
import com.example.groupproject.data.model.Trip


@Composable
fun profileScreen(navController: NavController, profileViewModel: profileViewModel) {
    val context = navController.context
    val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    val user by profileViewModel.user.collectAsState()

    LaunchedEffect(userEmail) {
        profileViewModel.getUser(userEmail)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            user?.let { displayUser(it) }
            Button(
                onClick = {
                    val profile = user?.profile?.get(0)
                    if (profile != null) {
                        profile.fullName = "New Name"
                        profileViewModel.updateProfile(user?.userId ?: "", profile)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Edit Profile")
            }
        }
    }
}

@Composable
fun displayUser(user: User) {
    Text(text = "User email: ${user.email}")
    Text(text = "Display Name: ${user.displayName}")
    Text(text = "Currency: ${user.currency}")

    // Display Trips
    Text(text = "Trips:")
    for (trip in user.trips) {
        displayTrip(trip)
    }

    // Display Profile
    Text(text = "Profile:")
    for (profile in user.profile) {
        displayUserProfile(profile)
    }

    // Display Reviews
    Text(text = "Reviews:")
    for (review in user.reviews) {
        displayReview(review)
    }
}

@Composable
fun displayTrip(trip: Trip) {
    Text(text = "Title: ${trip.title}")
    Text(text = "Description: ${trip.description}")
    Text(text = "Start Date: ${trip.startDate}")
    Text(text = "End Date: ${trip.endDate}")
    // Add more fields as needed
}

@Composable
fun displayUserProfile(profile: Profile) {
    Text(text = "Full Name: ${profile.fullName}")
    Text(text = "About You: ${profile.aboutYou}")
    Text(text = "Join Date: ${profile.joinDate}")
    Text(text = "Location: ${profile.location}")
    // Add more fields as needed
}

@Composable
fun displayReview(review: Review) {
    Text(text = "Description: ${review.description}")
    Text(text = "Rating: ${review.rating}")
    // Add more fields as needed
}

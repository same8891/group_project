package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Trip

@Composable
fun tripsScreen(navHostController: NavHostController, tripsViewModel: tripsViewModel) {
    // Fetch user trips when the screen is created
    val context = navHostController.context
    val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    tripsViewModel.getUserTrips(userEmail)

    // Observe the trips LiveData
    val trips = tripsViewModel.trips.value

    // Display the list of trips
    LazyColumn {
        items(trips) { trip ->
            TripItem(trip = trip)
        }
    }
}

@Composable
fun TripItem(trip: Trip) {
    // Display each trip item
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Title: ${trip.title}")
        Text(text = "Destinations: ${trip.destinationList.toString()}")
        Text(text = "Start Date: ${trip.startDate}")
        Text(text = "End Date: ${trip.endDate}")
        Text(text = "Collaborators: ${trip.collaborators.toString()}")
    }
}
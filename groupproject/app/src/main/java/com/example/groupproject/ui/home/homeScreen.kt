package com.example.groupproject.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.User

@Composable
fun homeScreen(navController: NavHostController, homeViewModel: homeViewModel) {
    // Fetch all destinations when the screen is created
    homeViewModel.getAllDestinations()

    // Observe the destinations LiveData
    val destinations = homeViewModel.destinations.value

    // Display the list of destinations
    LazyColumn {
        items(destinations) { destination ->
            DestinationItem(destination = destination)
        }
    }
}

@Composable
fun DestinationItem(destination: Destination) {
    // Display each destination item
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Name: ${destination.name}")
        Text(text = "Location: ${destination.location}")
        Text(text = "Description: ${destination.description}")
        Text(text = "Reviews: ${destination.reviews.toString()}")
    }
}
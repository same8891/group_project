package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Trip
import com.example.groupproject.data.model.Destination

@Composable
fun tripDetailScreen(
    navController: NavHostController,
    tripId: String,
    tripsViewModel: tripsViewModel
) {
    var sharedPref: SharedPreferences = navController.context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    var userId = sharedPref.getString("email", "") ?: ""
    var trip by remember { mutableStateOf(Trip()) }
    LaunchedEffect(key1 = true) {
        tripsViewModel.getTrip(userId, tripId) { tripData ->
            trip = tripData ?: Trip()
        }
    }
    Text(text = "Trip Detail Screen")
    Text(text = "Trip ID: $tripId")

    //Show Destination List
    LazyColumn {
        items(trip.destinationList.size) { index ->
            val destination = trip.destinationList[index]
            Text(
                text = destination,
                modifier = Modifier.clickable {
                    navController.navigate("destinationDetail/${destination}")
                }
            )
        }
    }
}
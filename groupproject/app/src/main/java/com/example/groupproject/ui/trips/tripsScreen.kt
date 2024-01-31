package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Destination

@Composable
fun tripsScreen(navHostController: NavHostController, tripsViewModel: tripsViewModel) {
    // Fetch user trips when the screen is created
    val context = navHostController.context
    val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    tripsViewModel.getUserTrips(userEmail)
    var showAddDialog by remember { mutableStateOf(false) }

    // Observe the trips LiveData
    val trips = tripsViewModel.trips.value

    // Display the list of trips
    Column(Modifier.padding(16.dp)) {
        LazyColumn {
            items(trips) { trip ->
                TripCard(trip = trip, destination= Destination())
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // add the createTripButton at the bottom right corner
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add padding for spacing
            contentAlignment = Alignment.BottomEnd
        ) {
            createTripButton(onClick = {
                showAddDialog = true
            })
            AddTripDialog(
                showAddDialog = showAddDialog,
                onDismissRequest = { showAddDialog = false },
                onConfirmClick = { showAddDialog = false }
            )
        }
    }
}

@Composable
fun createTripButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Add, "create trip fab") },
        text = { Text(text = "New Trip") },
    )
}


package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column

//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun tripsScreen(
    navHostController: NavHostController,
    tripsViewModel: tripsViewModel
) {
    // Fetch user trips when the screen is created
    val context = navHostController.context
    val sharedPref: SharedPreferences =
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    tripsViewModel.getUserTrips(userEmail, {})
    var showAddDialog by remember { mutableStateOf(false) }
    // Observe the trips LiveData
    val trips = tripsViewModel.trips.value
    Text(
        text = "Your Trips",
        color = MaterialTheme.colorScheme.background,
        fontSize = MaterialTheme.typography.displayMedium.fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
    )
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Filled.Add, "create trip fab") },
                text = { Text(text = "New Trip") },
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = innerPadding
            ) {
                items(trips) { trip ->
                    TripCard(
                        trip = trip,
                        navHostController,
                        tripsViewModel,
                        onClick = {
                            // 在这里执行导航到个人资料页面的操作
                            navHostController.navigate("tripDetail/${trip.tripId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
    tripsViewModel.getAllDestinations()
    val destinations = tripsViewModel.destinations.value
    val destinationNames = destinations.map { it.name }
            AddTripDialog(
            navHostController=navHostController,
            showAddDialog = showAddDialog,
            onDismissRequest = { showAddDialog = false },
            onConfirmClick = { showAddDialog = false},
            tripsViewModel = tripsViewModel,
                destinationNames = destinationNames
        )
}

//    // Display the list of trips
//    Column(Modifier.padding(16.dp)) {
//        LazyColumn {
//            items(trips) { trip ->
//                TripCard(trip = trip, navHostController, tripsViewModel)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//
//        Column(
//            horizontalAlignment = Alignment.End,
//            verticalArrangement =
//        ){
//            createTripButton(
//                onClick = {showAddDialog = true}
//            )
//        }
//
//        AddTripDialog(
//            navHostController=navHostController,
//            showAddDialog = showAddDialog,
//            onDismissRequest = { showAddDialog = false },
//            onConfirmClick = { showAddDialog = false },
//            tripsViewModel = tripsViewModel
//        )
//    }
//}

//@Composable
//fun createTripButton(onClick: () -> Unit) {
//    ExtendedFloatingActionButton(
//        onClick = { onClick() },
//        icon = { Icon(Icons.Filled.Add, "create trip fab") },
//        text = { Text(text = "New Trip") }
//    )
//}


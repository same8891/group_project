package com.example.groupproject.ui.trips

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Trip

@Composable
fun TripCard(trip: Trip, navHostController: NavHostController, tripsViewModel: tripsViewModel, onClick: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val allDestinationIds by remember { mutableStateOf(mutableListOf<String>()) }
    var totalCost by remember { mutableStateOf(trip.totalCost) }

    LaunchedEffect(key1 = true) {
        allDestinationIds.clear()
        tripsViewModel.getAllDestinationIds { destinationIdsList ->
            allDestinationIds.addAll(destinationIdsList ?: emptyList())
        }
        tripsViewModel.calculateTotalCostForTrip(trip) { cost ->
            totalCost = cost
        }
    }

    Log.d("TripCard's DestinationIds", "$allDestinationIds")
    ElevatedCard(

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint,
        ),
        elevation = CardDefaults.elevatedCardElevation()

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier
                    .weight(8f)
            ){
                Text(
                    text = "Title: ${trip.title}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp).clickable(onClick = onClick)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                ){
                    Column(modifier = Modifier
                        .weight(1f))
                    {
                        Text(text = "Start Date: ${trip.startDate}")
                        Text(text = "End Date: ${trip.endDate}")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier
                        .weight(1f))
                    {
                        Text(text = "Number Of People: ${trip.numberOfPeople.toString()}")
                        Text(text = "Total Cost: $totalCost")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Destinations: ${trip.destinationList.toString()}")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
//                    .background(Color.DarkGray)
            ){
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    Modifier.fillMaxHeight()
                ){
                    editTripButton(onClick = {
                        showEditDialog=true
                    })
                    EditTripDialog(
                        showEditDialog = showEditDialog,
                        trip = trip,
                        AllDestination=allDestinationIds,
                        onDismissRequest = { showEditDialog=false},
                        tripsViewModel=tripsViewModel,
                        navHostController = navHostController
                    )
                }
                Box(
                    Modifier.fillMaxHeight()
                ){
                    deleteTripButton(onClick = {
                        showDeleteDialog=true
                    })
                    DeleteTripDialog(
                        navHostController = navHostController,
                        showDeleteDialog = showDeleteDialog,
                        onDismissRequest = { showDeleteDialog=false},
                        onConfirmClick = { showDeleteDialog=false},
                        tripId=trip.tripId,
                        tripsViewModel=tripsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun deleteTripButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
            )
        }
    )
}

@Composable
fun editTripButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
            )
        }
    )
}

package com.example.groupproject.ui.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.groupproject.data.model.Trip

@Composable
fun TripCard(trip: Trip, navHostController: NavHostController, tripsViewModel: tripsViewModel) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                //modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Title: ${trip.title}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                ){
                    Column(){
                        Text(text = "Start Date: ${trip.startDate}")
                        Text(text = "End Date: ${trip.endDate}")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(){
                        Text(text = "Number Of People: ${trip.numberOfPeople.toString()}")
                        Text(text = "Total Cost: ")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Destinations: ${trip.destinationList.toString()}")
            }
            Column(
                modifier = Modifier.fillMaxHeight()
            ){
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                ){
                    editTripButton(onClick = {
                        showEditDialog=true
                    })
                    EditTripDialog(
                        showEditDialog = showEditDialog,
                        trip = Trip(),
                        AllDestination=trip.destinationList,
                        onDismissRequest = { showEditDialog=false}
                    )
                }
                Box(){
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

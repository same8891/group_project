package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Trip

@Composable
fun AddTripDialog(
    navHostController: NavHostController,
    showAddDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    tripsViewModel: tripsViewModel,
    destinationNames: List<String>
) {
    var tripName by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableIntStateOf(1) }
    var isPublic by remember { mutableStateOf(true) }
    var description by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var totalCost by remember { mutableStateOf(0.0) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var context = navHostController.context
    var sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    var userId = sharedPref.getString("email", "") ?: ""
    var collaboratorName by remember { mutableStateOf("") }
    var collaborators by remember { mutableStateOf(listOf<String>(userId)) }
    var selectedDestinationIndex by remember { mutableIntStateOf(0) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = "Create New Trip",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        //handle the creation of the new trip
                        val newTrip = Trip(
                            title = title,
                            description = description,
                            totalCost = totalCost,
                            startDate = startDate,
                            endDate = endDate,
                            numberOfPeople = numberOfPeople,
                            collaborators = collaborators.toMutableList(),
                            isPrivate = !isPublic,
                            tripId = userId + title + startDate + endDate,
                            destinationList = listOf(destinationNames[selectedDestinationIndex]).toMutableList()
                        )
                        tripsViewModel.addTrip(userId, newTrip)
                        onConfirmClick()
                        onDismissRequest()

                    },
                    content = {
                        Text("Confirm")
                    }
                )
            },
            dismissButton = {
                Button(
                    onClick = onDismissRequest,
                    content = {
                        Text("Cancel")
                    }
                )
            },
            text = {

                Column {

                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = tripName,
                        onValueChange = { tripName=it},
                        label = { Text("Trip Name:") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description:") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title:") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = totalCost.toString(),
                        onValueChange = { totalCost = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Total Cost():") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    //Add Dropdown for destinations
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Destinations:",style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = destinationNames[selectedDestinationIndex],Modifier.clickable { isDropdownExpanded = true },
                            style = MaterialTheme.typography.titleLarge)
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier.width(200.dp)
                        ) {
                            destinationNames.forEachIndexed { index, destination ->
                                DropdownMenuItem(onClick = {
                                    selectedDestinationIndex = index
                                    isDropdownExpanded = false
                                }, text = {
                                    Text(destination)
                                })
                            }
                        }
                    }



                    Spacer(modifier = Modifier.height(16.dp))
                    // Date selection (you may replace this with a date picker)
                    TextField(

                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Start Date:") },

                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("End Date:") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Collaborators
                    Row {
                        TextField(
                            value = collaboratorName,
                            onValueChange = { collaboratorName = it },
                            label = { Text("Collaborator Name:") },
                            modifier = Modifier.width(200.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                if (collaboratorName.isNotBlank()) {
                                    collaborators = collaborators + collaboratorName
                                    collaboratorName = ""
                                    numberOfPeople = collaborators.size
                                }
                            },
                            content = {
                                Text("Add Collaborator")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Num. People: $numberOfPeople", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Collaborators Name:\n $collaborators", style = MaterialTheme.typography.bodyLarge)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = !isPublic,
                            onClick = { isPublic = false },
                            colors = RadioButtonDefaults.colors(Color.Black)
                        )
                        Text("Private", style = MaterialTheme.typography.bodyLarge)
                        RadioButton(
                            selected = isPublic,
                            onClick = { isPublic = true },
                            colors = RadioButtonDefaults.colors(Color.Black)
                        )
                        Text("Public", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        )
    }
}
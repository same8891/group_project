package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Calendar
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
    tripsViewModel: tripsViewModel
) {
    var tripName by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }

    val context = navHostController.context
    val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userId = sharedPref.getString("email", "") ?: ""

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
                            title = tripName,
                            numberOfPeople = numberOfPeople.toIntOrNull() ?: 0,
                            isPrivate = !isPublic,
                            description = "",
                            destinationList = ArrayList(),
                            startDate = "",
                            endDate = "")
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
                        value = numberOfPeople,
                        onValueChange = { numberOfPeople = it },
                        label = { Text("Number of People:") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isPublic,
                                onClick = { isPublic = true },
                                colors = RadioButtonDefaults.colors(Color.Black)
                            )
                            Text("Public", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.width(48.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !isPublic,
                                onClick = { isPublic = false },
                                colors = RadioButtonDefaults.colors(Color.Black)
                            )
                            Text("Private", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        )
    }
}

/*
@Composable
fun DatePickerRow() {
    var startDate by remember { mutableStateOf(Calendar.getInstance()) }
    var endDate by remember { mutableStateOf(Calendar.getInstance()) }

    Column {
        Row {
            Text("Start Date: ${formatDate(startDate)}", modifier = Modifier.clickable {
                showDatePicker(
                    startDate = startDate,
                    onDateSet = { newDate ->
                        startDate = newDate
                    }
                )
            })
        }
        Row {
            Text("End Date: ${formatDate(endDate)}", modifier = Modifier.clickable {
                showDatePicker(
                    startDate = endDate,
                    onDateSet = { newDate ->
                        endDate = newDate
                    }
                )
            })
        }
    }
}

@Composable
fun formatDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return dateFormat.format(calendar.time)
}

@Preview
@Composable
fun DatePickerRowPreview() {
    DatePickerRow()
}
*/


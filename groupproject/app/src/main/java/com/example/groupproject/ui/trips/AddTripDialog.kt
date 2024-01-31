package com.example.groupproject.ui.trips

import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp

@Composable
fun AddTripDialog(
    showAddDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
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
                var tripName by remember { mutableStateOf("") }
                var numberOfPeople by remember { mutableStateOf("") }
                var isPublic by remember { mutableStateOf(true) }

                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = tripName,
                        onValueChange = { newValue ->
                            tripName = newValue
                        },
                        label = { Text("Trip Name:") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = numberOfPeople,
                        onValueChange = { newValue -> numberOfPeople = newValue },
                        label = { Text("Number of People:") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))
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
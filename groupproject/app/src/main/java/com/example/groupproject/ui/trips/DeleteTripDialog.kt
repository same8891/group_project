package com.example.groupproject.ui.trips

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.data.FirebaseApi

@Composable
fun DeleteTripDialog(
    navHostController: NavHostController,
    showDeleteDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    tripId: String,
    tripsViewModel: tripsViewModel
) {
    val context = navHostController.context
    val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = "Delete Trip",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmClick()
                        onDismissRequest()
                        tripsViewModel.deleteTrip(userEmail, tripId)
                        tripsViewModel.getUserTrips(userEmail)
                    },
                    content = {
                        Text("Delete Trip")
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
                Column() {
                    Text("Are you sure you want to delete this trip?")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("This action CANNOT be reversed.")
                }
            }
        )
    }
}
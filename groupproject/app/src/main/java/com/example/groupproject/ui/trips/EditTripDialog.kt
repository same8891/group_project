package com.example.groupproject.ui.trips

import android.content.ComponentCallbacks
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Trip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripDialog(
    showEditDialog: Boolean,
    trip: Trip,
    AllDestination: List<String>,
    onDismissRequest: () -> Unit,
    tripsViewModel: tripsViewModel,
    navHostController: NavHostController
) {
    val context = navHostController.context
    val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userId = sharedPref.getString("email", "") ?: ""
    var expanded by remember { mutableStateOf(false) }
    val list = AllDestination.subtract(trip.destinationList).toList()
    Log.d("dl","$trip.destinationList")
    var select by remember { mutableStateOf(if (list.isNotEmpty()) list[0] else "") }
    var tripDestinationList by remember { mutableStateOf(trip.destinationList.toList())}
    Log.d("tripDestinationList", "EditTripDialog: $tripDestinationList")
    val icon = if (expanded) {
        Icons.Outlined.KeyboardArrowUp
    } else {
        Icons.Outlined.KeyboardArrowDown
    }
    if (showEditDialog){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = "Edit Trip",
                    style = MaterialTheme.typography.titleLarge
                )

            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismissRequest()
                        tripsViewModel.getUserTrips(userId, {})
                    },
                    content = {
                        Text("Save Changes")
                    }
                )
            },
            dismissButton = {
                Button(
                    onClick = onDismissRequest,
                    content = {
                        Text("Cancel")
                        tripsViewModel.getUserTrips(userId, {})
                    }
                )
            },
            text={
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center

                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .height(40.dp)
                        ) {
                            OutlinedTextField(
                                value = select,
                                onValueChange = { },
                                placeholder = {
                                    Text(text = select)
                                },
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = "Arrow"
                                    )
                                },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                            ) {
                                list.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = it,
                                                fontSize = 20.sp
                                            )
                                        },
                                        onClick = {
                                            select = it
                                            expanded = false
                                        }
                                    )
                                }
                            }

                        }
                        Button(
                            onClick = {
                                val newTrip = trip.copy(destinationList = tripDestinationList.plus(select).toMutableList())
                                tripsViewModel.updateTrip(userId,trip.tripId ,newTrip, callback = {
                                    if (it != null) {
                                        tripDestinationList = it.destinationList
                                    }
                                })
                            },
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            Text("Add")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Log.d("trip destination","$trip.destinationList.size")
                    if(tripDestinationList.size > 0) {
                        LazyColumn(
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            items(tripDestinationList) { destination ->
                                showitem(
                                    itemName = destination,
                                    onUpClick = {
                                        val tempId=tripDestinationList.indexOf(destination)
                                        swapWithPrevious(tripDestinationList,tempId, callback = {
                                            val newTrip = trip.copy(destinationList = it.toMutableList())
                                            tripsViewModel.updateTrip(userId,trip.tripId ,newTrip, callback = {
                                                if (it != null) {
                                                    tripDestinationList = it.destinationList
                                                }
                                            })
                                        })


                                    },
                                    onDownClick = {
                                        val tempId=tripDestinationList.indexOf(destination)
                                        swapWithNext(tripDestinationList,tempId, callback = {
                                            val newTrip = trip.copy(destinationList = it.toMutableList())
                                            tripsViewModel.updateTrip(userId,trip.tripId ,newTrip, callback = {
                                                if (it != null) {
                                                    tripDestinationList = it.destinationList
                                                }
                                            })
                                        })
                                    },
                                    onDeleteClick = {
                                        val newTrip = trip.copy(destinationList = tripDestinationList.minus(destination).toMutableList())
                                        tripsViewModel.updateTrip(userId,trip.tripId ,newTrip, callback = {
                                            if (it != null) {
                                                tripDestinationList = it.destinationList
                                            }
                                        })
                                    }
                                )
                            }
                        }
                    }
                    else{
                        Box(modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(300.dp) ){
                            Text(text = "The trip is empty",modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            },
            modifier = Modifier.padding(0.dp)
        )
    }
}
fun swapWithPrevious(destinationList: List<String>, index: Int, callback: (List<String>) -> Unit) {
    var newDestinationList = destinationList.toMutableList()
    if (index > 0) {
        val temp = newDestinationList[index]
        newDestinationList[index] = newDestinationList[index - 1]
        newDestinationList[index - 1] = temp
        callback(newDestinationList)
    }
}

fun swapWithNext(destinationList: List<String>, index: Int, callback: (List<String>) -> Unit) {
    var newDestinationList = destinationList.toMutableList()
    if (index < destinationList.size - 1) {
        val temp = newDestinationList[index]
        newDestinationList[index] = newDestinationList[index + 1]
        newDestinationList[index + 1] = temp
        callback(newDestinationList)
    }
}

@Preview(showBackground = true)
@Composable
fun preida() {
    var trip = listOf("kyoto", "Taipei", "NewYork")
    var alltrip = Trip(destinationList = mutableListOf("kyoto", "Taipei", "NewYork", "paris"))
//   EditTripDialog(true,alltrip,trip, tripsViewModel){}
//   showitem(itemName = "taipei", onUpClick = { }, onDownClick = {}, onDeleteClick = {})
}
@Composable
fun showitem(
    itemName: String,
    onUpClick: () -> Unit,
    onDownClick: () -> Unit,
    onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = Modifier
                .weight(0.5f)
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Row (verticalAlignment = Alignment.CenterVertically){
                // Up arrow
                IconButton(
                    onClick = { onUpClick() },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Up")
                }

                // Down arrow
                IconButton(
                    onClick = { onDownClick() },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Down")
                }
                IconButton(
                    onClick = {onDeleteClick()},
                    modifier = Modifier
                        .size(20.dp)
                        .size(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(text = itemName, fontSize = 20.sp)
                }
            }
        }
    }
}
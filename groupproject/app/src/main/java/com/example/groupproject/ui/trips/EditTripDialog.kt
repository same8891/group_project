package com.example.groupproject.ui.trips

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.groupproject.data.model.Trip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripDialog(
    showEditDialog: Boolean,
    trip: Trip,
    AllDestination: List<String>,
    onDismissRequest: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val list = AllDestination.subtract(trip.destinationList).toList()
    Log.d("dl","$trip.destinationList")
    var select by remember { mutableStateOf(if (list.isNotEmpty()) list[0] else "") }
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
                        // Handle the creation of the new trip here
                        onDismissRequest()
                        //firebaseApi.deleteTrip()
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
                    }
                )
            },
            text={
                Column(
                    modifier = Modifier
                        //                    .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center

                ) {
                    Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            },
                            modifier = Modifier
                                .width(180.dp)
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
                                    .height(300.dp),
                            ) {
                                list.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = it.toString())
                                        },
                                        onClick = {
                                            select = it.toString()
                                            expanded = false
                                        }
                                    )
                                }
                            }

                        }
                        Button(
                            onClick = {
                                //                        Add it to destination list
                            },
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            Text("Add")
                        }

                    }
                    Log.d("trip destination","$trip.destinationList.size")
                    if(trip.destinationList.size > 0) {
                        LazyColumn(
                            modifier = Modifier.size(300.dp).align(Alignment.CenterHorizontally)
                        ) {
                            items(trip.destinationList) { destination ->
                                showitem(
                                    itemName = destination,
                                    onUpClick = { /* Handle up arrow click */ },
                                    onDownClick = { /* Handle down arrow click */ },
                                    onDeleteClick = { /* Handle delete button click */ }
                                )
                            }
                        }
                    }
                    else{
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally).size(300.dp) ){
                            Text(text = "The trip is empty",modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun preida() {
    var trip = listOf("kyoto", "Taipei", "NewYork")
    var alltrip = Trip(destinationList = listOf("kyoto", "Taipei", "NewYork", "paris"))
    EditTripDialog(true,alltrip,trip){}
//    showitem(itemName = "taipei", onUpClick = { }, onDownClick = {}, onDeleteClick = {})
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
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Row (verticalAlignment = Alignment.CenterVertically){
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = itemName, fontSize = 30.sp)
                }
            }
        }
    }
}
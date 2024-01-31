package com.example.groupproject.ui.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.groupproject.R
import com.example.groupproject.data.model.Feedback
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingScreen(navController: NavHostController, settingsViewModel: settingViewModel) {
    val context = navController.context
    val sharedPref: SharedPreferences =
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    val user by settingsViewModel.user.collectAsState()
    val feedbacks by settingsViewModel.feedback

    LaunchedEffect(userEmail) {
        settingsViewModel.getUser(userEmail)
        settingsViewModel.getFeedbacks()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Setting",
                color = MaterialTheme.colorScheme.background,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))


        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(320.dp)
                .height(222.dp)
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            text = "Currency",
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold
        )
        var expanded by remember { mutableStateOf(false) }
        var select by remember { mutableStateOf(user!!.currency) }
        val list = arrayOf("USD","CNY","EUR","GPB","JPY")
        val country = arrayOf(R.raw.usd, R.raw.cny, R.raw.eur, R.raw.gpb, R.raw.jpy)
        var countryId = when (select) {
                "USD" -> {
                    R.raw.usd
                }
                "CNY" -> {
                    R.raw.cny
                }
                "EUR" -> {
                    R.raw.eur
                }
                "GPB" -> {
                    R.raw.gpb
                }
                else -> {
                    R.raw.jpy
                }
            }
        val icon = if (expanded) {
            Icons.Outlined.KeyboardArrowUp
        } else {
            Icons.Outlined.KeyboardArrowDown
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = countryId),
                contentDescription = "Country",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 20.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .width(300.dp)
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
                        .height(250.dp),
                ) {
                    list.forEachIndexed() { index, it ->
                        var currencyDialog by remember { mutableStateOf(false) }
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = country[index]),
                                        contentDescription = "Country",
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                                    Text(text = it)
                                }
                            },
                            onClick = {
                                currencyDialog = true
                            }
                        )
                        if (currencyDialog) {
                            AlertDialog(
                                onDismissRequest = { currencyDialog = false },
                                title = {
                                    Text(
                                        text = "Change to $it"
                                    )
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            select = it

                                            user?.currency = it
                                            settingsViewModel.updateUser(user!!, user!!.email)

                                            currencyDialog = false
                                            expanded = false
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            currencyDialog = false
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = "Feedback",
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold
        )
        var feedbackDialog by remember { mutableStateOf(false) }
        OutlinedButton(
            modifier = Modifier.padding(bottom = 80.dp),
            onClick = {
                feedbackDialog = true
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.feedback),
                    contentDescription = "Add"
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = "Feedback",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
        }
        if (feedbackDialog) {
            var dialogDescription by remember { mutableStateOf("") }
            var dialogRating by remember { mutableIntStateOf(0) }
            AlertDialog(
                onDismissRequest = { feedbackDialog = false },
                title = {
                    Text(
                        text = "Give us your feedback"
                    )
                },
                text = {
                    Column {
                        Text("Rating")
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = "Star",
                                colorFilter = ColorFilter.tint(color = if (dialogRating > 0) Color.Cyan else Color.Gray),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        dialogRating = 1
                                    }
                            )
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = "Star",
                                colorFilter = ColorFilter.tint(color = if (dialogRating > 1) Color.Cyan else Color.Gray),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        dialogRating = 2
                                    }
                            )
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = "Star",
                                colorFilter = ColorFilter.tint(color = if (dialogRating > 2) Color.Cyan else Color.Gray),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        dialogRating = 3
                                    }
                            )
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = "Star",
                                colorFilter = ColorFilter.tint(color = if (dialogRating > 3) Color.Cyan else Color.Gray),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        dialogRating = 4
                                    }
                            )
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = "Star",
                                colorFilter = ColorFilter.tint(color = if (dialogRating > 4) Color.Cyan else Color.Gray),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        dialogRating = 5
                                    }
                            )
                            Text(
                                text = dialogRating.toString(),
                                modifier = Modifier.padding(
                                    vertical = 5.dp,
                                    horizontal = 30.dp
                                )
                            )
                        }
                        Text(text = "Tell us what you think")
                        OutlinedTextField(
                            value = dialogDescription,
                            onValueChange = { dialogDescription = it }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {

                            val currentTimeMillis = System.currentTimeMillis()
                            // Create a SimpleDateFormat instance
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a")
                            // Format the timestamp as a string
                            val formattedDate = dateFormat.format(Date(currentTimeMillis))

                            val feedback = Feedback(feedbackId = formattedDate, description = dialogDescription, rating = dialogRating, userId = user?.userId ?: "")
                            settingsViewModel.addFeedback(feedback)

                            Toast.makeText(context, "Thank you for your feedback", Toast.LENGTH_SHORT).show()

                            feedbackDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            feedbackDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = MaterialTheme.colorScheme.inversePrimary
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))

        var showDialog by remember { mutableStateOf(false) }
        Button(
            modifier = Modifier.padding(bottom = 30.dp),
            onClick = {
                showDialog = true
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.logout),
                contentDescription = "Add"
            )
            Spacer(modifier = Modifier.padding(horizontal = 1.dp))
            Text(
                text = "Logout",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Logout？") },
                text = { Text("Do you want to logout") },
                confirmButton = {
                    Button(
                        onClick = {

                            showDialog = false
                            navController.navigate("login")
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = MaterialTheme.colorScheme.inversePrimary
            )
        }
    }
}
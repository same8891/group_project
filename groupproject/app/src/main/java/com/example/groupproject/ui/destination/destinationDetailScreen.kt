package com.example.groupproject.ui.destination

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Trip
import com.example.groupproject.data.model.User
import com.example.groupproject.ui.trips.tripeditdialog
import com.example.groupproject.ui.util.ImageWall
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firestore.admin.v1.Index
import com.yisheng.shoppingapplication.ui.home.ImageSlider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun destinationDetailScreen(
    navController: NavHostController,
    destinationName: String,
    destinationDetailViewModel: destinationDetailViewModel
) {
    var destination by remember { mutableStateOf<Destination?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf(User()) }
    var tripDialogShow by remember { mutableStateOf(false) }
    var currentTripIndex by remember { mutableStateOf(0) }
    var allDestination by remember { mutableStateOf(listOf<Destination>()) }
    // Trigger fetching destination detail when the screen is first launched
    LaunchedEffect(destinationName) {
        isLoading = true
        destinationDetailViewModel.getDestinationByName(destinationName) {
            destination = it
        }
        val sharedPref: SharedPreferences = navController.context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("email","") ?: ""
        destinationDetailViewModel.getAllDestination { allDestination = it }
        destinationDetailViewModel.getUser(userEmail) {
            user = it ?: User()
            isLoading = false
        }
    }

    // Check if the destination is still loading
    if (isLoading) {
        // You can display a loading indicator or some other UI here
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        // Display the destination details once loaded

        Box(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)) {
                FloatingActionButton(
                    onClick = { expanded = true
                              destinationDetailViewModel.getUser(user.email) {
                                  user = it ?: User()
                              }},
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Align to the bottom end (right-bottom) of the screen
                        .padding(16.dp)
                        .zIndex(1f)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add to List")
                    if (expanded) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .align(Alignment.TopEnd)
                                .zIndex(2f) // Ensure the DropdownMenu has a higher zIndex
                                .background(MaterialTheme.colors.surface)
                        ) {
                            val tripList = user.trips
                            tripList.forEachIndexed() { index, trip ->
                                DropdownMenuItem(onClick = {
                                    if (trip.destinationList.contains(destination!!.name)) {
                                        Toast.makeText(
                                            navController.context,
                                            "Destination already in ${trip.title}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        destinationDetailViewModel.addDestinationToTrip(destination!!, user, trip)
                                        Toast.makeText(
                                            navController.context,
                                            "Added ${destination!!.name} to ${trip.title} successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }, text = { Text("${trip.title} : StartDate: ${trip.startDate}") })
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                Toast.makeText(
                                    navController.context,
                                    "Add To New trip",
                                    Toast.LENGTH_SHORT
                                ).show()
                                tripDialogShow = true
                            }, text = { Text("Add To New trip") })
                        }
                    }
                }
            }


            // Add more items as needed
            DestinationDetailsContent(
                navController = navController,
                destination = destination!!,
                destinationDetailViewModel = destinationDetailViewModel,
                currency = user.currency ?: "USD"
            )

            if (tripDialogShow) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))) {
                    tripeditdialog(
                        trip = Trip(),
                        AllDestination = allDestination.map { it.name },
                        onDismiss = { tripDialogShow = false },
                    )
                }
            }
        }


    }

}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun DestinationDetailsContent(
    navController: NavHostController,
    destination: Destination,
    destinationDetailViewModel: destinationDetailViewModel,
    currency: String
) {
    val averageRating = destination.reviews.map { it.rating }.average()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Displaying images
                    ImageSlider(
                        imageUrlList = destination.images,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    )

                    // Average rating
                    Text(
                        text = "Average Rating: ${averageRating}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Activities: ${destination.activityList.joinToString(", ")}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Likes: ${destination.likes}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Local Currencies: ${destination.localCurrencies.joinToString(", ")}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Local Language: ${destination.localLanguages.joinToString(", ")}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Tags: ${destination.tags.joinToString(", ")}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    // Tabs for Description, Reviews, and Map
                    DestinationTabs(
                        navController = navController,
                        destination = destination,
                        destinationDetailViewModel = destinationDetailViewModel,
                        currency = currency
                    )

                    // Add other destination details components here based on your requirements
                }

            }
        }
    }
}

@Composable
private fun DestinationTabs(
    navController: NavHostController,
    destination: Destination,
    destinationDetailViewModel: destinationDetailViewModel,
    currency: String
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .height(48.dp)
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    selectedTabIndex = 0
                }
            ) {
                Text("Description")
            }

            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    selectedTabIndex = 1
                }
            ) {
                Text("Reviews")
            }

            Tab(
                selected = selectedTabIndex == 2,
                onClick = {
                    selectedTabIndex = 2
                }
            ) {
                Text("Map")
            }

            Tab(
                selected = selectedTabIndex == 3,
                onClick = {
                    selectedTabIndex = 3
                }
            ) {
                Text("Picutres")
            }
        }

        // Content based on selected tab
        when (selectedTabIndex) {
            0 -> DestinationDescription(destination = destination, currency = currency)
            1 -> DestinationReviews(
                navController = navController,
                destination = destination,
                destinationDetailViewModel = destinationDetailViewModel
            )

            2 -> DestinationMap(navController = navController, destination = destination)
            3 -> DestinationPictures(destination = destination)
        }
    }
}

@Composable
fun DestinationPictures(destination: Destination) {
    ImageWall(imageUrlList = destination.images)
}

@Composable
private fun DestinationDescription(destination: Destination, currency: String) {
    // Displaying basic details
    Text(
        text = "Location: ${destination.location}",
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(text = "Price: ${destination.price.value} ${destination.price.currency}",
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(text = "Price in $currency: ${destination.price.convertTo(currency).value} $currency",
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
        text = "Description: ${destination.description}",
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 16.dp)
    )

}

@Composable
private fun DestinationReviews(
    navController: NavHostController,
    destination: Destination,
    destinationDetailViewModel: destinationDetailViewModel
) {

    var reviewDialogShown by remember { mutableStateOf(false) }
    // Review dialog
    if (reviewDialogShown) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))) {
            reviewdialog(
                destination = destination,
                isDestinationReviewed = false,
                rating = 5,
                reviewText = "",
                onDismiss = { reviewDialogShown = false },
                onSubmit = { rating, reviewText ->
                    destinationDetailViewModel.updateReviewLikes("userId", 1)
                    reviewDialogShown = false
                }
            )
        }
    }
    // Displaying reviews
    if (destination.reviews.isNotEmpty()) {
        // Button to write a review
        Button(
            onClick = {
                reviewDialogShown = true
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Write a Review")
        }
        // Count the number of reviews for each star rating
        val starRatingsCount = mutableMapOf<Int, Int>().withDefault { 0 }
        destination.reviews.forEach { review ->
            starRatingsCount[review.rating] = starRatingsCount.getValue(review.rating) + 1
        }

        // Display the count for each star rating
        starRatingsCount.toSortedMap(reverseOrder()).forEach { (rating, count) ->
            val stars = buildString {
                repeat(rating) {
                    append("⭐")
                }
            }
            Text("$stars: $count", modifier = Modifier.padding(top = 4.dp))
        }

        destination.reviews.forEach { review ->
            reviewCard(review = review, destinationDetailViewModel = destinationDetailViewModel)
        }
    }
}

@Composable
private fun DestinationMap(navController: NavHostController, destination: Destination) {
    val apiKey = "AIzaSyDS8L5B2IitXRFDYLCfSfWcyUydvCuCjt0"
    val destinationLocation = destination.location

    val imageUrl = "https://maps.googleapis.com/maps/api/staticmap?size=512x512&maptype=roadmap" +
            "&markers=size:mid%7Ccolor:red%7C" + destinationLocation +
            "&key=" + apiKey

    AsyncImage(
        model = imageUrl,
        contentDescription = "Translated description of what the image contains",
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .clickable {
                val uri = "geo:${destinationLocation}?q=${destinationLocation}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

                // 检查是否有应用程序可以处理该意图
                if (intent.resolveActivity(navController.context.packageManager) != null) {
                    navController.context.startActivity(intent)
                } else {
                    // 如果没有应用程序可以处理，执行备用操作，例如显示消息
                    Toast
                        .makeText(
                            navController.context,
                            "No app found to handle the intent",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }
    )
}
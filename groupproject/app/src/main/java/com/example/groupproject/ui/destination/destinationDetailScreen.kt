package com.example.groupproject.ui.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.groupproject.data.model.Destination
import com.yisheng.shoppingapplication.ui.home.ImageSlider

@Composable
fun destinationDetailScreen(
    navController: NavHostController,
    destinationName: String,
    destinationDetailViewModel: destinationDetailViewModel
) {
    var destination by remember { mutableStateOf<Destination?>(null) }

    // Trigger fetching destination detail when the screen is first launched
    LaunchedEffect(destinationName) {
        destinationDetailViewModel.getDestinationByName(destinationName) {
            destination = it
        }
    }

    // Fetch the destination detail from the ViewModel
    val currentDestination = destinationDetailViewModel.getDestinationDetail()

    // Check if the destination is still loading
    if (currentDestination == null) {
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
        DestinationDetailsContent(destination = currentDestination, destinationDetailViewModel = destinationDetailViewModel)
    }
}

@Composable
private fun DestinationDetailsContent(destination: Destination, destinationDetailViewModel: destinationDetailViewModel) {
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
                        imageUrls = destination.images,
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

                    // Displaying basic details
                    Text(
                        text = "Location: ${destination.location}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Description: ${destination.description}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Displaying activity list
                    if (destination.activityList.isNotEmpty()) {
                        Text(
                            text = "Activities:",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                        destination.activityList.forEach { activity ->
                            Text(
                                text = " - $activity",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    // Displaying price information
                    Text(
                        text = "Price: ${destination.price.value} ${destination.price.currency}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    // Displaying likes
                    Text(
                        text = "Likes: ${destination.likes}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Displaying reviews
                    if (destination.reviews.isNotEmpty()) {
                        Text(
                            text = "Reviews:",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                        destination.reviews.forEach { review ->
                            reviewCard(review = review, destinationDetailViewModel = destinationDetailViewModel)
                        }
                    }

                    // Add other destination details components here based on your requirements
                }
            }
        }
    }
}
package com.example.groupproject.ui.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.groupproject.data.model.Destination

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
        DestinationDetailsContent(destination = currentDestination)
    }
}

@Composable
private fun DestinationDetailsContent(destination: Destination) {
    // Your UI layout for displaying destination details goes here
    // You can use Jetpack Compose functions like Column, Row, Text, etc.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        // Displaying images
        destination.images.forEach { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Destination Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .padding(bottom = 8.dp)
            )
        }

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
                // You can customize how you want to display each review
                Text(
                    text = " - ${review.rating} stars: ${review.description}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        // Add other destination details components here based on your requirements
    }
}
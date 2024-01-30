package com.example.groupproject.ui.destination

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.groupproject.data.model.Destination
import com.example.groupproject.ui.util.ImageWall
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
        DestinationDetailsContent(navController = navController, destination = currentDestination, destinationDetailViewModel = destinationDetailViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun DestinationDetailsContent(navController: NavHostController, destination: Destination, destinationDetailViewModel: destinationDetailViewModel) {
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

                    // Tabs for Description, Reviews, and Map
                    DestinationTabs(
                        navController = navController,
                        destination = destination,
                        destinationDetailViewModel = destinationDetailViewModel
                    )

                    // Add other destination details components here based on your requirements
                }
            }
        }
    }
}

@Composable
private fun DestinationTabs(navController: NavHostController, destination: Destination, destinationDetailViewModel: destinationDetailViewModel) {
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
            0 -> DestinationDescription(destination = destination)
            1 -> DestinationReviews(destination = destination, destinationDetailViewModel = destinationDetailViewModel)
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
private fun DestinationDescription(destination: Destination) {
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
}

@Composable
private fun DestinationReviews(destination: Destination, destinationDetailViewModel: destinationDetailViewModel) {
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
}

@Composable
private fun DestinationMap(navController: NavHostController, destination: Destination) {
    val apiKey = "AIzaSyDS8L5B2IitXRFDYLCfSfWcyUydvCuCjt0"
    val destinationLocation = destination.location
    AsyncImage(
        model = "https://maps.googleapis.com/maps/api/staticmap?size=512x512&maptype=roadmap" +
                "&markers=size:mid%7Ccolor:red%7C" + destinationLocation +
                "&key=" + apiKey,
        contentDescription = "Translated description of what the image contains",
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}

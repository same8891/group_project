package com.example.groupproject.ui.destination

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun destinationDetailScreen(
    navController: NavHostController,
    destination: String,
    destinationDetailViewModel: destinationDetailViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Text(text = "Destination Detail Screen")
            Text(text = "Destination: $destination")
        }
    }
}
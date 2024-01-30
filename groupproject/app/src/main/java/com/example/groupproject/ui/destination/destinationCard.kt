package com.example.groupproject.ui.destination

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.groupproject.data.model.Destination
import com.example.groupproject.ui.home.CheckBoxList
import com.example.groupproject.ui.home.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun destinationCard(navController: NavController, destination: Destination) {
    Card(
        onClick = {
            navController.navigate("destinationDetail/${destination.name}")
                  },
        modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = destination.images[0],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            // Destination name
            Text(
                text = destination.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "rating",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(text = destination.reviews.map { it.rating }.average().toString())

                Spacer(modifier = Modifier.width(16.dp))

                // Location
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = destination.location)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = destination.likes.toString())

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                destination.tags.forEach { tag ->
                    Log.d("tags","$tag")
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = tag,
                        color = MaterialTheme.colorScheme.secondary,

                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun card() {
    var image = listOf<String>("https://firebasestorage.googleapis.com/v0/b/groupproject-4f97b.appspot.com/o/Arashiyama.jpg?alt=media&token=3f3ca39a-3b2a-4ca6-8350-17b4806151b4")
    var d = Destination(name = "Arashiyama",tags = listOf("nature", "culture"), likes = 5, images = image, description = "Explore the romantic charm of Paris, known as the City of Lights.", location = "Kyoto, Japan")
    val navController = rememberNavController()
    destinationCard(destination = d,navController = navController)
}
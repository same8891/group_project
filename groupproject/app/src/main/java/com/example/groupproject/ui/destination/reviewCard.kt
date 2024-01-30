package com.example.groupproject.ui.destination

import android.icu.text.SimpleDateFormat
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.groupproject.R
import com.example.groupproject.data.model.Review
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import java.util.Locale


@Composable
fun reviewCard(review: Review, destinationDetailViewModel: destinationDetailViewModel) {

    var likes by remember { mutableStateOf(review.likes) }

    // Create an Animatable for the like count with spring animation
    val animatedLikes = remember { androidx.compose.animation.core.Animatable(likes.toFloat()) }
    LaunchedEffect(likes) {
        animatedLikes.animateTo(likes.toFloat(), animationSpec = spring())
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display circular avatar
            Image(
                painter = painterResource(id = R.drawable.cat), // replace R.drawable.avt with your actual image resource
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            )

            // Spacer for separation
            Spacer(modifier = Modifier.width(16.dp))

            // Display review content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = review.description,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    items(5) { index ->
                        val starIcon =
                            if (index < review.rating) Icons.Default.Star
                            else if ((index + 0.5).toInt() == review.rating) Icons.Default.StarHalf
                            else Icons.Default.StarBorder

                        Icon(
                            imageVector = starIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like button with animation
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable {
                                likes++
                                destinationDetailViewModel.updateReviewLikes(review.userId, likes)
                            }
                            .padding(4.dp)
                            .animateContentSize()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = likes.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Display user information
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display user ID and format the timestamp
                    Text(
                        text = "Reviewed by: ${review.userId} on ${review.timestamp?.toFormattedString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

// Extension function to format Timestamp to String
fun Timestamp.toFormattedString(format: String = "yyyy/MM/dd"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(this.toDate())
}

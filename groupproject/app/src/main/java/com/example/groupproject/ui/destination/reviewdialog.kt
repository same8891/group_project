package com.example.groupproject.ui.destination

import android.content.Context
import android.content.SharedPreferences
import android.widget.RatingBar
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import com.example.groupproject.ui.home.SortOrder
import com.example.groupproject.ui.profile.profileViewModel


@Composable
fun reviewdialog(
    isDestinationReviewed:Boolean = true,
    rating: Int = 5,
    reviewText: String = "",
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit) {
    var rating by remember { mutableStateOf(rating) }
    var reviewText by remember { mutableStateOf(reviewText) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if(isDestinationReviewed == false) {
                Text(
                    text = "Add a review",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 20.sp
                )
            }
            else{
                Text(
                    text = "Edit a review",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 20.sp
                )
            }
//             Rating Bar
            RatingBar(
                rating = rating,
                onRatingChanged = { newRating ->
                    rating = newRating
                }
            )
            // Review Text
            TextField(
                value = reviewText,
                onValueChange = { newText ->
                    reviewText = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSubmit(rating, reviewText)
                        onDismiss()
                    }
                )
            )
            // Submit Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                if(isDestinationReviewed == false) {
                    Button(onClick = {
                        onSubmit(rating, reviewText)
                    }) {
                        Text("Submit")
                    }
                }
                else{
                    Button(onClick = {
                        onSubmit(rating, reviewText)
                    }) {
                        Text("Edit")
                    }
                }
            }

        }
    }
}
@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .width(250.dp)
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val activeColor = Color.Yellow
        Text(text = "rating:")
        for (i in 0 until 5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
                    .clickable {
                        onRatingChanged(i + 1)
                    },
                tint = if (i < rating) activeColor else Color.Gray
            )

        }
    }
}
@Preview(showBackground = true)
@Composable
fun preida() {
    reviewdialog(onDismiss = {}, onSubmit = {ratinf,review->})
}
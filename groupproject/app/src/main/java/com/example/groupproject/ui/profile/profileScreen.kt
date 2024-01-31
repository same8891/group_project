package com.example.groupproject.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.groupproject.R
import com.example.groupproject.data.model.User
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.Review
import com.example.groupproject.data.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint("MutableCollectionMutableState")
@Composable
fun profileScreen(navController: NavController, profileViewModel: profileViewModel) {
    val context = navController.context
    val sharedPref: SharedPreferences =
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    var user by remember { mutableStateOf(User())}
    var radio by remember { mutableStateOf("Date") }
    var userDisplayName by remember { mutableStateOf("") }
    var userProfile by remember { mutableStateOf(Profile()) }
    var userTrips by remember { mutableStateOf(emptyList<Trip>()) }
    var userReviews by remember { mutableStateOf(emptyList<Review>()) }
    var userImage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = userEmail) {
        profileViewModel.getUser(userEmail) { thisUser ->
            if (thisUser != null) {
                user = thisUser
                userDisplayName = user.displayName
                userProfile = user.profile[0]
                userTrips = user.trips
                userReviews = user.reviews
                userImage = user.profile[0].photoImage
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = userDisplayName,
                    color = MaterialTheme.colorScheme.background,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                    textAlign = TextAlign.Center
                )
                user?.profile?.forEach {
                    // 使用 remember 创建一个可观察的状态以保存图片的URL
                    var uploadImageUrl = remember { mutableStateOf("") }
                    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                        uri?.let { selectedUri ->
                            // 选择了图片，你可以在这里处理
                            user?.let {
                                profileViewModel.uploadImage(it.userId, uri) { imageUrl: String? ->
                                    if (imageUrl != null) {
                                        // 图片上传成功，imageUrl 是上传后的图片URL
                                        Log.d("SelectedImage", "Image URL: $imageUrl")
                                        // 返回图片URL
                                        uploadImageUrl.value = imageUrl
                                        profileViewModel.updateProfileImageFromUrl(
                                            user.userId,
                                            uploadImageUrl.value)
                                        profileViewModel.getUser(userEmail) { thisUser ->
                                            if (thisUser != null) {
                                                user = thisUser
                                                userDisplayName = user.displayName
                                                userProfile = user.profile[0]
                                                userTrips = user.trips
                                                userReviews = user.reviews
                                                userImage = user.profile[0].photoImage
                                            }
                                        }
                                        Toast.makeText(
                                            navController.context,
                                            "Image uploaded successfully with URL: $imageUrl",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // 图片上传失败或获取URL失败
                                        Log.e("SelectedImage", "Failed to upload image or get URL")
                                    }
                                }
                            }
                        }
                    }
                    val uploadImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                        uri?.let { selectedUri ->
                            // 选择了图片，你可以在这里处理
                            user?.let {
                                profileViewModel.uploadImage(it.userId, uri) { imageUrl: String? ->
                                    if (imageUrl != null) {
                                        // 图片上传成功，imageUrl 是上传后的图片URL
                                        Log.d("SelectedImage", "Image URL: $imageUrl")
                                        // 返回图片URL
                                        uploadImageUrl.value = imageUrl
                                        profileViewModel.uploadUserImage(user.userId, imageUrl)
                                        profileViewModel.getUser(userEmail) { thisUser ->
                                            if (thisUser != null) {
                                                user = thisUser
                                                userDisplayName = user.displayName
                                                userProfile = user.profile[0]
                                                userTrips = user.trips
                                                userReviews = user.reviews
                                                userImage = user.profile[0].photoImage
                                            }
                                        }
                                        Toast.makeText(
                                            navController.context,
                                            "Image uploaded successfully with URL: $imageUrl",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // 图片上传失败或获取URL失败
                                        Log.e("SelectedImage", "Failed to upload image or get URL")
                                    }
                                }
                            }
                        }
                    }
                    ElevatedCard(
                        modifier = Modifier
                            .size(
                                height = 300.dp,
                                width = 400.dp
                            )
                            .padding(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            Image(
                                painter = rememberAsyncImagePainter(userImage),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        getContent.launch("image/*")
                                    }
                            )
                            Spacer(modifier = Modifier.weight(0.5f))
                            Row {
                                Spacer(modifier = Modifier.weight(0.25f))
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.upload),
                                    contentDescription = "Upload",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            uploadImage.launch("image/*")
                                        })
                                Spacer(modifier = Modifier.weight(0.25f))
                                var nameDialog by remember { mutableStateOf(false) }
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.rename),
                                    contentDescription = "Rename",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            nameDialog = true
                                        }
                                )
                                if (nameDialog) {
                                    AlertDialog(
                                        onDismissRequest = { nameDialog = false },
                                        title = {
                                            Text(
                                                text = "Edit Display Name",
                                            )
                                        },
                                        text = {
                                            TextField(
                                                value = userDisplayName,
                                                onValueChange = { userDisplayName = it }
                                            )
                                        },
                                        confirmButton = {
                                            Button(
                                                onClick = {
                                                    user?.displayName = userDisplayName
                                                    profileViewModel.updateUser(
                                                        user!!,
                                                        user.email
                                                    )
                                                    profileViewModel.getUser(userEmail) { thisUser ->
                                                        if (thisUser != null) {
                                                            user = thisUser
                                                            userDisplayName = user.displayName
                                                            userProfile = user.profile[0]
                                                            userTrips = user.trips
                                                            userReviews = user.reviews
                                                            userImage = user.profile[0].photoImage
                                                        }
                                                    }
                                                    nameDialog = false
                                                }
                                            ) {
                                                Text("Confirm")
                                            }
                                        },
                                        dismissButton = {
                                            Button(
                                                onClick = {
                                                    nameDialog = false
                                                }
                                            ) {
                                                Text("Cancel")
                                            }
                                        },
                                        containerColor = MaterialTheme.colorScheme.inversePrimary
                                    )
                                }
                                Spacer(modifier = Modifier.weight(0.25f))
                                var accountDialog by remember { mutableStateOf(false) }
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.delete),
                                    contentDescription = "Delete",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            accountDialog = true
                                        }
                                )
                                if (accountDialog) {
                                    AlertDialog(
                                        onDismissRequest = { accountDialog = false },
                                        title = {
                                            Text(
                                                text = "Delete the Account?",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        text = {
                                            Text(
                                                text = "You want to delete your account",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        confirmButton = {
                                            Button(
                                                onClick = {
                                                    accountDialog = false
                                                }
                                            ) {
                                                Text("Confirm")
                                            }
                                        },
                                        dismissButton = {
                                            Button(
                                                onClick = {
                                                    accountDialog = false
                                                }
                                            ) {
                                                Text("Cancel")
                                            }
                                        },
                                        containerColor = MaterialTheme.colorScheme.inversePrimary
                                    )
                                }
                                Spacer(modifier = Modifier.weight(0.25f))
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var ascending by remember { mutableStateOf(true) }
                    Text(
                        text = "Ascending",
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    Switch(
                        checked = ascending,
                        onCheckedChange = {
                            ascending = it
                        }
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    val date = "Date"
                    RadioButton(
                        selected = radio == date,
                        onClick = { radio = date }
                    )
                    Text(text = date)
                    val location = "Location"
                    RadioButton(
                        selected = radio == location,
                        onClick = { radio = location }
                    )
                    Text(text = location)
                    val rating = "Rating"
                    RadioButton(
                        selected = radio == rating,
                        modifier = Modifier.padding(0.dp),
                        onClick = { radio = rating }
                    )
                    Text(
                        text = rating,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Gray)
                )
            }
            item {
                user!!.reviews.forEachIndexed { index, it ->
                    var rating by remember { mutableIntStateOf(it.rating) }
                    var description by remember { mutableStateOf(it.description) }
                    var reviewDialog by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { reviewDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = it.photos[0]),
                                contentDescription = "Review Picture",
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.weight(1.0f)
                            ) {
                                Text(
                                    text = it.destination,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.star),
                                        contentDescription = "Star",
                                        colorFilter = ColorFilter.tint(color = if (rating > 0) Color.Cyan else Color.Gray),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.star),
                                        contentDescription = "Star",
                                        colorFilter = ColorFilter.tint(color = if (rating > 1) Color.Cyan else Color.Gray),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.star),
                                        contentDescription = "Star",
                                        colorFilter = ColorFilter.tint(color = if (rating > 2) Color.Cyan else Color.Gray),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.star),
                                        contentDescription = "Star",
                                        colorFilter = ColorFilter.tint(color = if (rating > 3) Color.Cyan else Color.Gray),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.star),
                                        contentDescription = "Star",
                                        colorFilter = ColorFilter.tint(color = if (rating > 4) Color.Cyan else Color.Gray),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                    Text(
                                        text = rating.toString(),
                                        modifier = Modifier.padding(
                                            vertical = 5.dp,
                                            horizontal = 30.dp
                                        )
                                    )
                                }
                                Text(
                                    text = description,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                )
                                val timestamp = it.timestamp?.toDate()

                                val dateFormat =
                                    SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                                val dateString = timestamp?.let { dateFormat.format(it) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "${it.likes} Likes",
                                        color = Color.Blue
                                    )
                                    Spacer(modifier = Modifier.weight(1.0f))
                                    Text(
                                        text = dateString ?: "",
                                        color = Color.Blue
                                    )
                                }
                            }
                            if (reviewDialog) {
                                var dialogDescription by remember { mutableStateOf(description) }
                                var dialogRating by remember { mutableIntStateOf(rating) }
                                AlertDialog(
                                    onDismissRequest = { reviewDialog = false },
                                    title = {
                                        Text(
                                            text = it.destination
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
                                            Text(text = "Review")
                                            TextField(
                                                value = dialogDescription,
                                                onValueChange = { dialogDescription = it }
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                rating = dialogRating
                                                description = dialogDescription
                                                it.rating = dialogRating
                                                it.description = dialogDescription
                                                profileViewModel.updateUser(user!!, user!!.email)
                                                reviewDialog = false
                                            }
                                        ) {
                                            Text("Confirm")
                                        }
                                    },
                                    dismissButton = {
                                        Button(
                                            onClick = {
                                                reviewDialog = false
                                            }
                                        ) {
                                            Text("Cancel")
                                        }
                                    },
                                    containerColor = MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                            var reviewDeleteDialog by remember { mutableStateOf(false) }
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.delete),
                                contentDescription = "Delete",
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.error),
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .size(30.dp)
                                    .clickable {
                                        reviewDeleteDialog = true
                                    }
                            )
                            if (reviewDeleteDialog) {
                                AlertDialog(
                                    onDismissRequest = { reviewDeleteDialog = false },
                                    title = {
                                        Text(
                                            text = "Remove the Review?",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "You want to remove the review",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {

                                                user?.reviews?.removeAt(index)
//                                                navController.navigate("profile")
//                                                profileViewModel.updateUser(user!!, user!!.email)
                                                reviewDeleteDialog = false
                                            }
                                        ) {
                                            Text("Confirm")
                                        }
                                    },
                                    dismissButton = {
                                        Button(
                                            onClick = {
                                                reviewDeleteDialog = false
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
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}

@Composable
fun displayUser(user: User) {
    Text(text = "User email: ${user.email}")
    Text(text = "Display Name: ${user.displayName}")
    Text(text = "Currency: ${user.currency}")

    // Display Trips
    Text(text = "Trips:")
    for (trip in user.trips) {
        displayTrip(trip)
    }

    // Display Profile
    Text(text = "Profile:")
    for (profile in user.profile) {
        displayUserProfile(profile)
    }

    // Display Reviews
    Text(text = "Reviews:")
    for (review in user.reviews) {
        displayReview(review)
    }
}

@Composable
fun displayTrip(trip: Trip) {
    Text(text = "Title: ${trip.title}")
    Text(text = "Description: ${trip.description}")
    Text(text = "Start Date: ${trip.startDate}")
    Text(text = "End Date: ${trip.endDate}")
    // Add more fields as needed
}

@Composable
fun displayUserProfile(profile: Profile) {
    Text(text = "Full Name: ${profile.fullName}")
    Text(text = "About You: ${profile.aboutYou}")
    Text(text = "Join Date: ${profile.joinDate}")
    Text(text = "Location: ${profile.location}")
    // Add more fields as needed
}

@Composable
fun displayReview(review: Review) {
    Text(text = "Description: ${review.description}")
    Text(text = "Rating: ${review.rating}")
    // Add more fields as needed
}


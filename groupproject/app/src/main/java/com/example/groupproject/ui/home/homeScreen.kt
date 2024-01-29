package com.example.groupproject.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.groupproject.data.model.User

@Composable
fun homeScreen(navController: NavHostController, homeViewModel: homeViewModel) {
    // State to hold the user information
    var user by remember { mutableStateOf<User?>(null) }
    val sharedPref: SharedPreferences = navController.context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("email", "") ?: ""
    Log.d("HomeTest", "homeScreen: $userEmail")
    // Fetch user information when the composable is first called
    LaunchedEffect(userEmail) {
        // Ensure userId is not empty before making the API call
        if (userEmail.isNotEmpty()) {
            Log.d("HomeTest", "homeScreen: userEmail is not empty")
            homeViewModel.getUser(userEmail) { fetchedUser ->
                user = fetchedUser
            }
        }
    }

    // Main layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // Display user information if available
            user?.let { displayUserDetails(it) }
        }
    }
}

@Composable
fun displayUserDetails(user: User) {
    Text(text = "User email: ${user.email}")
    Text(text = "Display Name: ${user.displayName}")
}
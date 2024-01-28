package com.example.groupproject.ui.home

import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.User

class homeViewModel(private val firebaseApi: FirebaseApi) {

    // Function to get user by userId
    // Function to get user by userId
    fun getUser(userId: String, callback: (User?) -> Unit) {
        firebaseApi.getUserByEmail(userId) { user ->
            callback(user)
        }
    }
}
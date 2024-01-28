package com.example.groupproject.ui.auth

import androidx.lifecycle.ViewModel
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.User

class AuthViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {

    // Login function
    fun loginUser(email: String, password: String, callback: (User?) -> Unit) {
        // Perform Firebase authentication here
        // You can use Firebase Authentication methods to authenticate the user
        // Once authenticated, retrieve the user information using getUserByEmail
        firebaseApi.getUserByEmail(email) { user ->
            callback(user)
        }
    }

    // Registration function
    fun registerUser(user: User, callback: (Boolean) -> Unit) {
        // Perform Firebase registration here
        // You can use Firebase Authentication methods to create a new user
        // Once registered, save user information using saveUser
        firebaseApi.saveUser(user, user.userId)
        callback(true)
    }
}
package com.example.groupproject.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FireBaseAPI : ViewModel() {

    fun putUser(newUser: user, callback: (Boolean) -> Unit): String {
        val db = FirebaseFirestore.getInstance()
        var result = ""
        try {
            db.collection("users").document(newUser.account).set(newUser)
            callback(true)
        } catch (e: Exception) {
            callback(false)
        }
        return result
    }
     fun checkIfUserExists(userId: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("users")

        userCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                // Check if the document exists
                val exists = documentSnapshot.exists()
                callback(exists)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                callback(false) // Assuming an error means the document doesn't exist
            }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Access the UsersRepository through the application's container
                FireBaseAPI()
            }
        }
    }
}
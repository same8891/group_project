package com.example.groupproject.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FireBaseAPI : ViewModel() {

    suspend fun putUser(newUser: user): String {
        val db = Firebase.firestore
        var result = ""
        try {
            db.collection("users").document(newUser.account).set(newUser).await()
            result = "successful"
            Log.d("error", "success")
        } catch (e: Exception) {
            result = "error"
            Log.d("error", "add error: ${e.message}")
        }
        return result
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
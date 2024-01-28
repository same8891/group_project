package com.example.groupproject.data

import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Feedback
import com.example.groupproject.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseApi {

    private val db = FirebaseFirestore.getInstance()

    // Create or update user
    fun saveUser(user: User, userId: String) {
        db.collection("User")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                println("User saved successfully!")
            }
            .addOnFailureListener {
                println("Error saving user: $it")
            }
    }

    // Read user by email
    fun getUserByEmail(email: String, callback: (User?) -> Unit) {
        db.collection("User")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting user by email: $it")
                callback(null)
            }
    }

    // Read user
    fun getUser(userId: String, callback: (User?) -> Unit) {
        db.collection("User")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting user: $it")
                callback(null)
            }
    }

    // Delete user
    fun deleteUser(userId: String) {
        db.collection("User")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                println("User deleted successfully!")
            }
            .addOnFailureListener {
                println("Error deleting user: $it")
            }
    }

    // Create or update destination
    fun saveDestination(destination: Destination, destinationId: String) {
        db.collection("Destination")
            .document(destinationId)
            .set(destination)
            .addOnSuccessListener {
                println("Destination saved successfully!")
            }
            .addOnFailureListener {
                println("Error saving destination: $it")
            }
    }

    // Read destination
    fun getDestination(destinationId: String, callback: (Destination?) -> Unit) {
        db.collection("Destination")
            .document(destinationId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val destination = documentSnapshot.toObject(Destination::class.java)
                    callback(destination)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting destination: $it")
                callback(null)
            }
    }

    // Delete destination
    fun deleteDestination(destinationId: String) {
        db.collection("Destination")
            .document(destinationId)
            .delete()
            .addOnSuccessListener {
                println("Destination deleted successfully!")
            }
            .addOnFailureListener {
                println("Error deleting destination: $it")
            }
    }

    // Create or update feedback
    fun saveFeedback(feedback: Feedback, feedbackId: String) {
        db.collection("Feedback")
            .document(feedbackId)
            .set(feedback)
            .addOnSuccessListener {
                println("Feedback saved successfully!")
            }
            .addOnFailureListener {
                println("Error saving feedback: $it")
            }
    }

    // Read feedback
    fun getFeedback(feedbackId: String, callback: (Feedback?) -> Unit) {
        db.collection("Feedback")
            .document(feedbackId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val feedback = documentSnapshot.toObject(Feedback::class.java)
                    callback(feedback)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting feedback: $it")
                callback(null)
            }
    }

    // Delete feedback
    fun deleteFeedback(feedbackId: String) {
        db.collection("Feedback")
            .document(feedbackId)
            .delete()
            .addOnSuccessListener {
                println("Feedback deleted successfully!")
            }
            .addOnFailureListener {
                println("Error deleting feedback: $it")
            }
    }


}
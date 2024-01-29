package com.example.groupproject.data

import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Feedback
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.Review
import com.example.groupproject.data.model.Trip
import com.example.groupproject.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseApi {

    private val db = FirebaseFirestore.getInstance()

    // Create or update user
    fun saveUser(user: User, userId: String) {
        // Save user data
        db.collection("User")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                println("User saved successfully!")

                // Save Trips subcollection
                user.trips.forEachIndexed { index, trip ->
                    db.collection("User")
                        .document(userId)
                        .collection("trips")
                        .document("trip_$index")
                        .set(trip)
                        .addOnSuccessListener {
                            println("Trip $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Trip $index: $it")
                        }
                }

                // Save Profile subcollection
                user.profile.forEachIndexed { index, profile ->
                    db.collection("User")
                        .document(userId)
                        .collection("profile")
                        .document("profile_$index")
                        .set(profile)
                        .addOnSuccessListener {
                            println("Profile $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Profile $index: $it")
                        }
                }

                // Save Reviews subcollection
                user.reviews.forEachIndexed { index, review ->
                    db.collection("User")
                        .document(userId)
                        .collection("reviews")
                        .document("review_$index")
                        .set(review)
                        .addOnSuccessListener {
                            println("Review $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Review $index: $it")
                        }
                }
            }
            .addOnFailureListener {
                println("Error saving user: $it")
            }
    }

    fun getUserByEmail(email: String, callback: (User?) -> Unit) {
        db.collection("User")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val user = userDocument.toObject(User::class.java)

                    if (user != null) {
                        // Fetch Trips subcollection
                        userDocument.reference.collection("trips")
                            .get()
                            .addOnSuccessListener { tripsSnapshot ->
                                val tripsList = tripsSnapshot.toObjects(Trip::class.java)
                                user.trips = tripsList

                                // Fetch Profile subcollection
                                userDocument.reference.collection("profile")
                                    .get()
                                    .addOnSuccessListener { profilesSnapshot ->
                                        val profilesList = profilesSnapshot.toObjects(Profile::class.java)
                                        user.profile = profilesList

                                        // Fetch Reviews subcollection
                                        userDocument.reference.collection("reviews")
                                            .get()
                                            .addOnSuccessListener { reviewsSnapshot ->
                                                val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                                                user.reviews = reviewsList

                                                callback(user)
                                            }
                                            .addOnFailureListener {
                                                println("Error getting Reviews subcollection: $it")
                                                callback(null)
                                            }
                                    }
                                    .addOnFailureListener {
                                        println("Error getting Profile subcollection: $it")
                                        callback(null)
                                    }
                            }
                            .addOnFailureListener {
                                println("Error getting Trips subcollection: $it")
                                callback(null)
                            }
                    } else {
                        callback(null)
                    }
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
        val userRef = db.collection("User").document(userId)
        userRef.get()
            .addOnSuccessListener { userDocument ->
                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)

                    // Fetch Trips subcollection
                    userRef.collection("trips")
                        .get()
                        .addOnSuccessListener { tripsSnapshot ->
                            val tripsList = tripsSnapshot.toObjects(Trip::class.java)
                            user?.trips = tripsList

                            // Fetch Profile subcollection
                            userRef.collection("profile")
                                .get()
                                .addOnSuccessListener { profilesSnapshot ->
                                    val profilesList = profilesSnapshot.toObjects(Profile::class.java)
                                    user?.profile = profilesList

                                    // Fetch Reviews subcollection
                                    userRef.collection("reviews")
                                        .get()
                                        .addOnSuccessListener { reviewsSnapshot ->
                                            val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                                            user?.reviews = reviewsList
                                            callback(user)
                                        }
                                        .addOnFailureListener {
                                            println("Error getting Reviews subcollection: $it")
                                            callback(null)
                                        }
                                }
                                .addOnFailureListener {
                                    println("Error getting Profile subcollection: $it")
                                    callback(null)
                                }
                        }
                        .addOnFailureListener {
                            println("Error getting Trips subcollection: $it")
                            callback(null)
                        }
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


    // Get destinations by name
    fun getDestinationByName(destinationName: String, callback: (Destination?) -> Unit) {
        db.collection("Destination")
            .whereEqualTo("name", destinationName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val destination = querySnapshot.documents[0].toObject(Destination::class.java)

                    // Fetch Reviews subcollection
                    if (destination != null) {
                        val destinationId = querySnapshot.documents[0].id
                        val reviewsRef = db.collection("Destination").document(destinationId)
                            .collection("reviews")

                        reviewsRef.get()
                            .addOnSuccessListener { reviewsSnapshot ->
                                val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                                destination.reviews = reviewsList

                                callback(destination)
                            }
                            .addOnFailureListener {
                                println("Error getting Reviews subcollection: $it")
                                callback(null)
                            }
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting destination by name: $it")
                callback(null)
            }
    }


    // Create or update destination
    fun saveDestination(destination: Destination, destinationId: String) {
        // Save destination data
        db.collection("Destination")
            .document(destinationId)
            .set(destination)
            .addOnSuccessListener {
                println("Destination saved successfully!")

                // Save Reviews subcollection
                destination.reviews.forEachIndexed { index, review ->
                    db.collection("Destination")
                        .document(destinationId)
                        .collection("reviews")
                        .document("review_$index")
                        .set(review)
                        .addOnSuccessListener {
                            println("Review $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Review $index: $it")
                        }
                }
            }
            .addOnFailureListener {
                println("Error saving destination: $it")
            }
    }

    // Read destination
    fun getDestination(destinationId: String, callback: (Destination?) -> Unit) {
        val destinationRef = db.collection("Destination").document(destinationId)

        destinationRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val destination = documentSnapshot.toObject(Destination::class.java)

                    // Fetch Reviews subcollection
                    destinationRef.collection("reviews")
                        .get()
                        .addOnSuccessListener { reviewsSnapshot ->
                            val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                            destination?.reviews = reviewsList

                            callback(destination)
                        }
                        .addOnFailureListener {
                            println("Error getting Reviews subcollection: $it")
                            callback(null)
                        }
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

    // Get all destinations
    fun getAllDestinations(callback: (List<Destination>) -> Unit) {
        db.collection("Destination")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val destinationList = mutableListOf<Destination>()

                for (document in querySnapshot) {
                    val destination = document.toObject(Destination::class.java)

                    // Fetch Reviews subcollection for each destination
                    document.reference.collection("reviews")
                        .get()
                        .addOnSuccessListener { reviewsSnapshot ->
                            val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                            destination.reviews = reviewsList

                            destinationList.add(destination)

                            // Check if it's the last document, then call the callback
                            if (destinationList.size == querySnapshot.documents.size) {
                                callback(destinationList)
                            }
                        }
                        .addOnFailureListener {
                            println("Error getting Reviews subcollection: $it")
                            callback(emptyList())
                        }
                }
            }
            .addOnFailureListener {
                println("Error getting all destinations: $it")
                callback(emptyList())
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


    //Get all feedbacks
    fun getAllFeedbacks(callback: (List<Feedback>) -> Unit) {
        db.collection("Feedback")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val feedbackList = mutableListOf<Feedback>()

                for (document in querySnapshot) {
                    val feedback = document.toObject(Feedback::class.java)
                    feedbackList.add(feedback)
                }
                callback(feedbackList)
            }
            .addOnFailureListener {
                println("Error getting all feedbacks: $it")
                callback(emptyList())
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